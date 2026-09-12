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
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

class MethodNodeLoader {
  private final UaNodeContext context;

  private final NodeManager<UaNode> nodeManager;

  MethodNodeLoader(UaNodeContext context, NodeManager<UaNode> nodeManager) {
    this.context = context;
    this.nodeManager = nodeManager;
  }

  void loadNode0() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11489"),
            new QualifiedName(0, "GetMonitoredItems"),
            new LocalizedText("", "GetMonitoredItems"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11489"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11490").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11489"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11491").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11489"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11489"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2004").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12871"),
            new QualifiedName(0, "ResendData"),
            new LocalizedText("", "ResendData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12871"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12872").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12871"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12871"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2004").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12746"),
            new QualifiedName(0, "SetSubscriptionDurable"),
            new LocalizedText("", "SetSubscriptionDurable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12746"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12747").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12746"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12748").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12746"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12746"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2004").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12883"),
            new QualifiedName(0, "RequestServerStateChange"),
            new LocalizedText("", "RequestServerStateChange"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12883"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12884").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12883"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12883"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2004").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16296"),
            new QualifiedName(0, "AddRole"),
            new LocalizedText("", "AddRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16296"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16297").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16296"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16298").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16296"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16296"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16295").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16299"),
            new QualifiedName(0, "RemoveRole"),
            new LocalizedText("", "RemoveRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16299"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16300").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16299"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16299"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16295").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=32416"),
            new QualifiedName(0, "Failover"),
            new LocalizedText("", "Failover"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=32416"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32411").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11580"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11580"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11581").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11580"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11582").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11580"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11580"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11583"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11583"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11584").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11583"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11583"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11585"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11585"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11586").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11585"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11587").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11585"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11585"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11588"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11588"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11589").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11588"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11588"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11590"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11590"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11591").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11590"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11592").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11590"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11590"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11593"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11593"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11594").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11593"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11593"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11615"),
            new QualifiedName(0, "ExportNamespace"),
            new LocalizedText("", "ExportNamespace"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11615"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11615"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11595").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11629"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11629"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11630").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11629"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11631").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11629"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11629"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11632"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11632"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11633").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11632"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11632"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11634"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11634"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11635").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11634"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11636").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11634"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11634"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11637"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11637"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11638").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11637"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11637"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11639"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11639"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11640").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11639"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11641").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11639"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11639"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11642"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11642"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11643").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11642"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11642"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16301"),
            new QualifiedName(0, "AddRole"),
            new LocalizedText("", "AddRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16301"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16302").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16301"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16303").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16301"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15606").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16304"),
            new QualifiedName(0, "RemoveRole"),
            new LocalizedText("", "RemoveRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16304"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16305").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16304"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15606").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=11492"),
            new QualifiedName(0, "GetMonitoredItems"),
            new LocalizedText("", "GetMonitoredItems"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11492"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11493").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11492"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11494").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11492"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2253").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12873"),
            new QualifiedName(0, "ResendData"),
            new LocalizedText("", "ResendData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12873"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12874").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12873"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2253").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12749"),
            new QualifiedName(0, "SetSubscriptionDurable"),
            new LocalizedText("", "SetSubscriptionDurable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12749"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12750").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12749"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12751").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12749"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2253").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12886"),
            new QualifiedName(0, "RequestServerStateChange"),
            new LocalizedText("", "RequestServerStateChange"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12886"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12887").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12886"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2253").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13355"),
            new QualifiedName(0, "CreateDirectory"),
            new LocalizedText("", "CreateDirectory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13355"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13356").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13355"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13357").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13355"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13355"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13354").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13358"),
            new QualifiedName(0, "CreateFile"),
            new LocalizedText("", "CreateFile"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13358"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13359").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13358"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13360").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13358"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13358"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13354").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17718"),
            new QualifiedName(0, "Delete"),
            new LocalizedText("", "Delete"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17718"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17719").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17718"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17718"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13354").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13363"),
            new QualifiedName(0, "MoveOrCopy"),
            new LocalizedText("", "MoveOrCopy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13363"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13364").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13363"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13365").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13363"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13363"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13354").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13372"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13372"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13373").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13372"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13374").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13372"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13372"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13366").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13375"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13375"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13376").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13375"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13375"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13366").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13377"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13377"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13378").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13377"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13379").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13377"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13377"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13366").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13380"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13380"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13381").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13380"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13380"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13366").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13382"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13382"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13383").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13382"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13384").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13382"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13382"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13366").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13385"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13385"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13386").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13385"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13385"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13366").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13387"),
            new QualifiedName(0, "CreateDirectory"),
            new LocalizedText("", "CreateDirectory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13387"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13388").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13387"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13389").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13387"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13387"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13353").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13390"),
            new QualifiedName(0, "CreateFile"),
            new LocalizedText("", "CreateFile"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13390"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13391").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13390"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13392").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13390"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13390"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13353").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13393"),
            new QualifiedName(0, "Delete"),
            new LocalizedText("", "Delete"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13393"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13394").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13393"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13393"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13353").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13395"),
            new QualifiedName(0, "MoveOrCopy"),
            new LocalizedText("", "MoveOrCopy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13395"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13396").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13395"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13397").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13395"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13395"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13353").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16348"),
            new QualifiedName(0, "CreateDirectory"),
            new LocalizedText("", "CreateDirectory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16348"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16349").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16348"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16350").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16348"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16314").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16351"),
            new QualifiedName(0, "CreateFile"),
            new LocalizedText("", "CreateFile"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16351"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16352").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16351"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16353").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16351"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16314").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16354"),
            new QualifiedName(0, "Delete"),
            new LocalizedText("", "Delete"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16354"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16355").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16354"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16314").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16356"),
            new QualifiedName(0, "MoveOrCopy"),
            new LocalizedText("", "MoveOrCopy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16356"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16357").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16356"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16358").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16356"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16314").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15746"),
            new QualifiedName(0, "GenerateFileForRead"),
            new LocalizedText("", "GenerateFileForRead"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15746"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15747").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15746"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15748").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15746"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15746"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15744").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15749"),
            new QualifiedName(0, "GenerateFileForWrite"),
            new LocalizedText("", "GenerateFileForWrite"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15749"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16359").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15749"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15750").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15749"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15749"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15744").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15751"),
            new QualifiedName(0, "CloseAndCommit"),
            new LocalizedText("", "CloseAndCommit"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15751"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15752").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15751"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15753").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15751"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15751"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15744").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15794"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15794"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15794"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15754").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15843"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15843"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15843"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15803").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15997"),
            new QualifiedName(0, "AddRole"),
            new LocalizedText("", "AddRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15997"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15998").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15997"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15999").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15997"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15997"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15607").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16000"),
            new QualifiedName(0, "RemoveRole"),
            new LocalizedText("", "RemoveRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16000"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16001").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16000"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16000"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15607").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15624"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15624"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15625").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15624"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15624"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15620").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15626"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15626"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15627").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15626"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15626"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15620").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16176"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16176"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16177").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16176"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16176"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15620").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16178"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16178"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16179").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16178"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16178"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15620").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16180"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16180"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16181").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16180"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16180"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15620").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16182"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16182"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16183").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16182"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16182"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15620").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15672"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15672"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15673").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15672"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15668").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15674"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15674"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15675").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15674"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15668").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16217"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16217"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16218").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16217"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15668").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16219"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16219"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16220").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16219"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15668").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16221"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16221"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16222").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16221"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15668").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16223"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16223"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16224").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16223"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15668").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15684"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15684"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15685").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15684"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15680").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15686"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15686"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15687").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15686"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15680").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16228"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16228"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16229").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16228"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15680").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16230"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16230"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16231").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16230"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15680").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode67() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16232"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16232"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16233").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16232"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15680").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode68() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16234"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16234"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16235").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16234"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15680").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode69() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16041"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16041"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16042").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16041"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16036").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode70() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16043"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16043"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16044").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16043"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16036").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode71() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16239"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16239"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16240").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16239"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16036").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode72() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16241"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16241"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16242").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16241"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16036").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode73() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16243"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16243"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16244").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16243"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16036").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode74() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16245"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16245"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16246").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16245"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16036").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode75() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15696"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15696"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15697").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15696"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15692").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode76() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15698"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15698"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15699").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15698"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15692").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode77() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16250"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16250"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16251").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16250"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15692").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode78() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16252"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16252"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16253").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16252"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15692").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode79() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16254"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16254"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16255").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16254"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15692").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode80() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16256"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16256"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16257").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16256"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15692").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode81() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15720"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15720"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15721").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15720"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15716").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode82() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15722"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15722"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15723").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15722"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15716").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode83() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16272"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16272"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16273").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16272"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15716").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode84() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16274"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16274"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16275").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16274"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15716").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode85() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16276"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16276"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16277").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16276"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15716").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode86() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16278"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16278"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16279").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16278"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15716").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode87() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15708"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15708"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15709").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15708"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15704").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode88() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15710"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15710"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15711").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15710"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15704").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode89() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16261"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16261"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16262").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16261"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15704").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode90() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16263"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16263"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16264").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16263"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15704").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode91() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16265"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16265"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16266").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16265"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15704").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode92() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16267"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16267"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16268").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16267"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15704").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode93() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25572"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25572"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25573").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25572"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25565").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode94() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25574"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25574"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25575").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25574"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25565").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode95() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25576"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25576"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25577").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25576"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25565").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode96() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25578"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25578"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25579").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25578"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25565").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode97() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25580"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25580"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25581").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25580"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25565").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode98() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25582"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25582"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25583").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25582"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25565").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode99() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25610"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25610"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25611").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25610"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25603").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode100() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25612"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25612"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25613").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25612"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25603").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode101() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25614"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25614"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25615").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25614"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25603").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode102() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25616"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25617").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25616"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25603").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode103() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25618"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25618"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25619").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25618"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25603").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode104() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25620"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25621").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25603").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode105() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25591"),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25591"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25592").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25591"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25584").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode106() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25593"),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25593"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25594").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25593"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25584").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode107() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25595"),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25595"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25596").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25595"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25584").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode108() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25597"),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25597"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25598").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25597"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25584").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode109() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25599"),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25599"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25600").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25599"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25584").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode110() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25601"),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25601"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25602").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25601"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25584").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode111() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9028"),
            new QualifiedName(0, "Disable"),
            new LocalizedText("", "Disable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9028"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9028"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9028"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode112() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9027"),
            new QualifiedName(0, "Enable"),
            new LocalizedText("", "Enable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9027"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9027"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9027"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode113() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9029"),
            new QualifiedName(0, "AddComment"),
            new LocalizedText("", "AddComment"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9029"), NodeId.parse("i=46"), NodeId.parse("i=9030").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9029"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9029"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9029"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode114() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=3875"),
            new QualifiedName(0, "ConditionRefresh"),
            new LocalizedText("", "ConditionRefresh"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=3875"), NodeId.parse("i=46"), NodeId.parse("i=3876").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3875"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2787").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3875"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2788").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3875"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode115() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12912"),
            new QualifiedName(0, "ConditionRefresh2"),
            new LocalizedText("", "ConditionRefresh2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12912"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12913").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12912"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2787").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12912"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2788").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12912"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode116() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9069"),
            new QualifiedName(0, "Respond"),
            new LocalizedText("", "Respond"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9069"), NodeId.parse("i=46"), NodeId.parse("i=9070").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9069"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8927").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9069"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9069"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2830").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode117() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24312"),
            new QualifiedName(0, "Respond2"),
            new LocalizedText("", "Respond2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24312"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24313").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24312"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8927").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24312"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24312"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2830").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode118() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9111"),
            new QualifiedName(0, "Acknowledge"),
            new LocalizedText("", "Acknowledge"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9111"), NodeId.parse("i=46"), NodeId.parse("i=9112").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9111"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8944").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9111"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9111"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode119() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9113"),
            new QualifiedName(0, "Confirm"),
            new LocalizedText("", "Confirm"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9113"), NodeId.parse("i=46"), NodeId.parse("i=9114").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9113"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8961").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9113"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9113"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode120() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9213"),
            new QualifiedName(0, "TimedShelve"),
            new LocalizedText("", "TimedShelve"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9213"), NodeId.parse("i=46"), NodeId.parse("i=9214").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9213"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9213"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9213"),
            NodeId.parse("i=47"),
            NodeId.parse("i=9178").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode121() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9211"),
            new QualifiedName(0, "Unshelve"),
            new LocalizedText("", "Unshelve"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9211"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9211"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9211"),
            NodeId.parse("i=47"),
            NodeId.parse("i=9178").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode122() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=9212"),
            new QualifiedName(0, "OneShotShelve"),
            new LocalizedText("", "OneShotShelve"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=9212"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9212"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9212"),
            NodeId.parse("i=47"),
            NodeId.parse("i=9178").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode123() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16402"),
            new QualifiedName(0, "Silence"),
            new LocalizedText("", "Silence"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16402"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17242").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16402"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16402"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode124() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16403"),
            new QualifiedName(0, "Suppress"),
            new LocalizedText("", "Suppress"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16403"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16403"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16403"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode125() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24316"),
            new QualifiedName(0, "Suppress2"),
            new LocalizedText("", "Suppress2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24316"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24317").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24316"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24316"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24316"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode126() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17868"),
            new QualifiedName(0, "Unsuppress"),
            new LocalizedText("", "Unsuppress"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17868"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17868"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17868"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode127() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24318"),
            new QualifiedName(0, "Unsuppress2"),
            new LocalizedText("", "Unsuppress2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24319").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24318"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24318"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24318"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode128() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17869"),
            new QualifiedName(0, "RemoveFromService"),
            new LocalizedText("", "RemoveFromService"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17869"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17259").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17869"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17869"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode129() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24320"),
            new QualifiedName(0, "RemoveFromService2"),
            new LocalizedText("", "RemoveFromService2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24320"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24321").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24320"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17259").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24320"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24320"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode130() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17870"),
            new QualifiedName(0, "PlaceInService"),
            new LocalizedText("", "PlaceInService"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17870"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17259").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17870"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17870"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode131() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24322"),
            new QualifiedName(0, "PlaceInService2"),
            new LocalizedText("", "PlaceInService2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24322"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24323").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24322"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=17259").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24322"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24322"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode132() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18199"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18199"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=15013").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18199"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18199"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode133() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24324"),
            new QualifiedName(0, "Reset2"),
            new LocalizedText("", "Reset2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24324"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24325").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24324"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=15013").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24324"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24324"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode134() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24744"),
            new QualifiedName(0, "GetGroupMemberships"),
            new LocalizedText("", "GetGroupMemberships"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24744"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25154").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24744"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24744"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode135() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16439"),
            new QualifiedName(0, "Disable"),
            new LocalizedText("", "Disable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16439"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16439"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16439"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16406").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode136() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16440"),
            new QualifiedName(0, "Enable"),
            new LocalizedText("", "Enable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16440"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16440"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16440"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16406").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode137() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16441"),
            new QualifiedName(0, "AddComment"),
            new LocalizedText("", "AddComment"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16441"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16442").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16441"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16441"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16441"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16406").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode138() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16461"),
            new QualifiedName(0, "Acknowledge"),
            new LocalizedText("", "Acknowledge"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16461"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16462").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16461"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8944").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16461"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16461"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16406").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode139() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20034"),
            new QualifiedName(0, "Disable"),
            new LocalizedText("", "Disable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20034"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20034"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20034"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19847").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode140() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20035"),
            new QualifiedName(0, "Enable"),
            new LocalizedText("", "Enable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20035"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20035"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20035"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19847").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode141() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20036"),
            new QualifiedName(0, "AddComment"),
            new LocalizedText("", "AddComment"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20036"),
            NodeId.parse("i=46"),
            NodeId.parse("i=20037").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20036"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20036"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20036"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19847").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode142() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23493"),
            new QualifiedName(0, "Acknowledge"),
            new LocalizedText("", "Acknowledge"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23493"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23561").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23493"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8944").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23493"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23493"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19847").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode143() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2949"),
            new QualifiedName(0, "TimedShelve"),
            new LocalizedText("", "TimedShelve"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2949"), NodeId.parse("i=46"), NodeId.parse("i=2991").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2949"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2935").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2949"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2945").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2949"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2949"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2949"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2929").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode144() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24756"),
            new QualifiedName(0, "TimedShelve2"),
            new LocalizedText("", "TimedShelve2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24756"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24757").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24756"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2935").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=24756"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2945").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=24756"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24756"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24756"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2929").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode145() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2947"),
            new QualifiedName(0, "Unshelve"),
            new LocalizedText("", "Unshelve"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2947"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2940").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2947"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2943").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2947"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2947"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2947"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2929").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode146() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24758"),
            new QualifiedName(0, "Unshelve2"),
            new LocalizedText("", "Unshelve2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24758"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24759").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24758"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2940").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=24758"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2943").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=24758"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24758"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24758"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2929").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode147() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2948"),
            new QualifiedName(0, "OneShotShelve"),
            new LocalizedText("", "OneShotShelve"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2948"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2936").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2948"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2942").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2948"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2948"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2948"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2929").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode148() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24760"),
            new QualifiedName(0, "OneShotShelve2"),
            new LocalizedText("", "OneShotShelve2"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24760"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24761").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24760"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2936").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=24760"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2942").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=24760"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=11093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24760"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24760"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2929").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode149() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18666"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18666"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2127").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18666"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18666"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17279").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode150() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2426"),
            new QualifiedName(0, "Start"),
            new LocalizedText("", "Start"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2426"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2410").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2426"),
            NodeId.parse("i=37"),
            NodeId.parse("i=11508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2426"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2391").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode151() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2427"),
            new QualifiedName(0, "Suspend"),
            new LocalizedText("", "Suspend"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2427"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2416").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2427"),
            NodeId.parse("i=37"),
            NodeId.parse("i=11508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2427"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2391").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode152() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2428"),
            new QualifiedName(0, "Resume"),
            new LocalizedText("", "Resume"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2428"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2418").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2428"),
            NodeId.parse("i=37"),
            NodeId.parse("i=11508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2428"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2391").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode153() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2429"),
            new QualifiedName(0, "Halt"),
            new LocalizedText("", "Halt"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2429"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2412").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2429"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2420").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2429"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2424").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2429"),
            NodeId.parse("i=37"),
            NodeId.parse("i=11508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2429"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2391").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode154() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=2430"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2430"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2408").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2430"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2420").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2430"),
            NodeId.parse("i=53"),
            NodeId.parse("i=2422").expanded(),
            false));
    node.addReference(
        new Reference(
            NodeId.parse("i=2430"),
            NodeId.parse("i=37"),
            NodeId.parse("i=11508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2430"),
            NodeId.parse("i=47"),
            NodeId.parse("i=2391").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode155() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12543"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12543"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12544").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12543"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12545").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12543"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12543"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12522").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode156() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12546"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12546"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12705").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12546"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12547").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12546"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12546"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12522").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode157() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12548"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12548"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12549").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12548"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12548"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12522").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode158() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12550"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12551").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12550"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12550"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12522").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode159() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13605"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13605"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13606").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13605"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13607").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13605"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13605"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode160() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13608"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13608"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13609").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13608"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13608"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode161() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13610"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13610"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13611").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13610"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13612").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13610"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13610"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode162() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13613"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13613"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13614").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13613"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13613"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode163() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13615"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13615"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13616").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13615"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13617").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13615"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13615"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode164() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13618"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13618"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13619").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13618"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13618"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode165() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13621"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13621"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13622").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13621"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13623").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13621"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13621"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode166() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13624"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13624"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13625").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13624"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13626").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13624"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13624"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode167() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13627"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13627"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13628").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13627"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13627"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode168() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13629"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13629"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13630").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13629"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13629"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode169() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19483"),
            new QualifiedName(0, "Disable"),
            new LocalizedText("", "Disable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19483"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19483"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19483"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19450").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode170() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19484"),
            new QualifiedName(0, "Enable"),
            new LocalizedText("", "Enable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19484"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19484"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19484"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19450").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode171() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19485"),
            new QualifiedName(0, "AddComment"),
            new LocalizedText("", "AddComment"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19485"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19486").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19485"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19485"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19485"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19450").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode172() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19505"),
            new QualifiedName(0, "Acknowledge"),
            new LocalizedText("", "Acknowledge"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19505"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19506").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19505"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8944").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19505"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19505"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19450").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode173() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20176"),
            new QualifiedName(0, "Disable"),
            new LocalizedText("", "Disable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20176"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20176"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20176"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20143").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode174() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20177"),
            new QualifiedName(0, "Enable"),
            new LocalizedText("", "Enable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20177"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20177"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20177"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20143").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode175() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20178"),
            new QualifiedName(0, "AddComment"),
            new LocalizedText("", "AddComment"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20178"),
            NodeId.parse("i=46"),
            NodeId.parse("i=20179").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20178"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=2829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20178"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20178"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20143").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode176() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=20198"),
            new QualifiedName(0, "Acknowledge"),
            new LocalizedText("", "Acknowledge"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=20198"),
            NodeId.parse("i=46"),
            NodeId.parse("i=20199").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20198"),
            NodeId.parse("i=3065"),
            NodeId.parse("i=8944").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20198"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20198"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20143").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode177() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23526"),
            new QualifiedName(0, "GetRejectedList"),
            new LocalizedText("", "GetRejectedList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23526"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23527").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23526"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23526"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12555").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode178() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13821"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13821"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13822").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13821"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13823").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13821"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13821"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode179() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13824"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13825").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13824"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13824"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode180() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13826"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13826"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13827").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13826"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13828").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13826"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13826"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode181() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13829"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13829"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13830").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13829"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13829"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode182() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13831"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13831"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13832").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13831"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13833").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13831"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13831"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode183() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13834"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13834"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13835").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13834"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13834"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode184() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13837"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13837"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13838").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13837"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13839").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13837"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13837"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode185() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13840"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13840"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13841").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13840"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13842").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13840"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13840"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode186() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13843"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13843"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13844").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13843"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13843"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode187() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13845"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13845"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13846").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13845"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13845"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13815").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode188() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13855"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13855"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13856").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13855"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13857").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13855"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13855"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode189() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13858"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13858"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13859").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13858"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13858"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode190() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13860"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13860"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13861").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13860"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13862").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13860"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13860"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode191() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13863"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13863"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13864").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13863"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13863"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode192() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13865"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13865"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13866").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13865"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13867").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13865"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13865"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode193() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13868"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13868"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13869").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13868"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13868"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode194() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13871"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13871"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13872").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13871"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13873").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13871"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13871"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode195() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13874"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13874"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13875").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13874"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13876").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13874"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13874"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode196() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13877"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13877"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13878").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13877"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13877"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode197() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13879"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13879"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13880").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13879"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13879"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13849").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode198() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13889"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13889"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13890").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13889"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13891").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13889"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13889"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode199() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13892"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13892"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13893").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13892"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13892"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode200() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13894"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13894"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13895").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13894"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13896").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13894"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13894"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode201() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13897"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13897"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13898").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13897"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13897"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode202() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13899"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13899"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13900").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13899"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13901").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13899"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13899"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode203() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13902"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13902"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13903").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13902"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13902"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode204() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13905"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13905"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13906").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13905"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13907").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13905"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13905"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode205() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13908"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13908"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13909").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13908"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13910").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13908"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13908"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode206() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13911"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13911"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13912").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13911"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13911"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode207() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13913"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13913"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13914").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13913"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13913"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13883").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode208() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13923"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13923"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13924").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13923"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13925").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13923"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13923"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode209() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13926"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13926"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13927").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13926"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13926"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode210() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13928"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13928"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13929").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13928"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13930").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13928"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13928"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode211() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13931"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13931"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13932").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13931"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13931"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode212() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13933"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13933"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13934").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13933"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13935").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13933"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13933"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode213() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13936"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13936"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13937").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13936"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13936"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode214() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13939"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13939"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13940").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13939"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13941").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13939"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13939"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode215() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13942"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13942"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13943").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13942"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13944").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13942"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13942"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode216() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13945"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13945"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13946").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13945"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13945"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode217() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13947"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13947"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13948").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13947"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13947"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13917").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode218() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15508"),
            new QualifiedName(0, "ConfirmUpdate"),
            new LocalizedText("", "ConfirmUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15508"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15511").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15508"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15508"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15437").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode219() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15505"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15505"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15506").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15505"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15507").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15505"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15505"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15437").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode220() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18533"),
            new QualifiedName(0, "UpdateCertificate"),
            new LocalizedText("", "UpdateCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18533"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18534").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18533"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18535").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18533"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18533"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16663").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode221() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18539"),
            new QualifiedName(0, "ApplyChanges"),
            new LocalizedText("", "ApplyChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18539"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18539"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16663").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode222() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18541"),
            new QualifiedName(0, "CreateSigningRequest"),
            new LocalizedText("", "CreateSigningRequest"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18541"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18542").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18541"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18543").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18541"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18541"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16663").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode223() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18544"),
            new QualifiedName(0, "GetRejectedList"),
            new LocalizedText("", "GetRejectedList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18544"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18545").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18544"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18544"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16663").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode224() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16717"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16717"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16722").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16717"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16723").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16717"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16717"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode225() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16724"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16724"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16725").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16724"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16724"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode226() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16726"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16726"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16727").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16726"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16728").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16726"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16726"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode227() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16729"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16729"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16730").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16729"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16729"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode228() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16732"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16732"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16733").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16732"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16734").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16732"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16732"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode229() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16735"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16735"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16736").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16735"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16735"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode230() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16741"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16741"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16742").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16741"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16743").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16741"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16741"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode231() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16744"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16744"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16745").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16744"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16746").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16744"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16744"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode232() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16747"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16747"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16748").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16747"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16747"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode233() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16749"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16749"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16750").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16749"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16749"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16709").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode234() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12616"),
            new QualifiedName(0, "UpdateCertificate"),
            new LocalizedText("", "UpdateCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12617").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12618").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12616"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12616"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode235() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19337"),
            new QualifiedName(0, "CreateSelfSignedCertificate"),
            new LocalizedText("", "CreateSelfSignedCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19338").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19339").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19337"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19337"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode236() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19340"),
            new QualifiedName(0, "DeleteCertificate"),
            new LocalizedText("", "DeleteCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19340"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19341").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19340"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19340"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode237() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=32296"),
            new QualifiedName(0, "GetCertificates"),
            new LocalizedText("", "GetCertificates"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=32296"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32297").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32296"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32298").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32296"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32296"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode238() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12734"),
            new QualifiedName(0, "ApplyChanges"),
            new LocalizedText("", "ApplyChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12734"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12734"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode239() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25698"),
            new QualifiedName(0, "CancelChanges"),
            new LocalizedText("", "CancelChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25698"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25698"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode240() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12731"),
            new QualifiedName(0, "CreateSigningRequest"),
            new LocalizedText("", "CreateSigningRequest"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12732").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12733").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12731"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12731"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode241() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12775"),
            new QualifiedName(0, "GetRejectedList"),
            new LocalizedText("", "GetRejectedList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12775"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12776").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12775"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12775"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode242() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25699"),
            new QualifiedName(0, "ResetToServerDefaults"),
            new LocalizedText("", "ResetToServerDefaults"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25699"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25699"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode243() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13958"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13958"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13959").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13958"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13960").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13958"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13958"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode244() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13961"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13961"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13962").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13961"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13961"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode245() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13963"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13963"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13964").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13963"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13965").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13963"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13963"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode246() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13966"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13966"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13967").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13966"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13966"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode247() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13968"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13968"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13969").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13968"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13970").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13968"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13968"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode248() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13971"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13971"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13972").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13971"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13971"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode249() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13974"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13974"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13975").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13974"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13976").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13974"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13974"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode250() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13977"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13977"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13978").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13977"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13979").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13977"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13977"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode251() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13980"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13980"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13981").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13980"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13980"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode252() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13982"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13982"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13983").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13982"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13982"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13952").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode253() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15572"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15572"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15573").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15572"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15574").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15572"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15572"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode254() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15575"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15790").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15575"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode255() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15791"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15791"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15792").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15791"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15804").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15791"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15791"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode256() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15805"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15805"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15806").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15805"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15805"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode257() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15807"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15807"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15808").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15807"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15809").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15807"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15807"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode258() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15810"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15810"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15811").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15810"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15810"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode259() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15863"),
            new QualifiedName(0, "ConfirmUpdate"),
            new LocalizedText("", "ConfirmUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15863"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15864").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15863"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15863"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode260() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15849"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15849"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15850").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15849"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15851").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15849"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15849"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode261() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=13737"),
            new QualifiedName(0, "UpdateCertificate"),
            new LocalizedText("", "UpdateCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=13737"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13738").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13737"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13739").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13737"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode262() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12740"),
            new QualifiedName(0, "ApplyChanges"),
            new LocalizedText("", "ApplyChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(5)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12740"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode263() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25708"),
            new QualifiedName(0, "CancelChanges"),
            new LocalizedText("", "CancelChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(5)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25708"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode264() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12737"),
            new QualifiedName(0, "CreateSigningRequest"),
            new LocalizedText("", "CreateSigningRequest"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12737"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12738").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12737"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12739").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12737"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode265() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12777"),
            new QualifiedName(0, "GetRejectedList"),
            new LocalizedText("", "GetRejectedList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12777"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12778").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12777"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode266() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25709"),
            new QualifiedName(0, "ResetToServerDefaults"),
            new LocalizedText("", "ResetToServerDefaults"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25709"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode267() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12647"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12647"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12648").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12647"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12649").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12647"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode268() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12650"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12650"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12651").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12650"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode269() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12652"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12652"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12653").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12652"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12654").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12652"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode270() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12655"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12655"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12656").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12655"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode271() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12657"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12657"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12658").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12657"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12659").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12657"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode272() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12660"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12660"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12661").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12660"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode273() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12663"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12663"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12664").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12663"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12665").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12663"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode274() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12666"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12666"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14160").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12666"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12667").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12666"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode275() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12668"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12668"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12669").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12668"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode276() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=12670"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12670"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12671").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12670"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12642").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode277() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14095"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14095"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14096").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14095"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14097").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14095"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode278() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14098"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14098"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14099").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14098"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode279() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14100"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14100"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14101").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14100"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14102").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14100"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode280() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14103"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14103"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14104").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14103"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode281() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14105"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14106").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14107").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14105"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode282() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14108"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14108"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14109").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14108"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode283() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14111"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14112").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14113").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14111"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode284() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14114"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14114"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14115").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14114"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14116").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14114"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode285() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14117"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14117"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14118").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14117"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode286() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14119"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14119"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14120").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14119"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14089").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode287() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14129"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14129"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14130").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14129"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14131").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14129"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode288() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14132"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14132"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14133").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14132"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode289() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14134"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14134"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14135").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14134"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14136").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14134"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode290() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14137"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14137"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14138").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14137"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode291() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14139"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14139"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14140").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14139"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14141").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14139"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode292() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14142"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14142"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14143").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14142"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode293() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14145"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14145"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14146").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14145"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14147").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14145"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode294() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14148"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14148"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14149").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14148"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14150").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14148"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode295() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14151"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14151"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14152").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14151"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode296() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14153"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14153"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14154").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14153"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14123").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode297() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16013"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16020").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16059").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16013"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode298() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16060"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16060"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16061").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16060"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode299() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16074"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16074"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16075").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16074"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16076").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16074"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode300() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16101"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16101"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16102").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16101"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode301() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16103"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16103"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16122").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16103"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16123").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16103"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode302() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16124"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16124"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16160").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16124"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode303() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16320"),
            new QualifiedName(0, "ConfirmUpdate"),
            new LocalizedText("", "ConfirmUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16320"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16321").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16320"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode304() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16317"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16317"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16318").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16317"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16319").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16317"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15892").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode305() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17522"),
            new QualifiedName(0, "CreateCredential"),
            new LocalizedText("", "CreateCredential"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17522"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17523").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17522"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17524").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17522"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17522"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17496").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode306() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17528"),
            new QualifiedName(0, "CreateCredential"),
            new LocalizedText("", "CreateCredential"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17528"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17529").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17528"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17530").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17528"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18155").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode307() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17534"),
            new QualifiedName(0, "GetEncryptingKey"),
            new LocalizedText("", "GetEncryptingKey"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17534"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17535").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17534"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17536").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17534"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17534"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18001").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode308() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18006"),
            new QualifiedName(0, "UpdateCredential"),
            new LocalizedText("", "UpdateCredential"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18006"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18007").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18006"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18006"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18001").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode309() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18008"),
            new QualifiedName(0, "DeleteCredential"),
            new LocalizedText("", "DeleteCredential"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18008"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18008"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18001").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode310() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15907"),
            new QualifiedName(0, "GetSecurityKeys"),
            new LocalizedText("", "GetSecurityKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15907"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15908").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15907"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15909").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15907"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15907"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15906").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode311() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15910"),
            new QualifiedName(0, "GetSecurityGroup"),
            new LocalizedText("", "GetSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15910"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15911").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15910"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15912").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15910"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15910"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15906").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode312() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15914"),
            new QualifiedName(0, "AddSecurityGroup"),
            new LocalizedText("", "AddSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15914"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15915").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15914"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15916").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15914"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15914"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15913").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode313() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15917"),
            new QualifiedName(0, "RemoveSecurityGroup"),
            new LocalizedText("", "RemoveSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15917"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15918").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15917"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15917"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15913").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode314() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25278"),
            new QualifiedName(0, "AddPushTarget"),
            new LocalizedText("", "AddPushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25278"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25279").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25278"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25280").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25278"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25278"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25277").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode315() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25281"),
            new QualifiedName(0, "RemovePushTarget"),
            new LocalizedText("", "RemovePushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25281"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25282").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25281"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25281"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25277").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode316() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15454"),
            new QualifiedName(0, "AddSecurityGroup"),
            new LocalizedText("", "AddSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15454"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15455").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15454"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15456").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15454"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15454"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15453").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode317() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15457"),
            new QualifiedName(0, "RemoveSecurityGroup"),
            new LocalizedText("", "RemoveSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15457"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15458").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15457"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15457"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15453").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode318() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25293"),
            new QualifiedName(0, "AddSecurityGroupFolder"),
            new LocalizedText("", "AddSecurityGroupFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25293"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25294").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25293"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25295").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25293"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25293"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15453").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode319() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25296"),
            new QualifiedName(0, "RemoveSecurityGroupFolder"),
            new LocalizedText("", "RemoveSecurityGroupFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25296"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25297").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25296"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25296"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15453").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode320() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15461"),
            new QualifiedName(0, "AddSecurityGroup"),
            new LocalizedText("", "AddSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15461"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15462").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15461"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15463").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15461"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15461"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15452").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode321() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15464"),
            new QualifiedName(0, "RemoveSecurityGroup"),
            new LocalizedText("", "RemoveSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15464"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15465").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15464"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15464"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15452").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode322() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25312"),
            new QualifiedName(0, "AddSecurityGroupFolder"),
            new LocalizedText("", "AddSecurityGroupFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25312"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25313").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25312"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25314").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25312"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25312"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15452").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode323() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25315"),
            new QualifiedName(0, "RemoveSecurityGroupFolder"),
            new LocalizedText("", "RemoveSecurityGroupFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25315"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25316").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25315"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25315"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15452").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode324() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25624"),
            new QualifiedName(0, "InvalidateKeys"),
            new LocalizedText("", "InvalidateKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25624"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25624"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15471").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode325() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25625"),
            new QualifiedName(0, "ForceKeyRotation"),
            new LocalizedText("", "ForceKeyRotation"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25625"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25625"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15471").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode326() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25641"),
            new QualifiedName(0, "ConnectSecurityGroups"),
            new LocalizedText("", "ConnectSecurityGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25641"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25642").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25641"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25643").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25641"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25641"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25337").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode327() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25644"),
            new QualifiedName(0, "DisconnectSecurityGroups"),
            new LocalizedText("", "DisconnectSecurityGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25644"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25645").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25644"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25646").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25644"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25644"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25337").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode328() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25647"),
            new QualifiedName(0, "TriggerKeyUpdate"),
            new LocalizedText("", "TriggerKeyUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25647"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25647"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25337").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode329() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25348"),
            new QualifiedName(0, "AddPushTarget"),
            new LocalizedText("", "AddPushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25348"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25349").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25348"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25350").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25348"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25348"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25347").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode330() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25351"),
            new QualifiedName(0, "RemovePushTarget"),
            new LocalizedText("", "RemovePushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25351"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25352").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25351"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25351"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25347").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode331() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25353"),
            new QualifiedName(0, "AddPushTargetFolder"),
            new LocalizedText("", "AddPushTargetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25353"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25354").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25353"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25355").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25353"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25353"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25347").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode332() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25356"),
            new QualifiedName(0, "RemovePushTargetFolder"),
            new LocalizedText("", "RemovePushTargetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25356"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25357").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25356"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25356"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25347").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode333() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25655"),
            new QualifiedName(0, "ConnectSecurityGroups"),
            new LocalizedText("", "ConnectSecurityGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25655"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25656").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25655"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25657").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25655"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25655"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25358").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode334() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25658"),
            new QualifiedName(0, "DisconnectSecurityGroups"),
            new LocalizedText("", "DisconnectSecurityGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25658"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25659").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25658"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25660").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25658"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25658"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25358").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode335() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25661"),
            new QualifiedName(0, "TriggerKeyUpdate"),
            new LocalizedText("", "TriggerKeyUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25661"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25661"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25358").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode336() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25366"),
            new QualifiedName(0, "AddPushTarget"),
            new LocalizedText("", "AddPushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25366"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25367").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25366"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25368").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25366"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25366"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25346").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode337() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25369"),
            new QualifiedName(0, "RemovePushTarget"),
            new LocalizedText("", "RemovePushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25369"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25370").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25369"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25369"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25346").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode338() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25371"),
            new QualifiedName(0, "AddPushTargetFolder"),
            new LocalizedText("", "AddPushTargetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25371"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25372").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25371"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25373").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25371"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25371"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25346").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode339() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25374"),
            new QualifiedName(0, "RemovePushTargetFolder"),
            new LocalizedText("", "RemovePushTargetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25374"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25375").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25374"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25374"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25346").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode340() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17296"),
            new QualifiedName(0, "SetSecurityKeys"),
            new LocalizedText("", "SetSecurityKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17296"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17297").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17296"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17296"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14416").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode341() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16598"),
            new QualifiedName(0, "AddConnection"),
            new LocalizedText("", "AddConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16598"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16599").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16598"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16600").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16598"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16598"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14416").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode342() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14432"),
            new QualifiedName(0, "RemoveConnection"),
            new LocalizedText("", "RemoveConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14432"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14433").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14432"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14432"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14416").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode343() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25411"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25411"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25412").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25411"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25413").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25411"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25411"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode344() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25414"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25414"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25415").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25414"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25414"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode345() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25416"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25417").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25418").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25416"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode346() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25419"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25419"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25420").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25419"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25419"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode347() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25421"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25421"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25422").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25421"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25423").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25421"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25421"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode348() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25424"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25424"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25425").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25424"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25424"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode349() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25426"),
            new QualifiedName(0, "ReserveIds"),
            new LocalizedText("", "ReserveIds"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25426"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25427").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25426"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25428").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25426"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25426"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode350() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25429"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25429"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25430").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25429"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25431").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25429"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25429"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode351() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=18727"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18727"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18727"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18715").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode352() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15215"),
            new QualifiedName(0, "GetSecurityKeys"),
            new LocalizedText("", "GetSecurityKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15215"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15216").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15215"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15217").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15215"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode353() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15440"),
            new QualifiedName(0, "GetSecurityGroup"),
            new LocalizedText("", "GetSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15440"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15441").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15440"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15442").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15440"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode354() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15444"),
            new QualifiedName(0, "AddSecurityGroup"),
            new LocalizedText("", "AddSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15444"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15445").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15444"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15446").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15444"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode355() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15447"),
            new QualifiedName(0, "RemoveSecurityGroup"),
            new LocalizedText("", "RemoveSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15447"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15448").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15447"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode356() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25441"),
            new QualifiedName(0, "AddPushTarget"),
            new LocalizedText("", "AddPushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25441"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25442").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25441"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25443").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25441"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25440").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode357() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25444"),
            new QualifiedName(0, "RemovePushTarget"),
            new LocalizedText("", "RemovePushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25444"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25445").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25444"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25440").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode358() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17364"),
            new QualifiedName(0, "SetSecurityKeys"),
            new LocalizedText("", "SetSecurityKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  NodeId.parse("i=25584"), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  NodeId.parse("i=25565"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17364"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17365").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17364"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode359() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17366"),
            new QualifiedName(0, "AddConnection"),
            new LocalizedText("", "AddConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17366"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17367").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17366"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17368").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17366"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode360() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17369"),
            new QualifiedName(0, "RemoveConnection"),
            new LocalizedText("", "RemoveConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17369"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17370").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17369"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14443").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode361() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25459"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25459"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25460").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25459"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25461").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25459"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode362() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25462"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25462"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25463").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25462"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode363() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25464"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25464"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25465").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25464"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25466").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25464"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode364() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25467"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25467"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25468").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25467"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode365() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25469"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25469"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25470").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25469"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25471").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25469"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode366() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25472"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25472"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25473").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25472"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode367() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25474"),
            new QualifiedName(0, "ReserveIds"),
            new LocalizedText("", "ReserveIds"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25474"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25475").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25474"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25476").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25474"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode368() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25477"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25477"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25478").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25477"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25479").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25451").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode369() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17421"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15716"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17421"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17409").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode370() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25505"),
            new QualifiedName(0, "ReserveIds"),
            new LocalizedText("", "ReserveIds"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25505"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25506").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25505"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25507").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25505"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25505"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25482").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode371() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25508"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25508"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25509").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25508"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25510").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25508"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25508"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25482").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode372() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15482"),
            new QualifiedName(0, "AddExtensionField"),
            new LocalizedText("", "AddExtensionField"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15482"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15483").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15482"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15484").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15482"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15482"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15481").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode373() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15485"),
            new QualifiedName(0, "RemoveExtensionField"),
            new LocalizedText("", "RemoveExtensionField"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15485"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15486").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15485"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15485"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15481").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode374() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15491"),
            new QualifiedName(0, "AddExtensionField"),
            new LocalizedText("", "AddExtensionField"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15491"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15492").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15491"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15493").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15491"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15491"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15489").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode375() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15494"),
            new QualifiedName(0, "RemoveExtensionField"),
            new LocalizedText("", "RemoveExtensionField"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15494"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15495").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15494"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15494"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15489").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode376() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14555"),
            new QualifiedName(0, "AddVariables"),
            new LocalizedText("", "AddVariables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14555"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14556").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14555"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14557").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14555"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14555"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14534").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode377() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14558"),
            new QualifiedName(0, "RemoveVariables"),
            new LocalizedText("", "RemoveVariables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14558"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14559").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14558"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14560").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14558"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14558"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14534").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode378() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15052"),
            new QualifiedName(0, "ModifyFieldSelection"),
            new LocalizedText("", "ModifyFieldSelection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15052"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15053").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15052"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15517").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15052"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15052"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14572").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode379() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14479"),
            new QualifiedName(0, "AddPublishedDataItems"),
            new LocalizedText("", "AddPublishedDataItems"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14479"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14480").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14479"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14481").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14479"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14479"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode380() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14482"),
            new QualifiedName(0, "AddPublishedEvents"),
            new LocalizedText("", "AddPublishedEvents"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14482"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14483").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14482"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14484").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14482"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14482"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode381() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16842"),
            new QualifiedName(0, "AddPublishedDataItemsTemplate"),
            new LocalizedText("", "AddPublishedDataItemsTemplate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16842"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16843").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16842"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16853").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16842"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16842"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode382() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16881"),
            new QualifiedName(0, "AddPublishedEventsTemplate"),
            new LocalizedText("", "AddPublishedEventsTemplate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16881"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16882").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16881"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16883").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16881"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16881"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode383() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14485"),
            new QualifiedName(0, "RemovePublishedDataSet"),
            new LocalizedText("", "RemovePublishedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14485"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14486").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14485"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14485"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode384() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16884"),
            new QualifiedName(0, "AddDataSetFolder"),
            new LocalizedText("", "AddDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16884"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16894").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16884"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16922").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16884"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16884"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode385() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16923"),
            new QualifiedName(0, "RemoveDataSetFolder"),
            new LocalizedText("", "RemoveDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16923"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16924").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16923"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16923"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14478").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode386() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14493"),
            new QualifiedName(0, "AddPublishedDataItems"),
            new LocalizedText("", "AddPublishedDataItems"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14493"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14494").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14493"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14495").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14493"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14493"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode387() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14496"),
            new QualifiedName(0, "AddPublishedEvents"),
            new LocalizedText("", "AddPublishedEvents"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14496"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14497").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14496"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14498").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14496"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14496"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode388() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16935"),
            new QualifiedName(0, "AddPublishedDataItemsTemplate"),
            new LocalizedText("", "AddPublishedDataItemsTemplate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16935"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16958").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16935"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16959").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16935"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16935"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode389() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16960"),
            new QualifiedName(0, "AddPublishedEventsTemplate"),
            new LocalizedText("", "AddPublishedEventsTemplate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16960"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16961").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16960"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16971").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16960"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16960"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode390() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14499"),
            new QualifiedName(0, "RemovePublishedDataSet"),
            new LocalizedText("", "RemovePublishedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14499"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14500").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14499"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14499"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode391() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16994"),
            new QualifiedName(0, "AddDataSetFolder"),
            new LocalizedText("", "AddDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16994"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16995").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16994"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16996").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16994"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16994"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode392() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=16997"),
            new QualifiedName(0, "RemoveDataSetFolder"),
            new LocalizedText("", "RemoveDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=16997"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17007").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16997"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16997"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14477").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode393() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19253"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19253"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19253"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19241").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode394() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17427"),
            new QualifiedName(0, "AddWriterGroup"),
            new LocalizedText("", "AddWriterGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17427"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17428").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17427"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17456").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17427"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17427"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14209").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode395() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17465"),
            new QualifiedName(0, "AddReaderGroup"),
            new LocalizedText("", "AddReaderGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17465"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17507").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17465"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17465"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17465"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14209").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode396() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14225"),
            new QualifiedName(0, "RemoveGroup"),
            new LocalizedText("", "RemoveGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14225"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14226").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14225"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14225"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14209").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode397() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17824"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17824"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17824"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17812").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode398() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17969"),
            new QualifiedName(0, "AddDataSetWriter"),
            new LocalizedText("", "AddDataSetWriter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17969"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17976").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17969"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17987").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17969"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17969"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17725").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode399() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17992"),
            new QualifiedName(0, "RemoveDataSetWriter"),
            new LocalizedText("", "RemoveDataSetWriter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17992"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17993").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17992"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17992"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17725").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode400() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=21027"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=21027"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21027"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21015").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode401() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=21082"),
            new QualifiedName(0, "AddDataSetReader"),
            new LocalizedText("", "AddDataSetReader"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=21082"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21083").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21082"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21084").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21082"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21082"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17999").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode402() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=21085"),
            new QualifiedName(0, "RemoveDataSetReader"),
            new LocalizedText("", "RemoveDataSetReader"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=21085"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21086").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21085"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21085"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17999").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode403() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19562"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19562"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19562"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19550").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode404() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19621"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19621"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19621"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19609").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode405() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17386"),
            new QualifiedName(0, "CreateTargetVariables"),
            new LocalizedText("", "CreateTargetVariables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17386"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17387").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17386"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17388").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17386"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17386"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15306").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode406() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=17389"),
            new QualifiedName(0, "CreateDataSetMirror"),
            new LocalizedText("", "CreateDataSetMirror"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17389"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17390").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17389"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17391").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17389"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17389"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15306").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode407() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15115"),
            new QualifiedName(0, "AddTargetVariables"),
            new LocalizedText("", "AddTargetVariables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15115"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15116").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15115"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15117").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15115"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15115"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15111").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode408() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=15118"),
            new QualifiedName(0, "RemoveTargetVariables"),
            new LocalizedText("", "RemoveTargetVariables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15118"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15119").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15118"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15120").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15118"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15118"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15111").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode409() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23797"),
            new QualifiedName(0, "AddSubscribedDataSet"),
            new LocalizedText("", "AddSubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23797"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23798").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23797"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23799").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23797"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23797"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23796").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode410() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23800"),
            new QualifiedName(0, "RemoveSubscribedDataSet"),
            new LocalizedText("", "RemoveSubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23800"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23801").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23800"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23800"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23796").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode411() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23802"),
            new QualifiedName(0, "AddDataSetFolder"),
            new LocalizedText("", "AddDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23802"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23803").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23802"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23804").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23802"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23802"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23796").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode412() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23805"),
            new QualifiedName(0, "RemoveDataSetFolder"),
            new LocalizedText("", "RemoveDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23805"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23806").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23805"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23805"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23796").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode413() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23811"),
            new QualifiedName(0, "AddSubscribedDataSet"),
            new LocalizedText("", "AddSubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23811"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23812").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23811"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23813").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23811"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23811"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23795").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode414() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23814"),
            new QualifiedName(0, "RemoveSubscribedDataSet"),
            new LocalizedText("", "RemoveSubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23814"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23815").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23814"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23814"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23795").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode415() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23816"),
            new QualifiedName(0, "AddDataSetFolder"),
            new LocalizedText("", "AddDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23816"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23817").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23816"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23818").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23816"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23816"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23795").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode416() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23819"),
            new QualifiedName(0, "RemoveDataSetFolder"),
            new LocalizedText("", "RemoveDataSetFolder"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23819"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23820").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23819"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23819"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23795").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode417() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14645"),
            new QualifiedName(0, "Enable"),
            new LocalizedText("", "Enable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14645"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14645"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14643").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode418() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=14646"),
            new QualifiedName(0, "Disable"),
            new LocalizedText("", "Disable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=14646"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14646"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14643").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode419() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19689"),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19689"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19689"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode420() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23459"),
            new QualifiedName(0, "FindAlias"),
            new LocalizedText("", "FindAlias"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23459"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23460").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23459"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23461").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23459"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23459"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23458").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode421() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23930"),
            new QualifiedName(0, "FindAliasVerbose"),
            new LocalizedText("", "FindAliasVerbose"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23930"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23931").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23930"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23935").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23930"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23930"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23458").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode422() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23936"),
            new QualifiedName(0, "AddAliasesToCategory"),
            new LocalizedText("", "AddAliasesToCategory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23936"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23937").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23936"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23959").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23936"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23936"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23458").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode423() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23960"),
            new QualifiedName(0, "DeleteAliasesFromCategory"),
            new LocalizedText("", "DeleteAliasesFromCategory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23960"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23961").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23960"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23962").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23960"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23960"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23458").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode424() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23462"),
            new QualifiedName(0, "FindAlias"),
            new LocalizedText("", "FindAlias"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23462"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23463").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23462"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23464").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23462"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23462"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23456").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode425() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23963"),
            new QualifiedName(0, "FindAliasVerbose"),
            new LocalizedText("", "FindAliasVerbose"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23963"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23964").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23963"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23971").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23963"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23963"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23456").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode426() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23972"),
            new QualifiedName(0, "AddAliasesToCategory"),
            new LocalizedText("", "AddAliasesToCategory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23972"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23973").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23972"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23974").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23972"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23972"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23456").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode427() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23975"),
            new QualifiedName(0, "DeleteAliasesFromCategory"),
            new LocalizedText("", "DeleteAliasesFromCategory"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23975"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23976").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23975"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23986").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23975"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23975"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23456").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode428() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23476"),
            new QualifiedName(0, "FindAlias"),
            new LocalizedText("", "FindAlias"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23476"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23477").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23476"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23478").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23476"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23470").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode429() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23485"),
            new QualifiedName(0, "FindAlias"),
            new LocalizedText("", "FindAlias"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23485"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23486").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23485"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23487").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23485"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23479").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode430() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=23494"),
            new QualifiedName(0, "FindAlias"),
            new LocalizedText("", "FindAlias"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23494"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23495").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23494"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23496").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23494"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23488").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode431() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24269"),
            new QualifiedName(0, "AddUser"),
            new LocalizedText("", "AddUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24269"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24270").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24269"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24269"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24264").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode432() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24271"),
            new QualifiedName(0, "ModifyUser"),
            new LocalizedText("", "ModifyUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24271"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24272").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24271"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24271"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24264").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode433() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24273"),
            new QualifiedName(0, "RemoveUser"),
            new LocalizedText("", "RemoveUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24273"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24274").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24273"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24273"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24264").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode434() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24275"),
            new QualifiedName(0, "ChangePassword"),
            new LocalizedText("", "ChangePassword"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24275"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24276").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24275"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24275"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24264").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode435() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24304"),
            new QualifiedName(0, "AddUser"),
            new LocalizedText("", "AddUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24304"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24305").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24304"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24290").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode436() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24306"),
            new QualifiedName(0, "ModifyUser"),
            new LocalizedText("", "ModifyUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24307").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24290").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode437() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24308"),
            new QualifiedName(0, "RemoveUser"),
            new LocalizedText("", "RemoveUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24308"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24309").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24308"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24290").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode438() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24310"),
            new QualifiedName(0, "ChangePassword"),
            new LocalizedText("", "ChangePassword"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24310"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24311").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24310"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24290").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode439() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26873"),
            new QualifiedName(0, "RequestTickets"),
            new LocalizedText("", "RequestTickets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26873"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26874").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26873"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26873"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26871").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode440() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26875"),
            new QualifiedName(0, "SetRegistrarEndpoints"),
            new LocalizedText("", "SetRegistrarEndpoints"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26875"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26876").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26875"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26875"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26871").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode441() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=28005"),
            new QualifiedName(0, "UpdateCertificate"),
            new LocalizedText("", "UpdateCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=28005"),
            NodeId.parse("i=46"),
            NodeId.parse("i=28006").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28005"),
            NodeId.parse("i=46"),
            NodeId.parse("i=28007").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28005"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28005"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26878").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode442() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=28008"),
            new QualifiedName(0, "ApplyChanges"),
            new LocalizedText("", "ApplyChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=28008"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28008"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26878").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode443() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=28010"),
            new QualifiedName(0, "CreateSigningRequest"),
            new LocalizedText("", "CreateSigningRequest"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=28010"),
            NodeId.parse("i=46"),
            NodeId.parse("i=28011").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28010"),
            NodeId.parse("i=46"),
            NodeId.parse("i=28012").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28010"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28010"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26878").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode444() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=28013"),
            new QualifiedName(0, "GetRejectedList"),
            new LocalizedText("", "GetRejectedList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=28013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=28014").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28013"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=28013"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26878").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode445() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26889"),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26889"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26890").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26889"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26891").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26889"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26889"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode446() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26892"),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26892"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26893").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26892"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26892"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode447() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26894"),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26894"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26895").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26894"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26896").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26894"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26894"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode448() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26897"),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26897"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26898").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26897"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26897"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode449() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26899"),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26899"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26900").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26899"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26901").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26899"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26899"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode450() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26902"),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26902"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26903").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26902"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26902"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode451() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26907"),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26907"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26908").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26907"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26909").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26907"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26907"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode452() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26910"),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26910"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26911").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26910"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26912").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26910"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26910"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode453() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26913"),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26913"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26914").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26913"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26913"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode454() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=26915"),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=26915"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26916").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26915"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode455() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=29880"),
            new QualifiedName(0, "RequestTickets"),
            new LocalizedText("", "RequestTickets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=29880"),
            NodeId.parse("i=46"),
            NodeId.parse("i=29881").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=29880"),
            NodeId.parse("i=47"),
            NodeId.parse("i=29878").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode456() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25229"),
            new QualifiedName(0, "AddPriorityMappingEntry"),
            new LocalizedText("", "AddPriorityMappingEntry"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25229"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25230").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25229"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25229"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25227").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode457() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=25231"),
            new QualifiedName(0, "DeletePriorityMappingEntry"),
            new LocalizedText("", "DeletePriorityMappingEntry"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=25231"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25232").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25231"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25231"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25227").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode458() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19839"),
            new QualifiedName(0, "ConfigureSerialization"),
            new LocalizedText("", "ConfigureSerialization"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19839"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19840").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19839"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19841").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19839"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19839"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19824").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode459() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19353"),
            new QualifiedName(0, "GetRecords"),
            new LocalizedText("", "GetRecords"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19353"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19354").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19353"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19355").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19353"), NodeId.parse("i=37"), NodeId.parse("i=78").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19353"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19352").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode460() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=24372"),
            new QualifiedName(0, "ReleaseContinuationPoint"),
            new LocalizedText("", "ReleaseContinuationPoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=24372"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24373").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24372"), NodeId.parse("i=37"), NodeId.parse("i=80").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24372"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19352").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode461() {
    var node =
        new UaMethodNode(
            this.context,
            NodeId.parse("i=19373"),
            new QualifiedName(0, "GetRecords"),
            new LocalizedText("", "GetRecords"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19373"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19374").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19373"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19375").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19373"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19372").expanded(),
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
  }
}

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
            new NodeId(0, 11489),
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
            new NodeId(0, 11489), new NodeId(0, 46), new NodeId(0, 11490).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11489), new NodeId(0, 46), new NodeId(0, 11491).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11489), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11489), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12871),
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
            new NodeId(0, 12871), new NodeId(0, 46), new NodeId(0, 12872).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12871), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12871), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12746),
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
            new NodeId(0, 12746), new NodeId(0, 46), new NodeId(0, 12747).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12746), new NodeId(0, 46), new NodeId(0, 12748).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12746), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12746), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12883),
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
            new NodeId(0, 12883), new NodeId(0, 46), new NodeId(0, 12884).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12883), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12883), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16296),
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
            new NodeId(0, 16296), new NodeId(0, 46), new NodeId(0, 16297).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16296), new NodeId(0, 46), new NodeId(0, 16298).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16296), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16296), new NodeId(0, 47), new NodeId(0, 16295).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16299),
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
            new NodeId(0, 16299), new NodeId(0, 46), new NodeId(0, 16300).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16299), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16299), new NodeId(0, 47), new NodeId(0, 16295).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 32416),
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
        new Reference(new NodeId(0, 32416), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32416), new NodeId(0, 47), new NodeId(0, 32411).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11580),
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
            new NodeId(0, 11580), new NodeId(0, 46), new NodeId(0, 11581).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11580), new NodeId(0, 46), new NodeId(0, 11582).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11580), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11580), new NodeId(0, 47), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11583),
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
            new NodeId(0, 11583), new NodeId(0, 46), new NodeId(0, 11584).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11583), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11583), new NodeId(0, 47), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11585),
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
            new NodeId(0, 11585), new NodeId(0, 46), new NodeId(0, 11586).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11585), new NodeId(0, 46), new NodeId(0, 11587).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11585), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11585), new NodeId(0, 47), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11588),
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
            new NodeId(0, 11588), new NodeId(0, 46), new NodeId(0, 11589).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11588), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11588), new NodeId(0, 47), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11590),
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
            new NodeId(0, 11590), new NodeId(0, 46), new NodeId(0, 11591).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11590), new NodeId(0, 46), new NodeId(0, 11592).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11590), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11590), new NodeId(0, 47), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11593),
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
            new NodeId(0, 11593), new NodeId(0, 46), new NodeId(0, 11594).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11593), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11593), new NodeId(0, 47), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11615),
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
        new Reference(new NodeId(0, 11615), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11615), new NodeId(0, 47), new NodeId(0, 11595).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11629),
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
            new NodeId(0, 11629), new NodeId(0, 46), new NodeId(0, 11630).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11629), new NodeId(0, 46), new NodeId(0, 11631).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11629), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11629), new NodeId(0, 47), new NodeId(0, 11624).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11632),
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
            new NodeId(0, 11632), new NodeId(0, 46), new NodeId(0, 11633).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11632), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11632), new NodeId(0, 47), new NodeId(0, 11624).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11634),
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
            new NodeId(0, 11634), new NodeId(0, 46), new NodeId(0, 11635).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11634), new NodeId(0, 46), new NodeId(0, 11636).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11634), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11634), new NodeId(0, 47), new NodeId(0, 11624).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11637),
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
            new NodeId(0, 11637), new NodeId(0, 46), new NodeId(0, 11638).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11637), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11637), new NodeId(0, 47), new NodeId(0, 11624).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11639),
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
            new NodeId(0, 11639), new NodeId(0, 46), new NodeId(0, 11640).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11639), new NodeId(0, 46), new NodeId(0, 11641).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11639), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11639), new NodeId(0, 47), new NodeId(0, 11624).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11642),
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
            new NodeId(0, 11642), new NodeId(0, 46), new NodeId(0, 11643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11642), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11642), new NodeId(0, 47), new NodeId(0, 11624).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16301),
            new QualifiedName(0, "AddRole"),
            new LocalizedText("", "AddRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16301), new NodeId(0, 46), new NodeId(0, 16302).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16301), new NodeId(0, 46), new NodeId(0, 16303).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16301), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16304),
            new QualifiedName(0, "RemoveRole"),
            new LocalizedText("", "RemoveRole"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16304), new NodeId(0, 46), new NodeId(0, 16305).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16304), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 11492),
            new QualifiedName(0, "GetMonitoredItems"),
            new LocalizedText("", "GetMonitoredItems"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11492), new NodeId(0, 46), new NodeId(0, 11493).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11492), new NodeId(0, 46), new NodeId(0, 11494).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11492), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12873),
            new QualifiedName(0, "ResendData"),
            new LocalizedText("", "ResendData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12873), new NodeId(0, 46), new NodeId(0, 12874).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12873), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12749),
            new QualifiedName(0, "SetSubscriptionDurable"),
            new LocalizedText("", "SetSubscriptionDurable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12749), new NodeId(0, 46), new NodeId(0, 12750).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12749), new NodeId(0, 46), new NodeId(0, 12751).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12749), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12886),
            new QualifiedName(0, "RequestServerStateChange"),
            new LocalizedText("", "RequestServerStateChange"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12886), new NodeId(0, 46), new NodeId(0, 12887).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12886), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13355),
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
            new NodeId(0, 13355), new NodeId(0, 46), new NodeId(0, 13356).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13355), new NodeId(0, 46), new NodeId(0, 13357).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13355), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13355), new NodeId(0, 47), new NodeId(0, 13354).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13358),
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
            new NodeId(0, 13358), new NodeId(0, 46), new NodeId(0, 13359).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13358), new NodeId(0, 46), new NodeId(0, 13360).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13358), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13358), new NodeId(0, 47), new NodeId(0, 13354).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17718),
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
            new NodeId(0, 17718), new NodeId(0, 46), new NodeId(0, 17719).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17718), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17718), new NodeId(0, 47), new NodeId(0, 13354).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13363),
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
            new NodeId(0, 13363), new NodeId(0, 46), new NodeId(0, 13364).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13363), new NodeId(0, 46), new NodeId(0, 13365).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13363), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13363), new NodeId(0, 47), new NodeId(0, 13354).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13372),
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
            new NodeId(0, 13372), new NodeId(0, 46), new NodeId(0, 13373).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13372), new NodeId(0, 46), new NodeId(0, 13374).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13372), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13372), new NodeId(0, 47), new NodeId(0, 13366).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13375),
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
            new NodeId(0, 13375), new NodeId(0, 46), new NodeId(0, 13376).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13375), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13375), new NodeId(0, 47), new NodeId(0, 13366).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13377),
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
            new NodeId(0, 13377), new NodeId(0, 46), new NodeId(0, 13378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13377), new NodeId(0, 46), new NodeId(0, 13379).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13377), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13377), new NodeId(0, 47), new NodeId(0, 13366).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13380),
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
            new NodeId(0, 13380), new NodeId(0, 46), new NodeId(0, 13381).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13380), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13380), new NodeId(0, 47), new NodeId(0, 13366).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13382),
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
            new NodeId(0, 13382), new NodeId(0, 46), new NodeId(0, 13383).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13382), new NodeId(0, 46), new NodeId(0, 13384).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13382), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13382), new NodeId(0, 47), new NodeId(0, 13366).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13385),
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
            new NodeId(0, 13385), new NodeId(0, 46), new NodeId(0, 13386).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13385), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13385), new NodeId(0, 47), new NodeId(0, 13366).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13387),
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
            new NodeId(0, 13387), new NodeId(0, 46), new NodeId(0, 13388).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13387), new NodeId(0, 46), new NodeId(0, 13389).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13387), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13387), new NodeId(0, 47), new NodeId(0, 13353).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13390),
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
            new NodeId(0, 13390), new NodeId(0, 46), new NodeId(0, 13391).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13390), new NodeId(0, 46), new NodeId(0, 13392).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13390), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13390), new NodeId(0, 47), new NodeId(0, 13353).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13393),
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
            new NodeId(0, 13393), new NodeId(0, 46), new NodeId(0, 13394).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13393), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13393), new NodeId(0, 47), new NodeId(0, 13353).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13395),
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
            new NodeId(0, 13395), new NodeId(0, 46), new NodeId(0, 13396).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13395), new NodeId(0, 46), new NodeId(0, 13397).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13395), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13395), new NodeId(0, 47), new NodeId(0, 13353).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16348),
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
            new NodeId(0, 16348), new NodeId(0, 46), new NodeId(0, 16349).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16348), new NodeId(0, 46), new NodeId(0, 16350).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16348), new NodeId(0, 47), new NodeId(0, 16314).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16351),
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
            new NodeId(0, 16351), new NodeId(0, 46), new NodeId(0, 16352).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16351), new NodeId(0, 46), new NodeId(0, 16353).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16351), new NodeId(0, 47), new NodeId(0, 16314).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16354),
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
            new NodeId(0, 16354), new NodeId(0, 46), new NodeId(0, 16355).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16354), new NodeId(0, 47), new NodeId(0, 16314).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16356),
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
            new NodeId(0, 16356), new NodeId(0, 46), new NodeId(0, 16357).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16356), new NodeId(0, 46), new NodeId(0, 16358).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16356), new NodeId(0, 47), new NodeId(0, 16314).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15746),
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
            new NodeId(0, 15746), new NodeId(0, 46), new NodeId(0, 15747).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15746), new NodeId(0, 46), new NodeId(0, 15748).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15746), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15746), new NodeId(0, 47), new NodeId(0, 15744).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15749),
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
            new NodeId(0, 15749), new NodeId(0, 46), new NodeId(0, 16359).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15749), new NodeId(0, 46), new NodeId(0, 15750).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15749), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15749), new NodeId(0, 47), new NodeId(0, 15744).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15751),
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
            new NodeId(0, 15751), new NodeId(0, 46), new NodeId(0, 15752).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15751), new NodeId(0, 46), new NodeId(0, 15753).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15751), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15751), new NodeId(0, 47), new NodeId(0, 15744).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15794),
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
        new Reference(new NodeId(0, 15794), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15794), new NodeId(0, 47), new NodeId(0, 15754).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15843),
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
        new Reference(new NodeId(0, 15843), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15843), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15997),
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
            new NodeId(0, 15997), new NodeId(0, 46), new NodeId(0, 15998).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15997), new NodeId(0, 46), new NodeId(0, 15999).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15997), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15997), new NodeId(0, 47), new NodeId(0, 15607).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16000),
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
            new NodeId(0, 16000), new NodeId(0, 46), new NodeId(0, 16001).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16000), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16000), new NodeId(0, 47), new NodeId(0, 15607).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15624),
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
            new NodeId(0, 15624), new NodeId(0, 46), new NodeId(0, 15625).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15624), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15624), new NodeId(0, 47), new NodeId(0, 15620).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15626),
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
            new NodeId(0, 15626), new NodeId(0, 46), new NodeId(0, 15627).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15626), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15626), new NodeId(0, 47), new NodeId(0, 15620).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16176),
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
            new NodeId(0, 16176), new NodeId(0, 46), new NodeId(0, 16177).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16176), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16176), new NodeId(0, 47), new NodeId(0, 15620).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16178),
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
            new NodeId(0, 16178), new NodeId(0, 46), new NodeId(0, 16179).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16178), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16178), new NodeId(0, 47), new NodeId(0, 15620).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16180),
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
            new NodeId(0, 16180), new NodeId(0, 46), new NodeId(0, 16181).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16180), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16180), new NodeId(0, 47), new NodeId(0, 15620).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16182),
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
            new NodeId(0, 16182), new NodeId(0, 46), new NodeId(0, 16183).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16182), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16182), new NodeId(0, 47), new NodeId(0, 15620).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15672),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15672), new NodeId(0, 46), new NodeId(0, 15673).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15672), new NodeId(0, 47), new NodeId(0, 15668).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15674),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15674), new NodeId(0, 46), new NodeId(0, 15675).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15674), new NodeId(0, 47), new NodeId(0, 15668).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16217),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16217), new NodeId(0, 46), new NodeId(0, 16218).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16217), new NodeId(0, 47), new NodeId(0, 15668).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16219),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16219), new NodeId(0, 46), new NodeId(0, 16220).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16219), new NodeId(0, 47), new NodeId(0, 15668).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16221),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16221), new NodeId(0, 46), new NodeId(0, 16222).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16221), new NodeId(0, 47), new NodeId(0, 15668).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16223),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16223), new NodeId(0, 46), new NodeId(0, 16224).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16223), new NodeId(0, 47), new NodeId(0, 15668).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15684),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15684), new NodeId(0, 46), new NodeId(0, 15685).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15684), new NodeId(0, 47), new NodeId(0, 15680).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15686),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15686), new NodeId(0, 46), new NodeId(0, 15687).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15686), new NodeId(0, 47), new NodeId(0, 15680).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16228),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16228), new NodeId(0, 46), new NodeId(0, 16229).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16228), new NodeId(0, 47), new NodeId(0, 15680).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16230),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16230), new NodeId(0, 46), new NodeId(0, 16231).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16230), new NodeId(0, 47), new NodeId(0, 15680).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode67() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16232),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16232), new NodeId(0, 46), new NodeId(0, 16233).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16232), new NodeId(0, 47), new NodeId(0, 15680).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode68() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16234),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16234), new NodeId(0, 46), new NodeId(0, 16235).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16234), new NodeId(0, 47), new NodeId(0, 15680).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode69() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16041),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16041), new NodeId(0, 46), new NodeId(0, 16042).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16041), new NodeId(0, 47), new NodeId(0, 16036).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode70() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16043),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16043), new NodeId(0, 46), new NodeId(0, 16044).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16043), new NodeId(0, 47), new NodeId(0, 16036).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode71() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16239),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16239), new NodeId(0, 46), new NodeId(0, 16240).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16239), new NodeId(0, 47), new NodeId(0, 16036).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode72() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16241),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16241), new NodeId(0, 46), new NodeId(0, 16242).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16241), new NodeId(0, 47), new NodeId(0, 16036).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode73() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16243),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16243), new NodeId(0, 46), new NodeId(0, 16244).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16243), new NodeId(0, 47), new NodeId(0, 16036).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode74() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16245),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16245), new NodeId(0, 46), new NodeId(0, 16246).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16245), new NodeId(0, 47), new NodeId(0, 16036).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode75() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15696),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15696), new NodeId(0, 46), new NodeId(0, 15697).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15696), new NodeId(0, 47), new NodeId(0, 15692).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode76() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15698),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15698), new NodeId(0, 46), new NodeId(0, 15699).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15698), new NodeId(0, 47), new NodeId(0, 15692).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode77() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16250),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16250), new NodeId(0, 46), new NodeId(0, 16251).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16250), new NodeId(0, 47), new NodeId(0, 15692).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode78() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16252),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16252), new NodeId(0, 46), new NodeId(0, 16253).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16252), new NodeId(0, 47), new NodeId(0, 15692).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode79() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16254),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16254), new NodeId(0, 46), new NodeId(0, 16255).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16254), new NodeId(0, 47), new NodeId(0, 15692).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode80() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16256),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16256), new NodeId(0, 46), new NodeId(0, 16257).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16256), new NodeId(0, 47), new NodeId(0, 15692).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode81() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15720),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15720), new NodeId(0, 46), new NodeId(0, 15721).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15720), new NodeId(0, 47), new NodeId(0, 15716).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode82() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15722),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15722), new NodeId(0, 46), new NodeId(0, 15723).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15722), new NodeId(0, 47), new NodeId(0, 15716).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode83() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16272),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16272), new NodeId(0, 46), new NodeId(0, 16273).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16272), new NodeId(0, 47), new NodeId(0, 15716).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode84() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16274),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16274), new NodeId(0, 46), new NodeId(0, 16275).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16274), new NodeId(0, 47), new NodeId(0, 15716).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode85() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16276),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16276), new NodeId(0, 46), new NodeId(0, 16277).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16276), new NodeId(0, 47), new NodeId(0, 15716).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode86() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16278),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16278), new NodeId(0, 46), new NodeId(0, 16279).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16278), new NodeId(0, 47), new NodeId(0, 15716).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode87() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15708),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15708), new NodeId(0, 46), new NodeId(0, 15709).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15708), new NodeId(0, 47), new NodeId(0, 15704).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode88() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15710),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15710), new NodeId(0, 46), new NodeId(0, 15711).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15710), new NodeId(0, 47), new NodeId(0, 15704).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode89() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16261),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16261), new NodeId(0, 46), new NodeId(0, 16262).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16261), new NodeId(0, 47), new NodeId(0, 15704).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode90() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16263),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16263), new NodeId(0, 46), new NodeId(0, 16264).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16263), new NodeId(0, 47), new NodeId(0, 15704).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode91() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16265),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16265), new NodeId(0, 46), new NodeId(0, 16266).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16265), new NodeId(0, 47), new NodeId(0, 15704).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode92() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16267),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 16267), new NodeId(0, 46), new NodeId(0, 16268).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16267), new NodeId(0, 47), new NodeId(0, 15704).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode93() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25572),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25572), new NodeId(0, 46), new NodeId(0, 25573).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25572), new NodeId(0, 47), new NodeId(0, 25565).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode94() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25574),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25574), new NodeId(0, 46), new NodeId(0, 25575).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25574), new NodeId(0, 47), new NodeId(0, 25565).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode95() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25576),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25576), new NodeId(0, 46), new NodeId(0, 25577).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25576), new NodeId(0, 47), new NodeId(0, 25565).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode96() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25578),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25578), new NodeId(0, 46), new NodeId(0, 25579).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25578), new NodeId(0, 47), new NodeId(0, 25565).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode97() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25580),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25580), new NodeId(0, 46), new NodeId(0, 25581).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25580), new NodeId(0, 47), new NodeId(0, 25565).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode98() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25582),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25582), new NodeId(0, 46), new NodeId(0, 25583).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25582), new NodeId(0, 47), new NodeId(0, 25565).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode99() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25610),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25610), new NodeId(0, 46), new NodeId(0, 25611).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25610), new NodeId(0, 47), new NodeId(0, 25603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode100() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25612),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25612), new NodeId(0, 46), new NodeId(0, 25613).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25612), new NodeId(0, 47), new NodeId(0, 25603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode101() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25614),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25614), new NodeId(0, 46), new NodeId(0, 25615).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25614), new NodeId(0, 47), new NodeId(0, 25603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode102() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25616),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25616), new NodeId(0, 46), new NodeId(0, 25617).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25616), new NodeId(0, 47), new NodeId(0, 25603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode103() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25618),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25618), new NodeId(0, 46), new NodeId(0, 25619).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25618), new NodeId(0, 47), new NodeId(0, 25603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode104() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25620),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25620), new NodeId(0, 46), new NodeId(0, 25621).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25620), new NodeId(0, 47), new NodeId(0, 25603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode105() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25591),
            new QualifiedName(0, "AddIdentity"),
            new LocalizedText("", "AddIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25591), new NodeId(0, 46), new NodeId(0, 25592).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25591), new NodeId(0, 47), new NodeId(0, 25584).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode106() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25593),
            new QualifiedName(0, "RemoveIdentity"),
            new LocalizedText("", "RemoveIdentity"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25593), new NodeId(0, 46), new NodeId(0, 25594).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25593), new NodeId(0, 47), new NodeId(0, 25584).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode107() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25595),
            new QualifiedName(0, "AddApplication"),
            new LocalizedText("", "AddApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25595), new NodeId(0, 46), new NodeId(0, 25596).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25595), new NodeId(0, 47), new NodeId(0, 25584).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode108() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25597),
            new QualifiedName(0, "RemoveApplication"),
            new LocalizedText("", "RemoveApplication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25597), new NodeId(0, 46), new NodeId(0, 25598).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25597), new NodeId(0, 47), new NodeId(0, 25584).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode109() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25599),
            new QualifiedName(0, "AddEndpoint"),
            new LocalizedText("", "AddEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25599), new NodeId(0, 46), new NodeId(0, 25600).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25599), new NodeId(0, 47), new NodeId(0, 25584).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode110() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25601),
            new QualifiedName(0, "RemoveEndpoint"),
            new LocalizedText("", "RemoveEndpoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25601), new NodeId(0, 46), new NodeId(0, 25602).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25601), new NodeId(0, 47), new NodeId(0, 25584).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode111() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9028),
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
            new NodeId(0, 9028), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9028), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9028), new NodeId(0, 47), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode112() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9027),
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
            new NodeId(0, 9027), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9027), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9027), new NodeId(0, 47), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode113() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9029),
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
            new NodeId(0, 9029), new NodeId(0, 46), new NodeId(0, 9030).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9029), new NodeId(0, 3065), new NodeId(0, 2829).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9029), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9029), new NodeId(0, 47), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode114() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 3875),
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
            new NodeId(0, 3875), new NodeId(0, 46), new NodeId(0, 3876).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3875), new NodeId(0, 3065), new NodeId(0, 2787).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3875), new NodeId(0, 3065), new NodeId(0, 2788).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3875), new NodeId(0, 47), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode115() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12912),
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
            new NodeId(0, 12912), new NodeId(0, 46), new NodeId(0, 12913).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12912), new NodeId(0, 3065), new NodeId(0, 2787).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12912), new NodeId(0, 3065), new NodeId(0, 2788).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12912), new NodeId(0, 47), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode116() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9069),
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
            new NodeId(0, 9069), new NodeId(0, 46), new NodeId(0, 9070).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9069), new NodeId(0, 3065), new NodeId(0, 8927).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9069), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9069), new NodeId(0, 47), new NodeId(0, 2830).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode117() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24312),
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
            new NodeId(0, 24312), new NodeId(0, 46), new NodeId(0, 24313).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24312), new NodeId(0, 3065), new NodeId(0, 8927).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24312), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24312), new NodeId(0, 47), new NodeId(0, 2830).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode118() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9111),
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
            new NodeId(0, 9111), new NodeId(0, 46), new NodeId(0, 9112).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9111), new NodeId(0, 3065), new NodeId(0, 8944).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9111), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9111), new NodeId(0, 47), new NodeId(0, 2881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode119() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9113),
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
            new NodeId(0, 9113), new NodeId(0, 46), new NodeId(0, 9114).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9113), new NodeId(0, 3065), new NodeId(0, 8961).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9113), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9113), new NodeId(0, 47), new NodeId(0, 2881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode120() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9213),
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
            new NodeId(0, 9213), new NodeId(0, 46), new NodeId(0, 9214).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9213), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9213), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9213), new NodeId(0, 47), new NodeId(0, 9178).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode121() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9211),
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
            new NodeId(0, 9211), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9211), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9211), new NodeId(0, 47), new NodeId(0, 9178).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode122() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 9212),
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
            new NodeId(0, 9212), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9212), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9212), new NodeId(0, 47), new NodeId(0, 9178).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode123() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16402),
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
            new NodeId(0, 16402), new NodeId(0, 3065), new NodeId(0, 17242).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16402), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16402), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode124() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16403),
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
            new NodeId(0, 16403), new NodeId(0, 3065), new NodeId(0, 17225).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16403), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16403), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode125() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24316),
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
            new NodeId(0, 24316), new NodeId(0, 46), new NodeId(0, 24317).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24316), new NodeId(0, 3065), new NodeId(0, 17225).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24316), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24316), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode126() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17868),
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
            new NodeId(0, 17868), new NodeId(0, 3065), new NodeId(0, 17225).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17868), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17868), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode127() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24318),
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
            new NodeId(0, 24318), new NodeId(0, 46), new NodeId(0, 24319).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24318), new NodeId(0, 3065), new NodeId(0, 17225).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24318), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24318), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode128() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17869),
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
            new NodeId(0, 17869), new NodeId(0, 3065), new NodeId(0, 17259).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17869), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17869), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode129() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24320),
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
            new NodeId(0, 24320), new NodeId(0, 46), new NodeId(0, 24321).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24320), new NodeId(0, 3065), new NodeId(0, 17259).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24320), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24320), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode130() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17870),
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
            new NodeId(0, 17870), new NodeId(0, 3065), new NodeId(0, 17259).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17870), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17870), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode131() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24322),
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
            new NodeId(0, 24322), new NodeId(0, 46), new NodeId(0, 24323).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24322), new NodeId(0, 3065), new NodeId(0, 17259).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24322), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24322), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode132() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18199),
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
            new NodeId(0, 18199), new NodeId(0, 3065), new NodeId(0, 15013).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18199), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18199), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode133() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24324),
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
            new NodeId(0, 24324), new NodeId(0, 46), new NodeId(0, 24325).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24324), new NodeId(0, 3065), new NodeId(0, 15013).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24324), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24324), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode134() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24744),
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
            new NodeId(0, 24744), new NodeId(0, 46), new NodeId(0, 25154).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24744), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24744), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode135() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16439),
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
            new NodeId(0, 16439), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16439), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16439), new NodeId(0, 47), new NodeId(0, 16406).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode136() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16440),
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
            new NodeId(0, 16440), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16440), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16440), new NodeId(0, 47), new NodeId(0, 16406).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode137() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16441),
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
            new NodeId(0, 16441), new NodeId(0, 46), new NodeId(0, 16442).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16441), new NodeId(0, 3065), new NodeId(0, 2829).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16441), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16441), new NodeId(0, 47), new NodeId(0, 16406).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode138() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16461),
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
            new NodeId(0, 16461), new NodeId(0, 46), new NodeId(0, 16462).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16461), new NodeId(0, 3065), new NodeId(0, 8944).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16461), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16461), new NodeId(0, 47), new NodeId(0, 16406).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode139() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2949),
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
            new NodeId(0, 2949), new NodeId(0, 46), new NodeId(0, 2991).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2949), new NodeId(0, 53), new NodeId(0, 2935).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2949), new NodeId(0, 53), new NodeId(0, 2945).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2949), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2949), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2949), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode140() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24756),
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
            new NodeId(0, 24756), new NodeId(0, 46), new NodeId(0, 24757).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24756), new NodeId(0, 53), new NodeId(0, 2935).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24756), new NodeId(0, 53), new NodeId(0, 2945).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24756), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24756), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24756), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode141() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2947),
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
            new NodeId(0, 2947), new NodeId(0, 53), new NodeId(0, 2940).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2947), new NodeId(0, 53), new NodeId(0, 2943).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2947), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2947), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2947), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode142() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24758),
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
            new NodeId(0, 24758), new NodeId(0, 46), new NodeId(0, 24759).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24758), new NodeId(0, 53), new NodeId(0, 2940).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24758), new NodeId(0, 53), new NodeId(0, 2943).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24758), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24758), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24758), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode143() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2948),
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
            new NodeId(0, 2948), new NodeId(0, 53), new NodeId(0, 2936).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2948), new NodeId(0, 53), new NodeId(0, 2942).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2948), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2948), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2948), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode144() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24760),
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
            new NodeId(0, 24760), new NodeId(0, 46), new NodeId(0, 24761).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24760), new NodeId(0, 53), new NodeId(0, 2936).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24760), new NodeId(0, 53), new NodeId(0, 2942).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24760), new NodeId(0, 3065), new NodeId(0, 11093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24760), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24760), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode145() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18666),
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
            new NodeId(0, 18666), new NodeId(0, 3065), new NodeId(0, 2127).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18666), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18666), new NodeId(0, 47), new NodeId(0, 17279).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode146() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2426),
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
            new NodeId(0, 2426), new NodeId(0, 53), new NodeId(0, 2410).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2426), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2426), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode147() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2427),
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
            new NodeId(0, 2427), new NodeId(0, 53), new NodeId(0, 2416).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2427), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2427), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode148() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2428),
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
            new NodeId(0, 2428), new NodeId(0, 53), new NodeId(0, 2418).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2428), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2428), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode149() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2429),
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
            new NodeId(0, 2429), new NodeId(0, 53), new NodeId(0, 2412).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2429), new NodeId(0, 53), new NodeId(0, 2420).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2429), new NodeId(0, 53), new NodeId(0, 2424).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2429), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2429), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode150() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 2430),
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
            new NodeId(0, 2430), new NodeId(0, 53), new NodeId(0, 2408).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2430), new NodeId(0, 53), new NodeId(0, 2420).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2430), new NodeId(0, 53), new NodeId(0, 2422).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2430), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2430), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode151() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12543),
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
            new NodeId(0, 12543), new NodeId(0, 46), new NodeId(0, 12544).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12543), new NodeId(0, 46), new NodeId(0, 12545).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12543), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12543), new NodeId(0, 47), new NodeId(0, 12522).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode152() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12546),
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
            new NodeId(0, 12546), new NodeId(0, 46), new NodeId(0, 12705).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12546), new NodeId(0, 46), new NodeId(0, 12547).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12546), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12546), new NodeId(0, 47), new NodeId(0, 12522).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode153() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12548),
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
            new NodeId(0, 12548), new NodeId(0, 46), new NodeId(0, 12549).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12548), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12548), new NodeId(0, 47), new NodeId(0, 12522).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode154() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12550),
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
            new NodeId(0, 12550), new NodeId(0, 46), new NodeId(0, 12551).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12550), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12550), new NodeId(0, 47), new NodeId(0, 12522).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode155() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13605),
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
            new NodeId(0, 13605), new NodeId(0, 46), new NodeId(0, 13606).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13605), new NodeId(0, 46), new NodeId(0, 13607).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13605), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13605), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode156() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13608),
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
            new NodeId(0, 13608), new NodeId(0, 46), new NodeId(0, 13609).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13608), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13608), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode157() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13610),
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
            new NodeId(0, 13610), new NodeId(0, 46), new NodeId(0, 13611).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13610), new NodeId(0, 46), new NodeId(0, 13612).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13610), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13610), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode158() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13613),
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
            new NodeId(0, 13613), new NodeId(0, 46), new NodeId(0, 13614).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13613), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13613), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode159() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13615),
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
            new NodeId(0, 13615), new NodeId(0, 46), new NodeId(0, 13616).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13615), new NodeId(0, 46), new NodeId(0, 13617).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13615), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13615), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode160() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13618),
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
            new NodeId(0, 13618), new NodeId(0, 46), new NodeId(0, 13619).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13618), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13618), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode161() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13621),
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
            new NodeId(0, 13621), new NodeId(0, 46), new NodeId(0, 13622).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13621), new NodeId(0, 46), new NodeId(0, 13623).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13621), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13621), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode162() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13624),
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
            new NodeId(0, 13624), new NodeId(0, 46), new NodeId(0, 13625).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13624), new NodeId(0, 46), new NodeId(0, 13626).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13624), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13624), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode163() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13627),
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
            new NodeId(0, 13627), new NodeId(0, 46), new NodeId(0, 13628).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13627), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13627), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode164() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13629),
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
            new NodeId(0, 13629), new NodeId(0, 46), new NodeId(0, 13630).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13629), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13629), new NodeId(0, 47), new NodeId(0, 13599).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode165() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23526),
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
            new NodeId(0, 23526), new NodeId(0, 46), new NodeId(0, 23527).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23526), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23526), new NodeId(0, 47), new NodeId(0, 12555).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode166() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19483),
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
            new NodeId(0, 19483), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19483), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19483), new NodeId(0, 47), new NodeId(0, 19450).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode167() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19484),
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
            new NodeId(0, 19484), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19484), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19484), new NodeId(0, 47), new NodeId(0, 19450).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode168() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19485),
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
            new NodeId(0, 19485), new NodeId(0, 46), new NodeId(0, 19486).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19485), new NodeId(0, 3065), new NodeId(0, 2829).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19485), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19485), new NodeId(0, 47), new NodeId(0, 19450).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode169() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19505),
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
            new NodeId(0, 19505), new NodeId(0, 46), new NodeId(0, 19506).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19505), new NodeId(0, 3065), new NodeId(0, 8944).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19505), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19505), new NodeId(0, 47), new NodeId(0, 19450).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode170() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 20176),
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
            new NodeId(0, 20176), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20176), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20176), new NodeId(0, 47), new NodeId(0, 20143).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode171() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 20177),
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
            new NodeId(0, 20177), new NodeId(0, 3065), new NodeId(0, 2803).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20177), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20177), new NodeId(0, 47), new NodeId(0, 20143).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode172() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 20178),
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
            new NodeId(0, 20178), new NodeId(0, 46), new NodeId(0, 20179).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20178), new NodeId(0, 3065), new NodeId(0, 2829).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20178), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20178), new NodeId(0, 47), new NodeId(0, 20143).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode173() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 20198),
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
            new NodeId(0, 20198), new NodeId(0, 46), new NodeId(0, 20199).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20198), new NodeId(0, 3065), new NodeId(0, 8944).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20198), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20198), new NodeId(0, 47), new NodeId(0, 20143).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode174() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13821),
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
            new NodeId(0, 13821), new NodeId(0, 46), new NodeId(0, 13822).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13821), new NodeId(0, 46), new NodeId(0, 13823).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13821), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13821), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode175() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13824),
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
            new NodeId(0, 13824), new NodeId(0, 46), new NodeId(0, 13825).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13824), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13824), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode176() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13826),
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
            new NodeId(0, 13826), new NodeId(0, 46), new NodeId(0, 13827).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13826), new NodeId(0, 46), new NodeId(0, 13828).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13826), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13826), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode177() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13829),
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
            new NodeId(0, 13829), new NodeId(0, 46), new NodeId(0, 13830).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13829), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13829), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode178() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13831),
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
            new NodeId(0, 13831), new NodeId(0, 46), new NodeId(0, 13832).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13831), new NodeId(0, 46), new NodeId(0, 13833).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13831), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13831), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode179() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13834),
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
            new NodeId(0, 13834), new NodeId(0, 46), new NodeId(0, 13835).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13834), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13834), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode180() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13837),
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
            new NodeId(0, 13837), new NodeId(0, 46), new NodeId(0, 13838).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13837), new NodeId(0, 46), new NodeId(0, 13839).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13837), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13837), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode181() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13840),
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
            new NodeId(0, 13840), new NodeId(0, 46), new NodeId(0, 13841).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13840), new NodeId(0, 46), new NodeId(0, 13842).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13840), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13840), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode182() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13843),
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
            new NodeId(0, 13843), new NodeId(0, 46), new NodeId(0, 13844).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13843), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13843), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode183() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13845),
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
            new NodeId(0, 13845), new NodeId(0, 46), new NodeId(0, 13846).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13845), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13845), new NodeId(0, 47), new NodeId(0, 13815).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode184() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13855),
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
            new NodeId(0, 13855), new NodeId(0, 46), new NodeId(0, 13856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13855), new NodeId(0, 46), new NodeId(0, 13857).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13855), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13855), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode185() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13858),
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
            new NodeId(0, 13858), new NodeId(0, 46), new NodeId(0, 13859).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13858), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13858), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode186() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13860),
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
            new NodeId(0, 13860), new NodeId(0, 46), new NodeId(0, 13861).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13860), new NodeId(0, 46), new NodeId(0, 13862).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13860), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13860), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode187() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13863),
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
            new NodeId(0, 13863), new NodeId(0, 46), new NodeId(0, 13864).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13863), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13863), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode188() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13865),
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
            new NodeId(0, 13865), new NodeId(0, 46), new NodeId(0, 13866).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13865), new NodeId(0, 46), new NodeId(0, 13867).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13865), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13865), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode189() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13868),
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
            new NodeId(0, 13868), new NodeId(0, 46), new NodeId(0, 13869).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13868), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13868), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode190() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13871),
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
            new NodeId(0, 13871), new NodeId(0, 46), new NodeId(0, 13872).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13871), new NodeId(0, 46), new NodeId(0, 13873).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13871), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13871), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode191() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13874),
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
            new NodeId(0, 13874), new NodeId(0, 46), new NodeId(0, 13875).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13874), new NodeId(0, 46), new NodeId(0, 13876).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13874), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13874), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode192() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13877),
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
            new NodeId(0, 13877), new NodeId(0, 46), new NodeId(0, 13878).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13877), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13877), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode193() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13879),
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
            new NodeId(0, 13879), new NodeId(0, 46), new NodeId(0, 13880).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13879), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13879), new NodeId(0, 47), new NodeId(0, 13849).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode194() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13889),
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
            new NodeId(0, 13889), new NodeId(0, 46), new NodeId(0, 13890).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13889), new NodeId(0, 46), new NodeId(0, 13891).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13889), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13889), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode195() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13892),
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
            new NodeId(0, 13892), new NodeId(0, 46), new NodeId(0, 13893).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13892), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13892), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode196() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13894),
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
            new NodeId(0, 13894), new NodeId(0, 46), new NodeId(0, 13895).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13894), new NodeId(0, 46), new NodeId(0, 13896).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13894), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13894), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode197() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13897),
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
            new NodeId(0, 13897), new NodeId(0, 46), new NodeId(0, 13898).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13897), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13897), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode198() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13899),
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
            new NodeId(0, 13899), new NodeId(0, 46), new NodeId(0, 13900).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13899), new NodeId(0, 46), new NodeId(0, 13901).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13899), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13899), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode199() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13902),
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
            new NodeId(0, 13902), new NodeId(0, 46), new NodeId(0, 13903).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13902), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13902), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode200() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13905),
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
            new NodeId(0, 13905), new NodeId(0, 46), new NodeId(0, 13906).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13905), new NodeId(0, 46), new NodeId(0, 13907).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13905), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13905), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode201() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13908),
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
            new NodeId(0, 13908), new NodeId(0, 46), new NodeId(0, 13909).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13908), new NodeId(0, 46), new NodeId(0, 13910).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13908), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13908), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode202() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13911),
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
            new NodeId(0, 13911), new NodeId(0, 46), new NodeId(0, 13912).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13911), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13911), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode203() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13913),
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
            new NodeId(0, 13913), new NodeId(0, 46), new NodeId(0, 13914).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13913), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13913), new NodeId(0, 47), new NodeId(0, 13883).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode204() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13923),
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
            new NodeId(0, 13923), new NodeId(0, 46), new NodeId(0, 13924).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13923), new NodeId(0, 46), new NodeId(0, 13925).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13923), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13923), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode205() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13926),
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
            new NodeId(0, 13926), new NodeId(0, 46), new NodeId(0, 13927).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13926), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13926), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode206() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13928),
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
            new NodeId(0, 13928), new NodeId(0, 46), new NodeId(0, 13929).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13928), new NodeId(0, 46), new NodeId(0, 13930).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13928), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13928), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode207() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13931),
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
            new NodeId(0, 13931), new NodeId(0, 46), new NodeId(0, 13932).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13931), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13931), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode208() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13933),
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
            new NodeId(0, 13933), new NodeId(0, 46), new NodeId(0, 13934).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13933), new NodeId(0, 46), new NodeId(0, 13935).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13933), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13933), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode209() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13936),
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
            new NodeId(0, 13936), new NodeId(0, 46), new NodeId(0, 13937).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13936), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13936), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode210() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13939),
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
            new NodeId(0, 13939), new NodeId(0, 46), new NodeId(0, 13940).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13939), new NodeId(0, 46), new NodeId(0, 13941).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13939), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13939), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode211() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13942),
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
            new NodeId(0, 13942), new NodeId(0, 46), new NodeId(0, 13943).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13942), new NodeId(0, 46), new NodeId(0, 13944).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13942), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13942), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode212() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13945),
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
            new NodeId(0, 13945), new NodeId(0, 46), new NodeId(0, 13946).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13945), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13945), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode213() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13947),
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
            new NodeId(0, 13947), new NodeId(0, 46), new NodeId(0, 13948).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13947), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13947), new NodeId(0, 47), new NodeId(0, 13917).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode214() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16717),
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
            new NodeId(0, 16717), new NodeId(0, 46), new NodeId(0, 16722).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16717), new NodeId(0, 46), new NodeId(0, 16723).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16717), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16717), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode215() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16724),
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
            new NodeId(0, 16724), new NodeId(0, 46), new NodeId(0, 16725).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16724), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16724), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode216() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16726),
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
            new NodeId(0, 16726), new NodeId(0, 46), new NodeId(0, 16727).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16726), new NodeId(0, 46), new NodeId(0, 16728).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16726), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16726), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode217() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16729),
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
            new NodeId(0, 16729), new NodeId(0, 46), new NodeId(0, 16730).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16729), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16729), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode218() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16732),
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
            new NodeId(0, 16732), new NodeId(0, 46), new NodeId(0, 16733).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16732), new NodeId(0, 46), new NodeId(0, 16734).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16732), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16732), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode219() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16735),
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
            new NodeId(0, 16735), new NodeId(0, 46), new NodeId(0, 16736).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16735), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16735), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode220() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16741),
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
            new NodeId(0, 16741), new NodeId(0, 46), new NodeId(0, 16742).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16741), new NodeId(0, 46), new NodeId(0, 16743).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16741), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16741), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode221() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16744),
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
            new NodeId(0, 16744), new NodeId(0, 46), new NodeId(0, 16745).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16744), new NodeId(0, 46), new NodeId(0, 16746).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16744), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16744), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode222() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16747),
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
            new NodeId(0, 16747), new NodeId(0, 46), new NodeId(0, 16748).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16747), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16747), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode223() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16749),
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
            new NodeId(0, 16749), new NodeId(0, 46), new NodeId(0, 16750).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16749), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16749), new NodeId(0, 47), new NodeId(0, 16709).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode224() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18533),
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
            new NodeId(0, 18533), new NodeId(0, 46), new NodeId(0, 18534).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18533), new NodeId(0, 46), new NodeId(0, 18535).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18533), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18533), new NodeId(0, 47), new NodeId(0, 16663).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode225() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18539),
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
        new Reference(new NodeId(0, 18539), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18539), new NodeId(0, 47), new NodeId(0, 16663).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode226() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18541),
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
            new NodeId(0, 18541), new NodeId(0, 46), new NodeId(0, 18542).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18541), new NodeId(0, 46), new NodeId(0, 18543).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18541), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18541), new NodeId(0, 47), new NodeId(0, 16663).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode227() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18544),
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
            new NodeId(0, 18544), new NodeId(0, 46), new NodeId(0, 18545).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18544), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18544), new NodeId(0, 47), new NodeId(0, 16663).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode228() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13958),
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
            new NodeId(0, 13958), new NodeId(0, 46), new NodeId(0, 13959).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13958), new NodeId(0, 46), new NodeId(0, 13960).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13958), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13958), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode229() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13961),
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
            new NodeId(0, 13961), new NodeId(0, 46), new NodeId(0, 13962).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13961), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13961), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode230() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13963),
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
            new NodeId(0, 13963), new NodeId(0, 46), new NodeId(0, 13964).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13963), new NodeId(0, 46), new NodeId(0, 13965).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13963), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13963), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode231() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13966),
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
            new NodeId(0, 13966), new NodeId(0, 46), new NodeId(0, 13967).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13966), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13966), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode232() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13968),
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
            new NodeId(0, 13968), new NodeId(0, 46), new NodeId(0, 13969).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13968), new NodeId(0, 46), new NodeId(0, 13970).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13968), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13968), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode233() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13971),
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
            new NodeId(0, 13971), new NodeId(0, 46), new NodeId(0, 13972).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13971), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13971), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode234() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13974),
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
            new NodeId(0, 13974), new NodeId(0, 46), new NodeId(0, 13975).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13974), new NodeId(0, 46), new NodeId(0, 13976).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13974), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13974), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode235() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13977),
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
            new NodeId(0, 13977), new NodeId(0, 46), new NodeId(0, 13978).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13977), new NodeId(0, 46), new NodeId(0, 13979).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13977), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13977), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode236() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13980),
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
            new NodeId(0, 13980), new NodeId(0, 46), new NodeId(0, 13981).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13980), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13980), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode237() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13982),
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
            new NodeId(0, 13982), new NodeId(0, 46), new NodeId(0, 13983).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13982), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13982), new NodeId(0, 47), new NodeId(0, 13952).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode238() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12616),
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
            new NodeId(0, 12616), new NodeId(0, 46), new NodeId(0, 12617).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12616), new NodeId(0, 46), new NodeId(0, 12618).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12616), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12616), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode239() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 32296),
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
            new NodeId(0, 32296), new NodeId(0, 46), new NodeId(0, 32297).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32296), new NodeId(0, 46), new NodeId(0, 32298).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32296), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32296), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode240() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12734),
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
        new Reference(new NodeId(0, 12734), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12734), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode241() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25698),
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
        new Reference(new NodeId(0, 25698), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25698), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode242() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12731),
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
            new NodeId(0, 12731), new NodeId(0, 46), new NodeId(0, 12732).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12731), new NodeId(0, 46), new NodeId(0, 12733).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12731), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12731), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode243() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12775),
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
            new NodeId(0, 12775), new NodeId(0, 46), new NodeId(0, 12776).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12775), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12775), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode244() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25699),
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
        new Reference(new NodeId(0, 25699), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25699), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode245() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12647),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12647), new NodeId(0, 46), new NodeId(0, 12648).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12647), new NodeId(0, 46), new NodeId(0, 12649).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12647), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode246() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12650),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12650), new NodeId(0, 46), new NodeId(0, 12651).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12650), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode247() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12652),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12652), new NodeId(0, 46), new NodeId(0, 12653).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12652), new NodeId(0, 46), new NodeId(0, 12654).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12652), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode248() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12655),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12655), new NodeId(0, 46), new NodeId(0, 12656).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12655), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode249() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12657),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12657), new NodeId(0, 46), new NodeId(0, 12658).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12657), new NodeId(0, 46), new NodeId(0, 12659).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12657), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode250() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12660),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12660), new NodeId(0, 46), new NodeId(0, 12661).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12660), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode251() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12663),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12663), new NodeId(0, 46), new NodeId(0, 12664).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12663), new NodeId(0, 46), new NodeId(0, 12665).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12663), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode252() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12666),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12666), new NodeId(0, 46), new NodeId(0, 14160).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12666), new NodeId(0, 46), new NodeId(0, 12667).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12666), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode253() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12668),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12668), new NodeId(0, 46), new NodeId(0, 12669).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12668), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode254() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12670),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12670), new NodeId(0, 46), new NodeId(0, 12671).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12670), new NodeId(0, 47), new NodeId(0, 12642).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode255() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14095),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14095), new NodeId(0, 46), new NodeId(0, 14096).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14095), new NodeId(0, 46), new NodeId(0, 14097).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14095), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode256() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14098),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14098), new NodeId(0, 46), new NodeId(0, 14099).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14098), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode257() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14100),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14100), new NodeId(0, 46), new NodeId(0, 14101).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14100), new NodeId(0, 46), new NodeId(0, 14102).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14100), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode258() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14103),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14103), new NodeId(0, 46), new NodeId(0, 14104).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14103), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode259() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14105),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14105), new NodeId(0, 46), new NodeId(0, 14106).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14105), new NodeId(0, 46), new NodeId(0, 14107).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14105), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode260() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14108),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14108), new NodeId(0, 46), new NodeId(0, 14109).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14108), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode261() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14111),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14111), new NodeId(0, 46), new NodeId(0, 14112).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14111), new NodeId(0, 46), new NodeId(0, 14113).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14111), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode262() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14114),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14114), new NodeId(0, 46), new NodeId(0, 14115).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14114), new NodeId(0, 46), new NodeId(0, 14116).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14114), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode263() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14117),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14117), new NodeId(0, 46), new NodeId(0, 14118).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14117), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode264() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14119),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14119), new NodeId(0, 46), new NodeId(0, 14120).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14119), new NodeId(0, 47), new NodeId(0, 14089).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode265() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14129),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14129), new NodeId(0, 46), new NodeId(0, 14130).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14129), new NodeId(0, 46), new NodeId(0, 14131).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14129), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode266() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14132),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14132), new NodeId(0, 46), new NodeId(0, 14133).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14132), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode267() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14134),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14134), new NodeId(0, 46), new NodeId(0, 14135).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14134), new NodeId(0, 46), new NodeId(0, 14136).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14134), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode268() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14137),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14137), new NodeId(0, 46), new NodeId(0, 14138).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14137), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode269() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14139),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14139), new NodeId(0, 46), new NodeId(0, 14140).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14139), new NodeId(0, 46), new NodeId(0, 14141).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14139), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode270() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14142),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14142), new NodeId(0, 46), new NodeId(0, 14143).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14142), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode271() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14145),
            new QualifiedName(0, "OpenWithMasks"),
            new LocalizedText("", "OpenWithMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14145), new NodeId(0, 46), new NodeId(0, 14146).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14145), new NodeId(0, 46), new NodeId(0, 14147).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14145), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode272() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14148),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14148), new NodeId(0, 46), new NodeId(0, 14149).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14148), new NodeId(0, 46), new NodeId(0, 14150).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14148), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode273() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14151),
            new QualifiedName(0, "AddCertificate"),
            new LocalizedText("", "AddCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14151), new NodeId(0, 46), new NodeId(0, 14152).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14151), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode274() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14153),
            new QualifiedName(0, "RemoveCertificate"),
            new LocalizedText("", "RemoveCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14153), new NodeId(0, 46), new NodeId(0, 14154).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14153), new NodeId(0, 47), new NodeId(0, 14123).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode275() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 13737),
            new QualifiedName(0, "UpdateCertificate"),
            new LocalizedText("", "UpdateCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 13737), new NodeId(0, 46), new NodeId(0, 13738).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13737), new NodeId(0, 46), new NodeId(0, 13739).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13737), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode276() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12740),
            new QualifiedName(0, "ApplyChanges"),
            new LocalizedText("", "ApplyChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(5)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12740), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode277() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25708),
            new QualifiedName(0, "CancelChanges"),
            new LocalizedText("", "CancelChanges"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(5)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25708), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode278() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12737),
            new QualifiedName(0, "CreateSigningRequest"),
            new LocalizedText("", "CreateSigningRequest"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12737), new NodeId(0, 46), new NodeId(0, 12738).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12737), new NodeId(0, 46), new NodeId(0, 12739).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12737), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode279() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 12777),
            new QualifiedName(0, "GetRejectedList"),
            new LocalizedText("", "GetRejectedList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12777), new NodeId(0, 46), new NodeId(0, 12778).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12777), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode280() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25709),
            new QualifiedName(0, "ResetToServerDefaults"),
            new LocalizedText("", "ResetToServerDefaults"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25709), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode281() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17522),
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
            new NodeId(0, 17522), new NodeId(0, 46), new NodeId(0, 17523).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17522), new NodeId(0, 46), new NodeId(0, 17524).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17522), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17522), new NodeId(0, 47), new NodeId(0, 17496).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode282() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17528),
            new QualifiedName(0, "CreateCredential"),
            new LocalizedText("", "CreateCredential"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17528), new NodeId(0, 46), new NodeId(0, 17529).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17528), new NodeId(0, 46), new NodeId(0, 17530).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17528), new NodeId(0, 47), new NodeId(0, 18155).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode283() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17534),
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
            new NodeId(0, 17534), new NodeId(0, 46), new NodeId(0, 17535).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17534), new NodeId(0, 46), new NodeId(0, 17536).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17534), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17534), new NodeId(0, 47), new NodeId(0, 18001).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode284() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18006),
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
            new NodeId(0, 18006), new NodeId(0, 46), new NodeId(0, 18007).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18006), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18006), new NodeId(0, 47), new NodeId(0, 18001).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode285() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18008),
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
        new Reference(new NodeId(0, 18008), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18008), new NodeId(0, 47), new NodeId(0, 18001).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode286() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15907),
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
            new NodeId(0, 15907), new NodeId(0, 46), new NodeId(0, 15908).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15907), new NodeId(0, 46), new NodeId(0, 15909).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15907), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15907), new NodeId(0, 47), new NodeId(0, 15906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode287() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15910),
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
            new NodeId(0, 15910), new NodeId(0, 46), new NodeId(0, 15911).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15910), new NodeId(0, 46), new NodeId(0, 15912).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15910), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15910), new NodeId(0, 47), new NodeId(0, 15906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode288() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15914),
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
            new NodeId(0, 15914), new NodeId(0, 46), new NodeId(0, 15915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15914), new NodeId(0, 46), new NodeId(0, 15916).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15914), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15914), new NodeId(0, 47), new NodeId(0, 15913).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode289() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15917),
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
            new NodeId(0, 15917), new NodeId(0, 46), new NodeId(0, 15918).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15917), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15917), new NodeId(0, 47), new NodeId(0, 15913).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode290() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25278),
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
            new NodeId(0, 25278), new NodeId(0, 46), new NodeId(0, 25279).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25278), new NodeId(0, 46), new NodeId(0, 25280).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25278), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25278), new NodeId(0, 47), new NodeId(0, 25277).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode291() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25281),
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
            new NodeId(0, 25281), new NodeId(0, 46), new NodeId(0, 25282).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25281), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25281), new NodeId(0, 47), new NodeId(0, 25277).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode292() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15454),
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
            new NodeId(0, 15454), new NodeId(0, 46), new NodeId(0, 15455).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15454), new NodeId(0, 46), new NodeId(0, 15456).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15454), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15454), new NodeId(0, 47), new NodeId(0, 15453).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode293() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15457),
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
            new NodeId(0, 15457), new NodeId(0, 46), new NodeId(0, 15458).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15457), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15457), new NodeId(0, 47), new NodeId(0, 15453).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode294() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25293),
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
            new NodeId(0, 25293), new NodeId(0, 46), new NodeId(0, 25294).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25293), new NodeId(0, 46), new NodeId(0, 25295).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25293), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25293), new NodeId(0, 47), new NodeId(0, 15453).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode295() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25296),
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
            new NodeId(0, 25296), new NodeId(0, 46), new NodeId(0, 25297).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25296), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25296), new NodeId(0, 47), new NodeId(0, 15453).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode296() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15461),
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
            new NodeId(0, 15461), new NodeId(0, 46), new NodeId(0, 15462).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15461), new NodeId(0, 46), new NodeId(0, 15463).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15461), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15461), new NodeId(0, 47), new NodeId(0, 15452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode297() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15464),
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
            new NodeId(0, 15464), new NodeId(0, 46), new NodeId(0, 15465).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15464), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15464), new NodeId(0, 47), new NodeId(0, 15452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode298() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25312),
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
            new NodeId(0, 25312), new NodeId(0, 46), new NodeId(0, 25313).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25312), new NodeId(0, 46), new NodeId(0, 25314).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25312), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25312), new NodeId(0, 47), new NodeId(0, 15452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode299() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25315),
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
            new NodeId(0, 25315), new NodeId(0, 46), new NodeId(0, 25316).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25315), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25315), new NodeId(0, 47), new NodeId(0, 15452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode300() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25624),
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
        new Reference(new NodeId(0, 25624), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25624), new NodeId(0, 47), new NodeId(0, 15471).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode301() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25625),
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
        new Reference(new NodeId(0, 25625), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25625), new NodeId(0, 47), new NodeId(0, 15471).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode302() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25641),
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
            new NodeId(0, 25641), new NodeId(0, 46), new NodeId(0, 25642).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25641), new NodeId(0, 46), new NodeId(0, 25643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25641), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25641), new NodeId(0, 47), new NodeId(0, 25337).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode303() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25644),
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
            new NodeId(0, 25644), new NodeId(0, 46), new NodeId(0, 25645).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25644), new NodeId(0, 46), new NodeId(0, 25646).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25644), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25644), new NodeId(0, 47), new NodeId(0, 25337).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode304() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25647),
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
        new Reference(new NodeId(0, 25647), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25647), new NodeId(0, 47), new NodeId(0, 25337).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode305() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25348),
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
            new NodeId(0, 25348), new NodeId(0, 46), new NodeId(0, 25349).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25348), new NodeId(0, 46), new NodeId(0, 25350).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25348), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25348), new NodeId(0, 47), new NodeId(0, 25347).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode306() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25351),
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
            new NodeId(0, 25351), new NodeId(0, 46), new NodeId(0, 25352).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25351), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25351), new NodeId(0, 47), new NodeId(0, 25347).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode307() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25353),
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
            new NodeId(0, 25353), new NodeId(0, 46), new NodeId(0, 25354).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25353), new NodeId(0, 46), new NodeId(0, 25355).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25353), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25353), new NodeId(0, 47), new NodeId(0, 25347).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode308() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25356),
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
            new NodeId(0, 25356), new NodeId(0, 46), new NodeId(0, 25357).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25356), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25356), new NodeId(0, 47), new NodeId(0, 25347).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode309() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25655),
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
            new NodeId(0, 25655), new NodeId(0, 46), new NodeId(0, 25656).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25655), new NodeId(0, 46), new NodeId(0, 25657).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25655), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25655), new NodeId(0, 47), new NodeId(0, 25358).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode310() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25658),
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
            new NodeId(0, 25658), new NodeId(0, 46), new NodeId(0, 25659).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25658), new NodeId(0, 46), new NodeId(0, 25660).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25658), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25658), new NodeId(0, 47), new NodeId(0, 25358).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode311() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25661),
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
        new Reference(new NodeId(0, 25661), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25661), new NodeId(0, 47), new NodeId(0, 25358).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode312() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25366),
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
            new NodeId(0, 25366), new NodeId(0, 46), new NodeId(0, 25367).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25366), new NodeId(0, 46), new NodeId(0, 25368).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25366), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25366), new NodeId(0, 47), new NodeId(0, 25346).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode313() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25369),
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
            new NodeId(0, 25369), new NodeId(0, 46), new NodeId(0, 25370).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25369), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25369), new NodeId(0, 47), new NodeId(0, 25346).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode314() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25371),
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
            new NodeId(0, 25371), new NodeId(0, 46), new NodeId(0, 25372).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25371), new NodeId(0, 46), new NodeId(0, 25373).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25371), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25371), new NodeId(0, 47), new NodeId(0, 25346).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode315() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25374),
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
            new NodeId(0, 25374), new NodeId(0, 46), new NodeId(0, 25375).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25374), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25374), new NodeId(0, 47), new NodeId(0, 25346).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode316() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17296),
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
            new NodeId(0, 17296), new NodeId(0, 46), new NodeId(0, 17297).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17296), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17296), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode317() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16598),
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
            new NodeId(0, 16598), new NodeId(0, 46), new NodeId(0, 16599).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16598), new NodeId(0, 46), new NodeId(0, 16600).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16598), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16598), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode318() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14432),
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
            new NodeId(0, 14432), new NodeId(0, 46), new NodeId(0, 14433).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14432), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14432), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode319() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25411),
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
            new NodeId(0, 25411), new NodeId(0, 46), new NodeId(0, 25412).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25411), new NodeId(0, 46), new NodeId(0, 25413).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25411), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25411), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode320() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25414),
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
            new NodeId(0, 25414), new NodeId(0, 46), new NodeId(0, 25415).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25414), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25414), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode321() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25416),
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
            new NodeId(0, 25416), new NodeId(0, 46), new NodeId(0, 25417).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25416), new NodeId(0, 46), new NodeId(0, 25418).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25416), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25416), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode322() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25419),
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
            new NodeId(0, 25419), new NodeId(0, 46), new NodeId(0, 25420).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25419), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25419), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode323() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25421),
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
            new NodeId(0, 25421), new NodeId(0, 46), new NodeId(0, 25422).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25421), new NodeId(0, 46), new NodeId(0, 25423).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25421), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25421), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode324() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25424),
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
            new NodeId(0, 25424), new NodeId(0, 46), new NodeId(0, 25425).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25424), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25424), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode325() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25426),
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
            new NodeId(0, 25426), new NodeId(0, 46), new NodeId(0, 25427).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25426), new NodeId(0, 46), new NodeId(0, 25428).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25426), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25426), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode326() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25429),
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
            new NodeId(0, 25429), new NodeId(0, 46), new NodeId(0, 25430).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25429), new NodeId(0, 46), new NodeId(0, 25431).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25429), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25429), new NodeId(0, 47), new NodeId(0, 25403).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode327() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 18727),
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
        new Reference(new NodeId(0, 18727), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18727), new NodeId(0, 47), new NodeId(0, 18715).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode328() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15215),
            new QualifiedName(0, "GetSecurityKeys"),
            new LocalizedText("", "GetSecurityKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15215), new NodeId(0, 46), new NodeId(0, 15216).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15215), new NodeId(0, 46), new NodeId(0, 15217).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15215), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode329() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15440),
            new QualifiedName(0, "GetSecurityGroup"),
            new LocalizedText("", "GetSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15440), new NodeId(0, 46), new NodeId(0, 15441).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15440), new NodeId(0, 46), new NodeId(0, 15442).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15440), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode330() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15444),
            new QualifiedName(0, "AddSecurityGroup"),
            new LocalizedText("", "AddSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15444), new NodeId(0, 46), new NodeId(0, 15445).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15444), new NodeId(0, 46), new NodeId(0, 15446).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15444), new NodeId(0, 47), new NodeId(0, 15443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode331() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15447),
            new QualifiedName(0, "RemoveSecurityGroup"),
            new LocalizedText("", "RemoveSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15447), new NodeId(0, 46), new NodeId(0, 15448).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15447), new NodeId(0, 47), new NodeId(0, 15443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode332() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25441),
            new QualifiedName(0, "AddPushTarget"),
            new LocalizedText("", "AddPushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25441), new NodeId(0, 46), new NodeId(0, 25442).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25441), new NodeId(0, 46), new NodeId(0, 25443).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25441), new NodeId(0, 47), new NodeId(0, 25440).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode333() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25444),
            new QualifiedName(0, "RemovePushTarget"),
            new LocalizedText("", "RemovePushTarget"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25444), new NodeId(0, 46), new NodeId(0, 25445).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25444), new NodeId(0, 47), new NodeId(0, 25440).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode334() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17364),
            new QualifiedName(0, "SetSecurityKeys"),
            new LocalizedText("", "SetSecurityKeys"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 25584), new PermissionType(UInteger.valueOf(61455))),
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17364), new NodeId(0, 46), new NodeId(0, 17365).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17364), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode335() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17366),
            new QualifiedName(0, "AddConnection"),
            new LocalizedText("", "AddConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17366), new NodeId(0, 46), new NodeId(0, 17367).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17366), new NodeId(0, 46), new NodeId(0, 17368).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17366), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode336() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17369),
            new QualifiedName(0, "RemoveConnection"),
            new LocalizedText("", "RemoveConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17369), new NodeId(0, 46), new NodeId(0, 17370).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17369), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode337() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25459),
            new QualifiedName(0, "Open"),
            new LocalizedText("", "Open"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25459), new NodeId(0, 46), new NodeId(0, 25460).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25459), new NodeId(0, 46), new NodeId(0, 25461).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25459), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode338() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25462),
            new QualifiedName(0, "Close"),
            new LocalizedText("", "Close"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25462), new NodeId(0, 46), new NodeId(0, 25463).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25462), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode339() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25464),
            new QualifiedName(0, "Read"),
            new LocalizedText("", "Read"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25464), new NodeId(0, 46), new NodeId(0, 25465).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25464), new NodeId(0, 46), new NodeId(0, 25466).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25464), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode340() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25467),
            new QualifiedName(0, "Write"),
            new LocalizedText("", "Write"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25467), new NodeId(0, 46), new NodeId(0, 25468).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25467), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode341() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25469),
            new QualifiedName(0, "GetPosition"),
            new LocalizedText("", "GetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25469), new NodeId(0, 46), new NodeId(0, 25470).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25469), new NodeId(0, 46), new NodeId(0, 25471).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25469), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode342() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25472),
            new QualifiedName(0, "SetPosition"),
            new LocalizedText("", "SetPosition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25472), new NodeId(0, 46), new NodeId(0, 25473).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25472), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode343() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25474),
            new QualifiedName(0, "ReserveIds"),
            new LocalizedText("", "ReserveIds"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25474), new NodeId(0, 46), new NodeId(0, 25475).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25474), new NodeId(0, 46), new NodeId(0, 25476).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25474), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode344() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25477),
            new QualifiedName(0, "CloseAndUpdate"),
            new LocalizedText("", "CloseAndUpdate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25477), new NodeId(0, 46), new NodeId(0, 25478).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25477), new NodeId(0, 46), new NodeId(0, 25479).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25477), new NodeId(0, 47), new NodeId(0, 25451).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode345() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17421),
            new QualifiedName(0, "Reset"),
            new LocalizedText("", "Reset"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17421), new NodeId(0, 47), new NodeId(0, 17409).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode346() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25505),
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
            new NodeId(0, 25505), new NodeId(0, 46), new NodeId(0, 25506).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25505), new NodeId(0, 46), new NodeId(0, 25507).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25505), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25505), new NodeId(0, 47), new NodeId(0, 25482).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode347() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25508),
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
            new NodeId(0, 25508), new NodeId(0, 46), new NodeId(0, 25509).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25508), new NodeId(0, 46), new NodeId(0, 25510).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25508), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25508), new NodeId(0, 47), new NodeId(0, 25482).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode348() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15482),
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
            new NodeId(0, 15482), new NodeId(0, 46), new NodeId(0, 15483).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15482), new NodeId(0, 46), new NodeId(0, 15484).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15482), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15482), new NodeId(0, 47), new NodeId(0, 15481).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode349() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15485),
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
            new NodeId(0, 15485), new NodeId(0, 46), new NodeId(0, 15486).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15485), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15485), new NodeId(0, 47), new NodeId(0, 15481).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode350() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15491),
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
            new NodeId(0, 15491), new NodeId(0, 46), new NodeId(0, 15492).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15491), new NodeId(0, 46), new NodeId(0, 15493).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15491), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15491), new NodeId(0, 47), new NodeId(0, 15489).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode351() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15494),
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
            new NodeId(0, 15494), new NodeId(0, 46), new NodeId(0, 15495).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15494), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15494), new NodeId(0, 47), new NodeId(0, 15489).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode352() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14555),
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
            new NodeId(0, 14555), new NodeId(0, 46), new NodeId(0, 14556).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14555), new NodeId(0, 46), new NodeId(0, 14557).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14555), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14555), new NodeId(0, 47), new NodeId(0, 14534).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode353() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14558),
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
            new NodeId(0, 14558), new NodeId(0, 46), new NodeId(0, 14559).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14558), new NodeId(0, 46), new NodeId(0, 14560).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14558), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14558), new NodeId(0, 47), new NodeId(0, 14534).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode354() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15052),
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
            new NodeId(0, 15052), new NodeId(0, 46), new NodeId(0, 15053).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15052), new NodeId(0, 46), new NodeId(0, 15517).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15052), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15052), new NodeId(0, 47), new NodeId(0, 14572).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode355() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14479),
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
            new NodeId(0, 14479), new NodeId(0, 46), new NodeId(0, 14480).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14479), new NodeId(0, 46), new NodeId(0, 14481).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14479), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14479), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode356() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14482),
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
            new NodeId(0, 14482), new NodeId(0, 46), new NodeId(0, 14483).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14482), new NodeId(0, 46), new NodeId(0, 14484).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14482), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14482), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode357() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16842),
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
            new NodeId(0, 16842), new NodeId(0, 46), new NodeId(0, 16843).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16842), new NodeId(0, 46), new NodeId(0, 16853).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16842), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16842), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode358() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16881),
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
            new NodeId(0, 16881), new NodeId(0, 46), new NodeId(0, 16882).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16881), new NodeId(0, 46), new NodeId(0, 16883).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16881), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16881), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode359() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14485),
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
            new NodeId(0, 14485), new NodeId(0, 46), new NodeId(0, 14486).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14485), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14485), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode360() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16884),
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
            new NodeId(0, 16884), new NodeId(0, 46), new NodeId(0, 16894).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16884), new NodeId(0, 46), new NodeId(0, 16922).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16884), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16884), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode361() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16923),
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
            new NodeId(0, 16923), new NodeId(0, 46), new NodeId(0, 16924).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16923), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16923), new NodeId(0, 47), new NodeId(0, 14478).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode362() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14493),
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
            new NodeId(0, 14493), new NodeId(0, 46), new NodeId(0, 14494).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14493), new NodeId(0, 46), new NodeId(0, 14495).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14493), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14493), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode363() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14496),
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
            new NodeId(0, 14496), new NodeId(0, 46), new NodeId(0, 14497).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14496), new NodeId(0, 46), new NodeId(0, 14498).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14496), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14496), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode364() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16935),
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
            new NodeId(0, 16935), new NodeId(0, 46), new NodeId(0, 16958).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16935), new NodeId(0, 46), new NodeId(0, 16959).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16935), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16935), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode365() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16960),
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
            new NodeId(0, 16960), new NodeId(0, 46), new NodeId(0, 16961).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16960), new NodeId(0, 46), new NodeId(0, 16971).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16960), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16960), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode366() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14499),
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
            new NodeId(0, 14499), new NodeId(0, 46), new NodeId(0, 14500).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14499), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14499), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode367() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16994),
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
            new NodeId(0, 16994), new NodeId(0, 46), new NodeId(0, 16995).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16994), new NodeId(0, 46), new NodeId(0, 16996).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16994), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16994), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode368() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 16997),
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
            new NodeId(0, 16997), new NodeId(0, 46), new NodeId(0, 17007).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16997), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16997), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode369() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19253),
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
        new Reference(new NodeId(0, 19253), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19253), new NodeId(0, 47), new NodeId(0, 19241).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode370() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17427),
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
            new NodeId(0, 17427), new NodeId(0, 46), new NodeId(0, 17428).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17427), new NodeId(0, 46), new NodeId(0, 17456).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17427), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17427), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode371() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17465),
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
            new NodeId(0, 17465), new NodeId(0, 46), new NodeId(0, 17507).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17465), new NodeId(0, 46), new NodeId(0, 17508).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17465), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17465), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode372() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14225),
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
            new NodeId(0, 14225), new NodeId(0, 46), new NodeId(0, 14226).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14225), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14225), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode373() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17824),
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
        new Reference(new NodeId(0, 17824), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17824), new NodeId(0, 47), new NodeId(0, 17812).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode374() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17969),
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
            new NodeId(0, 17969), new NodeId(0, 46), new NodeId(0, 17976).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17969), new NodeId(0, 46), new NodeId(0, 17987).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17969), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17969), new NodeId(0, 47), new NodeId(0, 17725).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode375() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17992),
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
            new NodeId(0, 17992), new NodeId(0, 46), new NodeId(0, 17993).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17992), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17992), new NodeId(0, 47), new NodeId(0, 17725).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode376() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 21027),
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
        new Reference(new NodeId(0, 21027), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21027), new NodeId(0, 47), new NodeId(0, 21015).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode377() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 21082),
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
            new NodeId(0, 21082), new NodeId(0, 46), new NodeId(0, 21083).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21082), new NodeId(0, 46), new NodeId(0, 21084).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21082), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21082), new NodeId(0, 47), new NodeId(0, 17999).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode378() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 21085),
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
            new NodeId(0, 21085), new NodeId(0, 46), new NodeId(0, 21086).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21085), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21085), new NodeId(0, 47), new NodeId(0, 17999).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode379() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19562),
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
        new Reference(new NodeId(0, 19562), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19562), new NodeId(0, 47), new NodeId(0, 19550).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode380() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19621),
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
        new Reference(new NodeId(0, 19621), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19621), new NodeId(0, 47), new NodeId(0, 19609).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode381() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17386),
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
            new NodeId(0, 17386), new NodeId(0, 46), new NodeId(0, 17387).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17386), new NodeId(0, 46), new NodeId(0, 17388).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17386), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17386), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode382() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 17389),
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
            new NodeId(0, 17389), new NodeId(0, 46), new NodeId(0, 17390).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17389), new NodeId(0, 46), new NodeId(0, 17391).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17389), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17389), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode383() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15115),
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
            new NodeId(0, 15115), new NodeId(0, 46), new NodeId(0, 15116).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15115), new NodeId(0, 46), new NodeId(0, 15117).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15115), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15115), new NodeId(0, 47), new NodeId(0, 15111).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode384() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 15118),
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
            new NodeId(0, 15118), new NodeId(0, 46), new NodeId(0, 15119).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15118), new NodeId(0, 46), new NodeId(0, 15120).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15118), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15118), new NodeId(0, 47), new NodeId(0, 15111).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode385() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23797),
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
            new NodeId(0, 23797), new NodeId(0, 46), new NodeId(0, 23798).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23797), new NodeId(0, 46), new NodeId(0, 23799).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23797), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23797), new NodeId(0, 47), new NodeId(0, 23796).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode386() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23800),
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
            new NodeId(0, 23800), new NodeId(0, 46), new NodeId(0, 23801).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23800), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23800), new NodeId(0, 47), new NodeId(0, 23796).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode387() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23802),
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
            new NodeId(0, 23802), new NodeId(0, 46), new NodeId(0, 23803).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23802), new NodeId(0, 46), new NodeId(0, 23804).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23802), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23802), new NodeId(0, 47), new NodeId(0, 23796).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode388() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23805),
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
            new NodeId(0, 23805), new NodeId(0, 46), new NodeId(0, 23806).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23805), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23805), new NodeId(0, 47), new NodeId(0, 23796).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode389() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23811),
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
            new NodeId(0, 23811), new NodeId(0, 46), new NodeId(0, 23812).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23811), new NodeId(0, 46), new NodeId(0, 23813).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23811), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23811), new NodeId(0, 47), new NodeId(0, 23795).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode390() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23814),
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
            new NodeId(0, 23814), new NodeId(0, 46), new NodeId(0, 23815).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23814), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23814), new NodeId(0, 47), new NodeId(0, 23795).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode391() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23816),
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
            new NodeId(0, 23816), new NodeId(0, 46), new NodeId(0, 23817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23816), new NodeId(0, 46), new NodeId(0, 23818).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23816), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23816), new NodeId(0, 47), new NodeId(0, 23795).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode392() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23819),
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
            new NodeId(0, 23819), new NodeId(0, 46), new NodeId(0, 23820).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23819), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23819), new NodeId(0, 47), new NodeId(0, 23795).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode393() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14645),
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
        new Reference(new NodeId(0, 14645), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14645), new NodeId(0, 47), new NodeId(0, 14643).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode394() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 14646),
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
        new Reference(new NodeId(0, 14646), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14646), new NodeId(0, 47), new NodeId(0, 14643).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode395() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 19689),
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
        new Reference(new NodeId(0, 19689), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19689), new NodeId(0, 47), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode396() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23459),
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
            new NodeId(0, 23459), new NodeId(0, 46), new NodeId(0, 23460).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23459), new NodeId(0, 46), new NodeId(0, 23461).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23459), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23459), new NodeId(0, 47), new NodeId(0, 23458).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode397() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23462),
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
            new NodeId(0, 23462), new NodeId(0, 46), new NodeId(0, 23463).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23462), new NodeId(0, 46), new NodeId(0, 23464).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23462), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23462), new NodeId(0, 47), new NodeId(0, 23456).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode398() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23476),
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
            new NodeId(0, 23476), new NodeId(0, 46), new NodeId(0, 23477).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23476), new NodeId(0, 46), new NodeId(0, 23478).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23476), new NodeId(0, 47), new NodeId(0, 23470).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode399() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23485),
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
            new NodeId(0, 23485), new NodeId(0, 46), new NodeId(0, 23486).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23485), new NodeId(0, 46), new NodeId(0, 23487).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23485), new NodeId(0, 47), new NodeId(0, 23479).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode400() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 23494),
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
            new NodeId(0, 23494), new NodeId(0, 46), new NodeId(0, 23495).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23494), new NodeId(0, 46), new NodeId(0, 23496).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23494), new NodeId(0, 47), new NodeId(0, 23488).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode401() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24269),
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
            new NodeId(0, 24269), new NodeId(0, 46), new NodeId(0, 24270).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24269), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24269), new NodeId(0, 47), new NodeId(0, 24264).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode402() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24271),
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
            new NodeId(0, 24271), new NodeId(0, 46), new NodeId(0, 24272).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24271), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24271), new NodeId(0, 47), new NodeId(0, 24264).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode403() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24273),
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
            new NodeId(0, 24273), new NodeId(0, 46), new NodeId(0, 24274).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24273), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24273), new NodeId(0, 47), new NodeId(0, 24264).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode404() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24275),
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
            new NodeId(0, 24275), new NodeId(0, 46), new NodeId(0, 24276).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24275), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24275), new NodeId(0, 47), new NodeId(0, 24264).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode405() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24304),
            new QualifiedName(0, "AddUser"),
            new LocalizedText("", "AddUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24304), new NodeId(0, 46), new NodeId(0, 24305).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24304), new NodeId(0, 47), new NodeId(0, 24290).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode406() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24306),
            new QualifiedName(0, "ModifyUser"),
            new LocalizedText("", "ModifyUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24306), new NodeId(0, 46), new NodeId(0, 24307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24306), new NodeId(0, 47), new NodeId(0, 24290).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode407() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24308),
            new QualifiedName(0, "RemoveUser"),
            new LocalizedText("", "RemoveUser"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24308), new NodeId(0, 46), new NodeId(0, 24309).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24308), new NodeId(0, 47), new NodeId(0, 24290).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode408() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 24310),
            new QualifiedName(0, "ChangePassword"),
            new LocalizedText("", "ChangePassword"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(61455)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(3)),
            true,
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24310), new NodeId(0, 46), new NodeId(0, 24311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24310), new NodeId(0, 47), new NodeId(0, 24290).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode409() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26873),
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
            new NodeId(0, 26873), new NodeId(0, 46), new NodeId(0, 26874).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26873), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26873), new NodeId(0, 47), new NodeId(0, 26871).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode410() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26875),
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
            new NodeId(0, 26875), new NodeId(0, 46), new NodeId(0, 26876).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26875), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26875), new NodeId(0, 47), new NodeId(0, 26871).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode411() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26889),
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
            new NodeId(0, 26889), new NodeId(0, 46), new NodeId(0, 26890).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26889), new NodeId(0, 46), new NodeId(0, 26891).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26889), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26889), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode412() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26892),
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
            new NodeId(0, 26892), new NodeId(0, 46), new NodeId(0, 26893).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26892), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26892), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode413() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26894),
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
            new NodeId(0, 26894), new NodeId(0, 46), new NodeId(0, 26895).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26894), new NodeId(0, 46), new NodeId(0, 26896).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26894), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26894), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode414() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26897),
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
            new NodeId(0, 26897), new NodeId(0, 46), new NodeId(0, 26898).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26897), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26897), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode415() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26899),
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
            new NodeId(0, 26899), new NodeId(0, 46), new NodeId(0, 26900).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26899), new NodeId(0, 46), new NodeId(0, 26901).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26899), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26899), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode416() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26902),
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
            new NodeId(0, 26902), new NodeId(0, 46), new NodeId(0, 26903).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26902), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26902), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode417() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26907),
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
            new NodeId(0, 26907), new NodeId(0, 46), new NodeId(0, 26908).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26907), new NodeId(0, 46), new NodeId(0, 26909).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26907), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26907), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode418() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26910),
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
            new NodeId(0, 26910), new NodeId(0, 46), new NodeId(0, 26911).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26910), new NodeId(0, 46), new NodeId(0, 26912).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26910), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26910), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode419() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26913),
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
            new NodeId(0, 26913), new NodeId(0, 46), new NodeId(0, 26914).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26913), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26913), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode420() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 26915),
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
            new NodeId(0, 26915), new NodeId(0, 46), new NodeId(0, 26916).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26915), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26915), new NodeId(0, 47), new NodeId(0, 26881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode421() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 28005),
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
            new NodeId(0, 28005), new NodeId(0, 46), new NodeId(0, 28006).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 28005), new NodeId(0, 46), new NodeId(0, 28007).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 28005), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 28005), new NodeId(0, 47), new NodeId(0, 26878).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode422() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 28008),
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
        new Reference(new NodeId(0, 28008), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 28008), new NodeId(0, 47), new NodeId(0, 26878).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode423() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 28010),
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
            new NodeId(0, 28010), new NodeId(0, 46), new NodeId(0, 28011).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 28010), new NodeId(0, 46), new NodeId(0, 28012).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 28010), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 28010), new NodeId(0, 47), new NodeId(0, 26878).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode424() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 28013),
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
            new NodeId(0, 28013), new NodeId(0, 46), new NodeId(0, 28014).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 28013), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 28013), new NodeId(0, 47), new NodeId(0, 26878).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode425() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 29880),
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
            new NodeId(0, 29880), new NodeId(0, 46), new NodeId(0, 29881).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 29880), new NodeId(0, 47), new NodeId(0, 29878).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode426() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25229),
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
            new NodeId(0, 25229), new NodeId(0, 46), new NodeId(0, 25230).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25229), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25229), new NodeId(0, 47), new NodeId(0, 25227).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode427() {
    var node =
        new UaMethodNode(
            this.context,
            new NodeId(0, 25231),
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
            new NodeId(0, 25231), new NodeId(0, 46), new NodeId(0, 25232).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25231), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25231), new NodeId(0, 47), new NodeId(0, 25227).expanded(), false));
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
  }
}

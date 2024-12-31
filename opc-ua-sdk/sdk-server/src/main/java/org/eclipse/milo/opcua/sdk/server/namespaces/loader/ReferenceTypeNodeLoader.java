/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;

class ReferenceTypeNodeLoader {
  private final UaNodeContext context;

  private final NodeManager<UaNode> nodeManager;

  ReferenceTypeNodeLoader(UaNodeContext context, NodeManager<UaNode> nodeManager) {
    this.context = context;
    this.nodeManager = nodeManager;
  }

  void loadNode0() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 31),
            new QualifiedName(0, "References"),
            new LocalizedText("", "References"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true,
            null);
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 32),
            new QualifiedName(0, "NonHierarchicalReferences"),
            new LocalizedText("", "NonHierarchicalReferences"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 32), new NodeId(0, 45), new NodeId(0, 31).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 33),
            new QualifiedName(0, "HierarchicalReferences"),
            new LocalizedText("", "HierarchicalReferences"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            false,
            new LocalizedText("", "InverseHierarchicalReferences"));
    node.addReference(
        new Reference(new NodeId(0, 33), new NodeId(0, 45), new NodeId(0, 31).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 34),
            new QualifiedName(0, "HasChild"),
            new LocalizedText("", "HasChild"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            false,
            new LocalizedText("", "ChildOf"));
    node.addReference(
        new Reference(new NodeId(0, 34), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 35),
            new QualifiedName(0, "Organizes"),
            new LocalizedText("", "Organizes"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "OrganizedBy"));
    node.addReference(
        new Reference(new NodeId(0, 35), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 36),
            new QualifiedName(0, "HasEventSource"),
            new LocalizedText("", "HasEventSource"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "EventSourceOf"));
    node.addReference(
        new Reference(new NodeId(0, 36), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 37),
            new QualifiedName(0, "HasModellingRule"),
            new LocalizedText("", "HasModellingRule"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "ModellingRuleOf"));
    node.addReference(
        new Reference(new NodeId(0, 37), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 38),
            new QualifiedName(0, "HasEncoding"),
            new LocalizedText("", "HasEncoding"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "EncodingOf"));
    node.addReference(
        new Reference(new NodeId(0, 38), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 39),
            new QualifiedName(0, "HasDescription"),
            new LocalizedText("", "HasDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "DescriptionOf"));
    node.addReference(
        new Reference(new NodeId(0, 39), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 40),
            new QualifiedName(0, "HasTypeDefinition"),
            new LocalizedText("", "HasTypeDefinition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "TypeDefinitionOf"));
    node.addReference(
        new Reference(new NodeId(0, 40), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 41),
            new QualifiedName(0, "GeneratesEvent"),
            new LocalizedText("", "GeneratesEvent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "GeneratedBy"));
    node.addReference(
        new Reference(new NodeId(0, 41), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 3065),
            new QualifiedName(0, "AlwaysGeneratesEvent"),
            new LocalizedText("", "AlwaysGeneratesEvent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "AlwaysGeneratedBy"));
    node.addReference(
        new Reference(new NodeId(0, 3065), new NodeId(0, 45), new NodeId(0, 41).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 44),
            new QualifiedName(0, "Aggregates"),
            new LocalizedText("", "Aggregates"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            false,
            new LocalizedText("", "AggregatedBy"));
    node.addReference(
        new Reference(new NodeId(0, 44), new NodeId(0, 45), new NodeId(0, 34).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 45),
            new QualifiedName(0, "HasSubtype"),
            new LocalizedText("", "HasSubtype"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "SubtypeOf"));
    node.addReference(
        new Reference(new NodeId(0, 45), new NodeId(0, 45), new NodeId(0, 34).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 46),
            new QualifiedName(0, "HasProperty"),
            new LocalizedText("", "HasProperty"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "PropertyOf"));
    node.addReference(
        new Reference(new NodeId(0, 46), new NodeId(0, 45), new NodeId(0, 44).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 47),
            new QualifiedName(0, "HasComponent"),
            new LocalizedText("", "HasComponent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "ComponentOf"));
    node.addReference(
        new Reference(new NodeId(0, 47), new NodeId(0, 45), new NodeId(0, 44).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 48),
            new QualifiedName(0, "HasNotifier"),
            new LocalizedText("", "HasNotifier"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "NotifierOf"));
    node.addReference(
        new Reference(new NodeId(0, 48), new NodeId(0, 45), new NodeId(0, 36).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 49),
            new QualifiedName(0, "HasOrderedComponent"),
            new LocalizedText("", "HasOrderedComponent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "OrderedComponentOf"));
    node.addReference(
        new Reference(new NodeId(0, 49), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 51),
            new QualifiedName(0, "FromState"),
            new LocalizedText("", "FromState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "ToTransition"));
    node.addReference(
        new Reference(new NodeId(0, 51), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 52),
            new QualifiedName(0, "ToState"),
            new LocalizedText("", "ToState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "FromTransition"));
    node.addReference(
        new Reference(new NodeId(0, 52), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 53),
            new QualifiedName(0, "HasCause"),
            new LocalizedText("", "HasCause"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MayBeCausedBy"));
    node.addReference(
        new Reference(new NodeId(0, 53), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 54),
            new QualifiedName(0, "HasEffect"),
            new LocalizedText("", "HasEffect"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MayBeEffectedBy"));
    node.addReference(
        new Reference(new NodeId(0, 54), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 117),
            new QualifiedName(0, "HasSubStateMachine"),
            new LocalizedText("", "HasSubStateMachine"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "SubStateMachineOf"));
    node.addReference(
        new Reference(new NodeId(0, 117), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 56),
            new QualifiedName(0, "HasHistoricalConfiguration"),
            new LocalizedText("", "HasHistoricalConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "HistoricalConfigurationOf"));
    node.addReference(
        new Reference(new NodeId(0, 56), new NodeId(0, 45), new NodeId(0, 44).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 24136),
            new QualifiedName(0, "HasStructuredComponent"),
            new LocalizedText("", "HasStructuredComponent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsStructuredComponentOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 24136), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 24137),
            new QualifiedName(0, "AssociatedWith"),
            new LocalizedText("", "AssociatedWith"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            true,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 24137), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 129),
            new QualifiedName(0, "HasArgumentDescription"),
            new LocalizedText("", "HasArgumentDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "ArgumentDescriptionOf"));
    node.addReference(
        new Reference(new NodeId(0, 129), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 131),
            new QualifiedName(0, "HasOptionalInputArgumentDescription"),
            new LocalizedText("", "HasOptionalInputArgumentDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "OptionalInputArgumentDescriptionOf"));
    node.addReference(
        new Reference(new NodeId(0, 131), new NodeId(0, 45), new NodeId(0, 129).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 23562),
            new QualifiedName(0, "IsDeprecated"),
            new LocalizedText("", "IsDeprecated"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "Deprecates"));
    node.addReference(
        new Reference(
            new NodeId(0, 23562), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 15112),
            new QualifiedName(0, "HasGuard"),
            new LocalizedText("", "HasGuard"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "GuardOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 15112), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17597),
            new QualifiedName(0, "HasDictionaryEntry"),
            new LocalizedText("", "HasDictionaryEntry"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "DictionaryEntryOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 17597), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17603),
            new QualifiedName(0, "HasInterface"),
            new LocalizedText("", "HasInterface"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "InterfaceOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 17603), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17604),
            new QualifiedName(0, "HasAddIn"),
            new LocalizedText("", "HasAddIn"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "AddInOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 17604), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 9004),
            new QualifiedName(0, "HasTrueSubState"),
            new LocalizedText("", "HasTrueSubState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsTrueSubStateOf"));
    node.addReference(
        new Reference(new NodeId(0, 9004), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 9005),
            new QualifiedName(0, "HasFalseSubState"),
            new LocalizedText("", "HasFalseSubState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsFalseSubStateOf"));
    node.addReference(
        new Reference(new NodeId(0, 9005), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 16361),
            new QualifiedName(0, "HasAlarmSuppressionGroup"),
            new LocalizedText("", "HasAlarmSuppressionGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsAlarmSuppressionGroupOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 16361), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 16362),
            new QualifiedName(0, "AlarmGroupMember"),
            new LocalizedText("", "AlarmGroupMember"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MemberOfAlarmGroup"));
    node.addReference(
        new Reference(
            new NodeId(0, 16362), new NodeId(0, 45), new NodeId(0, 35).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 32059),
            new QualifiedName(0, "AlarmSuppressionGroupMember"),
            new LocalizedText("", "AlarmSuppressionGroupMember"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MemberOfAlarmSuppressionGroup"));
    node.addReference(
        new Reference(
            new NodeId(0, 32059), new NodeId(0, 45), new NodeId(0, 16362).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 9006),
            new QualifiedName(0, "HasCondition"),
            new LocalizedText("", "HasCondition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsConditionOf"));
    node.addReference(
        new Reference(new NodeId(0, 9006), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17276),
            new QualifiedName(0, "HasEffectDisable"),
            new LocalizedText("", "HasEffectDisable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MayBeDisabledBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 17276), new NodeId(0, 45), new NodeId(0, 54).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17983),
            new QualifiedName(0, "HasEffectEnable"),
            new LocalizedText("", "HasEffectEnable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MayBeEnabledBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 17983), new NodeId(0, 45), new NodeId(0, 54).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17984),
            new QualifiedName(0, "HasEffectSuppressed"),
            new LocalizedText("", "HasEffectSuppressed"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MayBeSuppressedBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 17984), new NodeId(0, 45), new NodeId(0, 54).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 17985),
            new QualifiedName(0, "HasEffectUnsuppressed"),
            new LocalizedText("", "HasEffectUnsuppressed"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "MayBeUnsuppressedBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 17985), new NodeId(0, 45), new NodeId(0, 54).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25345),
            new QualifiedName(0, "HasPushedSecurityGroup"),
            new LocalizedText("", "HasPushedSecurityGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "HasPushTarget"));
    node.addReference(
        new Reference(
            new NodeId(0, 25345), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 14476),
            new QualifiedName(0, "HasPubSubConnection"),
            new LocalizedText("", "HasPubSubConnection"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "PubSubConnectionOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 14476), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 14936),
            new QualifiedName(0, "DataSetToWriter"),
            new LocalizedText("", "DataSetToWriter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "WriterToDataSet"));
    node.addReference(
        new Reference(
            new NodeId(0, 14936), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 15296),
            new QualifiedName(0, "HasDataSetWriter"),
            new LocalizedText("", "HasDataSetWriter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsWriterInGroup"));
    node.addReference(
        new Reference(
            new NodeId(0, 15296), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 18804),
            new QualifiedName(0, "HasWriterGroup"),
            new LocalizedText("", "HasWriterGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsWriterGroupOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 18804), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 15297),
            new QualifiedName(0, "HasDataSetReader"),
            new LocalizedText("", "HasDataSetReader"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsReaderInGroup"));
    node.addReference(
        new Reference(
            new NodeId(0, 15297), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 18805),
            new QualifiedName(0, "HasReaderGroup"),
            new LocalizedText("", "HasReaderGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsReaderGroupOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 18805), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 23469),
            new QualifiedName(0, "AliasFor"),
            new LocalizedText("", "AliasFor"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "HasAlias"));
    node.addReference(
        new Reference(
            new NodeId(0, 23469), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25237),
            new QualifiedName(0, "UsesPriorityMappingTable"),
            new LocalizedText("", "UsesPriorityMappingTable"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "UsedByNetworkInterface"));
    node.addReference(
        new Reference(
            new NodeId(0, 25237), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25238),
            new QualifiedName(0, "HasLowerLayerInterface"),
            new LocalizedText("", "HasLowerLayerInterface"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "HasHigherLayerInterface"));
    node.addReference(
        new Reference(
            new NodeId(0, 25238), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25253),
            new QualifiedName(0, "IsExecutableOn"),
            new LocalizedText("", "IsExecutableOn"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "CanExecute"));
    node.addReference(
        new Reference(
            new NodeId(0, 25253), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25254),
            new QualifiedName(0, "Controls"),
            new LocalizedText("", "Controls"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsControlledBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 25254), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25255),
            new QualifiedName(0, "Utilizes"),
            new LocalizedText("", "Utilizes"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsUtilizedBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 25255), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25265),
            new QualifiedName(0, "IsExecutingOn"),
            new LocalizedText("", "IsExecutingOn"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "Executes"));
    node.addReference(
        new Reference(
            new NodeId(0, 25265), new NodeId(0, 45), new NodeId(0, 25255).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25256),
            new QualifiedName(0, "Requires"),
            new LocalizedText("", "Requires"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "IsRequiredBy"));
    node.addReference(
        new Reference(
            new NodeId(0, 25256), new NodeId(0, 45), new NodeId(0, 33).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25257),
            new QualifiedName(0, "IsPhysicallyConnectedTo"),
            new LocalizedText("", "IsPhysicallyConnectedTo"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            true,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 25257), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25258),
            new QualifiedName(0, "RepresentsSameEntityAs"),
            new LocalizedText("", "RepresentsSameEntityAs"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            true,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 25258), new NodeId(0, 45), new NodeId(0, 32).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25259),
            new QualifiedName(0, "RepresentsSameHardwareAs"),
            new LocalizedText("", "RepresentsSameHardwareAs"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            true,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 25259), new NodeId(0, 45), new NodeId(0, 25258).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25260),
            new QualifiedName(0, "RepresentsSameFunctionalityAs"),
            new LocalizedText("", "RepresentsSameFunctionalityAs"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            true,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 25260), new NodeId(0, 45), new NodeId(0, 25258).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25261),
            new QualifiedName(0, "IsHostedBy"),
            new LocalizedText("", "IsHostedBy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "Hosts"));
    node.addReference(
        new Reference(
            new NodeId(0, 25261), new NodeId(0, 45), new NodeId(0, 25255).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25262),
            new QualifiedName(0, "HasPhysicalComponent"),
            new LocalizedText("", "HasPhysicalComponent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "PhysicalComponentOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 25262), new NodeId(0, 45), new NodeId(0, 47).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25263),
            new QualifiedName(0, "HasContainedComponent"),
            new LocalizedText("", "HasContainedComponent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "ContainedComponentOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 25263), new NodeId(0, 45), new NodeId(0, 25262).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 25264),
            new QualifiedName(0, "HasAttachedComponent"),
            new LocalizedText("", "HasAttachedComponent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "AttachedComponentOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 25264), new NodeId(0, 45), new NodeId(0, 25262).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new UaReferenceTypeNode(
            this.context,
            new NodeId(0, 32679),
            new QualifiedName(0, "HasReferenceDescription"),
            new LocalizedText("", "HasReferenceDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            false,
            new LocalizedText("", "ReferenceDescriptionOf"));
    node.addReference(
        new Reference(
            new NodeId(0, 32679), new NodeId(0, 45), new NodeId(0, 34).expanded(), false));
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
  }
}

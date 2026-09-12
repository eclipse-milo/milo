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
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;

class VariableTypeNodeLoader {
  private final UaNodeContext context;

  private final NodeManager<UaNode> nodeManager;

  VariableTypeNodeLoader(UaNodeContext context, NodeManager<UaNode> nodeManager) {
    this.context = context;
    this.nodeManager = nodeManager;
  }

  void loadNode0() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=62"),
            new QualifiedName(0, "BaseVariableType"),
            new LocalizedText("", "BaseVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -2,
            null,
            true);
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=63"),
            new QualifiedName(0, "BaseDataVariableType"),
            new LocalizedText("", "BaseDataVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=63"), NodeId.parse("i=45"), NodeId.parse("i=62").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=68"),
            new QualifiedName(0, "PropertyType"),
            new LocalizedText("", "PropertyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=68"), NodeId.parse("i=45"), NodeId.parse("i=62").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=69"),
            new QualifiedName(0, "DataTypeDescriptionType"),
            new LocalizedText("", "DataTypeDescriptionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=12"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=69"), NodeId.parse("i=46"), NodeId.parse("i=104").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=69"), NodeId.parse("i=46"), NodeId.parse("i=105").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=69"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=72"),
            new QualifiedName(0, "DataTypeDictionaryType"),
            new LocalizedText("", "DataTypeDictionaryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=15"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=72"), NodeId.parse("i=46"), NodeId.parse("i=106").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=72"), NodeId.parse("i=46"), NodeId.parse("i=107").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=72"), NodeId.parse("i=46"), NodeId.parse("i=15001").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=72"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2137"),
            new QualifiedName(0, "ServerVendorCapabilityType"),
            new LocalizedText("", "ServerVendorCapabilityType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -1,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2137"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2138"),
            new QualifiedName(0, "ServerStatusType"),
            new LocalizedText("", "ServerStatusType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=862"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=47"), NodeId.parse("i=2139").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=47"), NodeId.parse("i=2140").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=47"), NodeId.parse("i=2141").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=47"), NodeId.parse("i=2142").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=47"), NodeId.parse("i=2752").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=47"), NodeId.parse("i=2753").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2138"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=3051"),
            new QualifiedName(0, "BuildInfoType"),
            new LocalizedText("", "BuildInfoType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=338"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=47"), NodeId.parse("i=3052").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=47"), NodeId.parse("i=3053").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=47"), NodeId.parse("i=3054").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=47"), NodeId.parse("i=3055").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=47"), NodeId.parse("i=3056").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=47"), NodeId.parse("i=3057").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3051"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2150"),
            new QualifiedName(0, "ServerDiagnosticsSummaryType"),
            new LocalizedText("", "ServerDiagnosticsSummaryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=859"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2151").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2152").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2153").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2154").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2155").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2156").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2157").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2159").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2160").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2161").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2162").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=47"), NodeId.parse("i=2163").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2150"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2164"),
            new QualifiedName(0, "SamplingIntervalDiagnosticsArrayType"),
            new LocalizedText("", "SamplingIntervalDiagnosticsArrayType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=856"),
            1,
            new UInteger[] {UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2164"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12779").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2164"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2165"),
            new QualifiedName(0, "SamplingIntervalDiagnosticsType"),
            new LocalizedText("", "SamplingIntervalDiagnosticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=856"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2165"), NodeId.parse("i=47"), NodeId.parse("i=2166").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2165"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11697").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2165"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11698").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2165"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11699").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2165"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2171"),
            new QualifiedName(0, "SubscriptionDiagnosticsArrayType"),
            new LocalizedText("", "SubscriptionDiagnosticsArrayType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=874"),
            1,
            new UInteger[] {UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2171"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12784").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2171"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2172"),
            new QualifiedName(0, "SubscriptionDiagnosticsType"),
            new LocalizedText("", "SubscriptionDiagnosticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=874"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2173").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2174").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2175").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2176").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2177").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8888").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2179").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2180").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2181").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2182").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2183").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2184").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2185").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2186").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2187").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2188").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2189").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2190").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2191").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2998").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=2193").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8889").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8890").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8891").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8892").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8893").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8894").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8895").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8896").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8897").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=47"), NodeId.parse("i=8902").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2172"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2196"),
            new QualifiedName(0, "SessionDiagnosticsArrayType"),
            new LocalizedText("", "SessionDiagnosticsArrayType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=865"),
            1,
            new UInteger[] {UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2196"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12816").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2196"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2197"),
            new QualifiedName(0, "SessionDiagnosticsVariableType"),
            new LocalizedText("", "SessionDiagnosticsVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=865"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2198").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2199").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2200").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2201").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2202").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2203").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2204").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=3050").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2205").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2206").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2207").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2208").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2209").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=8900").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11892").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2217").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2218").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2219").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2220").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2221").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2222").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2223").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2224").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2225").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2226").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2227").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2228").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2229").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2230").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2231").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2232").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2233").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2234").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2235").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2236").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2237").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2238").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2239").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2240").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2241").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2242").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2730").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=47"), NodeId.parse("i=2731").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2197"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2243"),
            new QualifiedName(0, "SessionSecurityDiagnosticsArrayType"),
            new LocalizedText("", "SessionSecurityDiagnosticsArrayType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=868"),
            1,
            new UInteger[] {UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2243"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12860").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2243"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2244"),
            new QualifiedName(0, "SessionSecurityDiagnosticsType"),
            new LocalizedText("", "SessionSecurityDiagnosticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=868"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2245").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2246").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2247").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2248").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2249").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2250").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2251").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=2252").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=47"), NodeId.parse("i=3058").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2244"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=11487"),
            new QualifiedName(0, "OptionSetType"),
            new LocalizedText("", "OptionSetType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=11487"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11488").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11487"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11701").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11487"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=16309"),
            new QualifiedName(0, "SelectionListType"),
            new LocalizedText("", "SelectionListType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=16309"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17632").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16309"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17633").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16309"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16312").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16309"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17986"),
            new QualifiedName(0, "AudioVariableType"),
            new LocalizedText("", "AudioVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=16307"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17986"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17988").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17986"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17989").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17986"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17990").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17986"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2755"),
            new QualifiedName(0, "StateVariableType"),
            new LocalizedText("", "StateVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2755"), NodeId.parse("i=46"), NodeId.parse("i=2756").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2755"), NodeId.parse("i=46"), NodeId.parse("i=2757").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2755"), NodeId.parse("i=46"), NodeId.parse("i=2758").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2755"), NodeId.parse("i=46"), NodeId.parse("i=2759").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2755"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2762"),
            new QualifiedName(0, "TransitionVariableType"),
            new LocalizedText("", "TransitionVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2762"), NodeId.parse("i=46"), NodeId.parse("i=2763").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2762"), NodeId.parse("i=46"), NodeId.parse("i=2764").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2762"), NodeId.parse("i=46"), NodeId.parse("i=2765").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2762"), NodeId.parse("i=46"), NodeId.parse("i=2766").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2762"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11456").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2762"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2760"),
            new QualifiedName(0, "FiniteStateVariableType"),
            new LocalizedText("", "FiniteStateVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2760"), NodeId.parse("i=46"), NodeId.parse("i=2761").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2760"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2755").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2767"),
            new QualifiedName(0, "FiniteTransitionVariableType"),
            new LocalizedText("", "FiniteTransitionVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2767"), NodeId.parse("i=46"), NodeId.parse("i=2768").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2767"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2762").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=15113"),
            new QualifiedName(0, "GuardVariableType"),
            new LocalizedText("", "GuardVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15113"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=15128"),
            new QualifiedName(0, "ExpressionGuardVariableType"),
            new LocalizedText("", "ExpressionGuardVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15128"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15129").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15128"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15113").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=15317"),
            new QualifiedName(0, "ElseGuardVariableType"),
            new LocalizedText("", "ElseGuardVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15317"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15113").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17709"),
            new QualifiedName(0, "RationalNumberType"),
            new LocalizedText("", "RationalNumberType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18806"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17709"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17712").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17709"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17713").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17709"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17714"),
            new QualifiedName(0, "VectorType"),
            new LocalizedText("", "VectorType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18807"),
            -1,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=17714"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17715").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17714"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17716"),
            new QualifiedName(0, "3DVectorType"),
            new LocalizedText("", "3DVectorType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18808"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17716"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18769").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17716"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18770").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17716"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18771").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17716"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17714").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=18772"),
            new QualifiedName(0, "CartesianCoordinatesType"),
            new LocalizedText("", "CartesianCoordinatesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18809"),
            -1,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18772"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18773").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18772"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=18774"),
            new QualifiedName(0, "3DCartesianCoordinatesType"),
            new LocalizedText("", "3DCartesianCoordinatesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18810"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=18774"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18776").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18774"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18777").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18774"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18778").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18774"),
            NodeId.parse("i=45"),
            NodeId.parse("i=18772").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=18779"),
            new QualifiedName(0, "OrientationType"),
            new LocalizedText("", "OrientationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18811"),
            -1,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18779"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18780").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18779"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=18781"),
            new QualifiedName(0, "3DOrientationType"),
            new LocalizedText("", "3DOrientationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18812"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=18781"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18783").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18781"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18784").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18781"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18785").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18781"),
            NodeId.parse("i=45"),
            NodeId.parse("i=18779").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=18786"),
            new QualifiedName(0, "FrameType"),
            new LocalizedText("", "FrameType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18813"),
            -1,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=18786"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18801").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18786"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18787").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18786"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18788").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18786"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18789").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18786"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18790").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18786"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=18791"),
            new QualifiedName(0, "3DFrameType"),
            new LocalizedText("", "3DFrameType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=18814"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=18791"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18796").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18791"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18792").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18791"),
            NodeId.parse("i=45"),
            NodeId.parse("i=18786").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=32431"),
            new QualifiedName(0, "BitFieldType"),
            new LocalizedText("", "BitFieldType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=28"),
            -1,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=32431"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32432").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32431"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32433").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32431"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15014").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32431"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2365"),
            new QualifiedName(0, "DataItemType"),
            new LocalizedText("", "DataItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2365"), NodeId.parse("i=46"), NodeId.parse("i=2366").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2365"), NodeId.parse("i=46"), NodeId.parse("i=2367").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2365"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=15318"),
            new QualifiedName(0, "BaseAnalogType"),
            new LocalizedText("", "BaseAnalogType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17567").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23904").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17568").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23905").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17569").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15318"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2365").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2368"),
            new QualifiedName(0, "AnalogItemType"),
            new LocalizedText("", "AnalogItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2368"), NodeId.parse("i=46"), NodeId.parse("i=2369").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2368"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15318").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=23906"),
            new QualifiedName(0, "AnalogNumberItemType"),
            new LocalizedText("", "AnalogNumberItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=23906"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23907").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23906"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2368").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17497"),
            new QualifiedName(0, "AnalogUnitType"),
            new LocalizedText("", "AnalogUnitType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17497"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17502").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17497"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15318").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17570"),
            new QualifiedName(0, "AnalogUnitRangeType"),
            new LocalizedText("", "AnalogUnitRangeType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17570"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17575").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17570"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2368").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=23918"),
            new QualifiedName(0, "AnalogNumberUnitRangeType"),
            new LocalizedText("", "AnalogNumberUnitRangeType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=23918"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23927").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23918"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17570").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2372"),
            new QualifiedName(0, "DiscreteItemType"),
            new LocalizedText("", "DiscreteItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -2,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2372"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2365").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2373"),
            new QualifiedName(0, "TwoStateDiscreteType"),
            new LocalizedText("", "TwoStateDiscreteType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=1"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2373"), NodeId.parse("i=46"), NodeId.parse("i=2374").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2373"), NodeId.parse("i=46"), NodeId.parse("i=2375").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2373"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2372").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2376"),
            new QualifiedName(0, "MultiStateDiscreteType"),
            new LocalizedText("", "MultiStateDiscreteType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=28"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2376"), NodeId.parse("i=46"), NodeId.parse("i=2377").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2376"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2372").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=11238"),
            new QualifiedName(0, "MultiStateValueDiscreteType"),
            new LocalizedText("", "MultiStateValueDiscreteType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=11238"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11241").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11238"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11461").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11238"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2372").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=12021"),
            new QualifiedName(0, "ArrayItemType"),
            new LocalizedText("", "ArrayItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            0,
            null,
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=12021"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12024").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12021"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12025").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12021"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12026").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12021"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12027").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12021"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12028").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12021"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2365").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=12029"),
            new QualifiedName(0, "YArrayItemType"),
            new LocalizedText("", "YArrayItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            1,
            new UInteger[] {UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=12029"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12037").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12029"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12021").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=12038"),
            new QualifiedName(0, "XYArrayItemType"),
            new LocalizedText("", "XYArrayItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=12080"),
            1,
            new UInteger[] {UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=12038"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12046").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12038"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12021").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=12047"),
            new QualifiedName(0, "ImageItemType"),
            new LocalizedText("", "ImageItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            2,
            new UInteger[] {UInteger.valueOf(0), UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=12047"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12055").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12047"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12056").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12047"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12021").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=12057"),
            new QualifiedName(0, "CubeItemType"),
            new LocalizedText("", "CubeItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            3,
            new UInteger[] {UInteger.valueOf(0), UInteger.valueOf(0), UInteger.valueOf(0)},
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=12057"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12065").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12057"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12066").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12057"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12067").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12057"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12021").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=12068"),
            new QualifiedName(0, "NDimensionArrayItemType"),
            new LocalizedText("", "NDimensionArrayItemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            0,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=12068"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12076").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12068"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12021").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=8995"),
            new QualifiedName(0, "TwoStateVariableType"),
            new LocalizedText("", "TwoStateVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=21"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=8995"), NodeId.parse("i=46"), NodeId.parse("i=8996").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8995"), NodeId.parse("i=46"), NodeId.parse("i=9000").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8995"), NodeId.parse("i=46"), NodeId.parse("i=9001").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8995"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11110").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8995"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11111").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8995"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2755").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=9002"),
            new QualifiedName(0, "ConditionVariableType"),
            new LocalizedText("", "ConditionVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24"),
            -2,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=9002"), NodeId.parse("i=46"), NodeId.parse("i=9003").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9002"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=17277"),
            new QualifiedName(0, "AlarmRateVariableType"),
            new LocalizedText("", "AlarmRateVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=11"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17277"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17278").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17277"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=32244"),
            new QualifiedName(0, "AlarmStateVariableType"),
            new LocalizedText("", "AlarmStateVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=32251"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32245").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32246").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32247").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32248").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32249").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32250").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32244"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=2380"),
            new QualifiedName(0, "ProgramDiagnosticType"),
            new LocalizedText("", "ProgramDiagnosticType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=894"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2381").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2382").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2383").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2384").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2385").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2386").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2387").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2388").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2389").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=46"), NodeId.parse("i=2390").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2380"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=15383"),
            new QualifiedName(0, "ProgramDiagnostic2Type"),
            new LocalizedText("", "ProgramDiagnostic2Type"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=24033"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15384").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15385").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15386").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15387").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15388").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15389").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15390").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15391").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15392").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15393").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15394").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15395").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15383"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=19725"),
            new QualifiedName(0, "PubSubDiagnosticsCounterType"),
            new LocalizedText("", "PubSubDiagnosticsCounterType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=7"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19726").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19727").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19728").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19729").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19725"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=19077"),
            new QualifiedName(0, "MultiStateDictionaryEntryDiscreteBaseType"),
            new LocalizedText("", "MultiStateDictionaryEntryDiscreteBaseType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19077"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19082").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19077"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19083").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19077"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11238").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=19084"),
            new QualifiedName(0, "MultiStateDictionaryEntryDiscreteType"),
            new LocalizedText("", "MultiStateDictionaryEntryDiscreteType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=26"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19084"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19090").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19084"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19077").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaVariableTypeNode(
            this.context,
            NodeId.parse("i=32657"),
            new QualifiedName(0, "ReferenceDescriptionVariableType"),
            new LocalizedText("", "ReferenceDescriptionVariableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            new DataValue(Variant.NULL_VALUE),
            NodeId.parse("i=32659"),
            -1,
            null,
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=32657"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32658").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32657"), NodeId.parse("i=45"), NodeId.parse("i=63").expanded(), false));
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
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.junit.jupiter.api.Test;

/** Explicit Condition property construction with strict generated member access. */
class ConditionPropertyInitializationTest extends AbstractClientServerTest {

  // The declaration has no modelling rule; the instantiator leaves it absent and the generated
  // setter must not create it. Adoption supplies the behavior's explicit default instead.
  @Test
  void adoptionCreatesMissingSupportsFilteredRetainWithExactMetadata() throws Exception {
    AlarmConditionTypeNode node = bareAlarm("missing");
    UaRuntimeException missing =
        assertThrows(UaRuntimeException.class, () -> node.setSupportsFilteredRetain(false));
    assertEquals(StatusCodes.Bad_NotFound, missing.getStatusCode().getValue());

    adopt(node);

    assertFalse(node.getSupportsFilteredRetain());
    PropertyTypeNode property = node.getSupportsFilteredRetainNode();
    assertEquals(NodeIds.Boolean, property.getDataType());
    assertEquals(-1, property.getValueRank());
    assertNull(property.getArrayDimensions());
    assertEquals(new QualifiedName(0, "SupportsFilteredRetain"), property.getBrowseName());
    assertEquals(NodeIds.PropertyType, property.getTypeDefinitionNode().getNodeId());
    assertTrue(
        property.getReferences().stream()
            .anyMatch(
                r ->
                    !r.isForward()
                        && r.getReferenceTypeId().equals(NodeIds.HasProperty)
                        && r.getTargetNodeId().equals(node.getNodeId().expanded())));
  }

  // Completing a loaded instance must retain an application's existing value and node identity.
  @Test
  void adoptionPreservesExistingTrue() throws Exception {
    AlarmConditionTypeNode node = bareAlarm("true");
    node.setProperty(ConditionType.SUPPORTS_FILTERED_RETAIN, true);
    PropertyTypeNode property = node.getSupportsFilteredRetainNode();

    adopt(node);

    assertSame(property, node.getSupportsFilteredRetainNode());
    assertTrue(node.getSupportsFilteredRetain());
  }

  // A present but unset property receives the default without replacement.
  @Test
  void adoptionInitializesPresentNull() throws Exception {
    AlarmConditionTypeNode node = bareAlarm("null");
    node.setProperty(ConditionType.SUPPORTS_FILTERED_RETAIN, null);
    PropertyTypeNode property = node.getSupportsFilteredRetainNode();

    adopt(node);

    assertSame(property, node.getSupportsFilteredRetainNode());
    assertFalse(node.getSupportsFilteredRetain());
  }

  // A malformed candidate is not absence and must not be replaced by a default PropertyType.
  @Test
  void adoptionPreservesMalformedCandidateAndLookupFailure() throws Exception {
    AlarmConditionTypeNode node = bareAlarm("malformed");
    UaObjectNode wrong =
        UaObjectNode.builder(testNamespace.getNodeContext())
            .setNodeId(newNodeId("malformed-property"))
            .setBrowseName(new QualifiedName(0, "SupportsFilteredRetain"))
            .setDisplayName(
                org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText.english("wrong"))
            .build();
    testNamespace.getNodeManager().addNode(wrong);
    node.addReference(
        new Reference(node.getNodeId(), NodeIds.HasProperty, wrong.getNodeId().expanded(), true));

    UaRuntimeException failure = assertThrows(UaRuntimeException.class, () -> adopt(node));

    assertEquals(StatusCodes.Bad_NodeClassInvalid, failure.getStatusCode().getValue());
    assertSame(wrong, testNamespace.getNodeManager().getNode(wrong.getNodeId()).orElseThrow());
    assertThrows(UaRuntimeException.class, node::getSupportsFilteredRetainNode);
  }

  // Attached instances can omit optional timestamps; behavior still initializes them explicitly.
  @Test
  void attachmentCreatesOptionalTransitionTimestampWithoutWeakeningSetter() throws Exception {
    AlarmConditionTypeNode node = bareAlarm("timestamp");
    var state = node.getEnabledStateNode();
    assertNull(state.getTransitionTimeNode());
    UaRuntimeException missing =
        assertThrows(UaRuntimeException.class, () -> state.setTransitionTime(DateTime.NULL_VALUE));
    assertEquals(StatusCodes.Bad_NotFound, missing.getStatusCode().getValue());

    AlarmCondition.attach(node);

    assertEquals(NodeIds.UtcTime, state.getTransitionTimeNode().getDataType());
    assertEquals(-1, state.getTransitionTimeNode().getValueRank());
    assertTrue(state.getTransitionTime().getJavaTime() > 0);
  }

  private AlarmConditionTypeNode bareAlarm(String name) throws Exception {
    return server
        .getNodeInstantiator()
        .instantiate(
            InstantiationRequest.of(AlarmConditionTypeNode.class, NodeIds.AlarmConditionType)
                .nodeId(newNodeId("ConditionPropertyInitializationTest/" + name))
                .browseName(newQualifiedName(name))
                .target(testNamespace.getNodeManager())
                .build())
        .root();
  }

  private void adopt(AlarmConditionTypeNode node) throws Exception {
    AlarmCondition.adopt(testNamespace.getNodeContext(), node.getNodeId(), builder -> {});
  }
}

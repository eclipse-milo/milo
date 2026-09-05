/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import static org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeFixtures.path;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AttributeSnapshotCompatibilityTest {
  // A standard outer codec does not guarantee that nested application values have standard
  // codecs. Such values retain the documented opaque, caller-immutable snapshot contract.
  @Test
  void standardStructureContainingCustomValueCanStillCompile() throws Exception {
    var value = new KeyValuePair(new QualifiedName(0, "custom"), new Variant(new OpaqueValue(42)));
    var fx = TypeFixtures.create();
    var type = fx.addObjectType("CustomValue", NodeIds.BaseObjectType);
    var declaration =
        fx.addVariableDeclaration(
            type, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    declaration.setDataType(NodeIds.KeyValuePair);
    declaration.setValue(new DataValue(new Variant(value)));
    AttributeSnapshot snapshot = AttributeSnapshot.of(declaration);
    assertSame(value, snapshot.value().getValue().getValue());
    assertEquals(snapshot.contentHash(), AttributeSnapshot.of(declaration).contentHash());
    var model = fx.compiler().compile(type.getNodeId());
    assertSame(
        value, model.get(path("V")).orElseThrow().attributes().value().getValue().getValue());
  }

  // Empty enum/structure matrices have a known builtin type but no element from which to derive
  // a namespace-qualified DataType id. Missing metadata must not prevent snapshots or copying.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void emptyMatricesKeepDimensionsWithoutInventingADataTypeId(boolean enumeration) {
    Object elements = enumeration ? new ApplicationType[0] : new Argument[0];
    var matrix = new Matrix(elements, new int[] {0, 0});
    var snapshot =
        AttributeSnapshot.builder()
            .put(AttributeId.Value, new DataValue(new Variant(matrix)))
            .build();
    var copy = (Matrix) snapshot.value().getValue().getValue();
    assertNotSame(elements, copy.getElements());
    assertArrayEquals(new int[] {0, 0}, copy.getDimensions());
    assertTrue(copy.getDataTypeId().isEmpty());
    assertEquals(matrix.getDataType(), copy.getDataType());
    String originalContent = snapshot.contentHash();
    matrix.getDimensions()[0] = 7;
    copy.getDimensions()[1] = 9;
    assertArrayEquals(
        new int[] {0, 0}, ((Matrix) snapshot.value().getValue().getValue()).getDimensions());
    assertEquals(originalContent, snapshot.contentHash());
  }

  // Binary bodies are common inside standard structures. Their fingerprint should scale with
  // payload bytes, while retaining content, length, element-boundary, and Java-type distinctions.
  @Test
  void binaryFingerprintsStayCompactAndDistinguishContentAndFraming() {
    byte[] bytes = new byte[4096];
    String original = fingerprint(ByteString.of(bytes));
    assertTrue(original.length() < bytes.length * 3, "binary payload should have compact framing");
    bytes[bytes.length - 1] = 1;
    assertNotEquals(original, fingerprint(ByteString.of(bytes)));
    assertNotEquals(fingerprint(new byte[] {1, 23}), fingerprint(new byte[] {12, 3}));
    assertNotEquals(fingerprint(new byte[] {1}), fingerprint(new byte[] {0, 1}));
    assertNotEquals(fingerprint(new byte[] {1}), fingerprint(new Byte[] {1}));
    assertNotEquals(fingerprint(new byte[] {1}), fingerprint(ByteString.of(new byte[] {1})));
    assertNotEquals(fingerprint(ByteString.NULL_VALUE), fingerprint(ByteString.of(new byte[0])));
  }

  private static String fingerprint(Object value) {
    return AttributeSnapshot.builder().put(AttributeId.Value, value).build().contentHash();
  }

  private record OpaqueValue(int value) implements UaStructuredType {
    @Override
    public ExpandedNodeId getTypeId() {
      return ExpandedNodeId.parse("nsu=urn:custom;i=1");
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.parse("nsu=urn:custom;i=2");
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }
  }
}

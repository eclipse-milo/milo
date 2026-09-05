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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

class AttributeSnapshotCompatibilityTest {
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
}

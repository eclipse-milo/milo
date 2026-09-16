/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class XmlNamespaceEncodingContextTest {

  // Metadata loaded after a view is created cannot change an encoding operation's namespaces.
  // Other context services remain live so session namespace-table updates are still visible.
  @Test
  void mappingSnapshotSharesContextServicesWithoutMutatingTheOriginal() {
    EncodingContext original = new DefaultEncodingContext();
    var metadata = new HashMap<>(Map.of("urn:model", "urn:schema"));
    EncodingContext mapped = original.withXmlNamespaceUris(metadata);
    metadata.put("urn:model", "urn:changed");
    original.getNamespaceTable().add("urn:model");

    assertEquals(Map.of("urn:model", "urn:schema"), mapped.getXmlNamespaceUris());
    assertTrue(original.getXmlNamespaceUris().isEmpty());
    assertThrows(
        UnsupportedOperationException.class,
        () -> mapped.getXmlNamespaceUris().put("urn:model", "urn:changed"));
    assertSame(original.getNamespaceTable(), mapped.getNamespaceTable());
    assertSame(original.getServerTable(), mapped.getServerTable());
    assertSame(original.getDataTypeManager(), mapped.getDataTypeManager());
    assertSame(original.getEncodingManager(), mapped.getEncodingManager());
    assertSame(original.getEncodingLimits(), mapped.getEncodingLimits());
  }

  // Combining multiple models must preserve earlier metadata, allow duplicates that agree, and
  // fail explicitly on conflicts instead of making XML output depend on registration order.
  @Test
  void composingMappingsAcceptsAgreementAndRejectsConflicts() {
    EncodingContext first =
        new DefaultEncodingContext().withXmlNamespaceUris(Map.of("urn:model", "urn:schema"));
    EncodingContext combined =
        first.withXmlNamespaceUris(
            Map.of("urn:model", "urn:schema", "urn:other", "urn:other-schema"));
    assertEquals(
        Map.of("urn:model", "urn:schema", "urn:other", "urn:other-schema"),
        combined.getXmlNamespaceUris());
    assertThrows(
        IllegalArgumentException.class,
        () -> first.withXmlNamespaceUris(Map.of("urn:model", "urn:conflict")));
    assertEquals(Map.of("urn:model", "urn:schema"), first.getXmlNamespaceUris());
  }

  // The UA namespace is fixed by the wire format and cannot be reassigned by model metadata.
  @Test
  void namespaceZeroOnlyAcceptsTheStandardXmlNamespace() {
    EncodingContext original = new DefaultEncodingContext();
    original.withXmlNamespaceUris(Map.of(Namespaces.OPC_UA, Namespaces.OPC_UA_XSD));
    assertThrows(
        IllegalArgumentException.class,
        () -> original.withXmlNamespaceUris(Map.of(Namespaces.OPC_UA, "urn:wrong")));
  }

  @ParameterizedTest
  @CsvSource({"'',urn:schema", "urn:model,''", "'   ',urn:schema", "urn:model,'   '"})
  void blankNamespaceMappingsAreRejected(String model, String schema) {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DefaultEncodingContext().withXmlNamespaceUris(Map.of(model, schema)));
  }

  @Test
  void nullNamespaceMappingsAreRejected() {
    EncodingContext original = new DefaultEncodingContext();
    var nullKey = new HashMap<String, String>();
    nullKey.put(null, "urn:schema");
    var nullValue = new HashMap<String, String>();
    nullValue.put("urn:model", null);
    assertThrows(NullPointerException.class, () -> original.withXmlNamespaceUris(null));
    assertThrows(NullPointerException.class, () -> original.withXmlNamespaceUris(nullKey));
    assertThrows(NullPointerException.class, () -> original.withXmlNamespaceUris(nullValue));
  }
}

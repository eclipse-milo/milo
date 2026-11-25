/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.junit.jupiter.api.Test;

class LazyClientDataTypeTreeSeedTest {

  private static final String STRUCTURED_PACKAGE =
      "org.eclipse.milo.opcua.stack.core.types.structured";

  /**
   * A seeded type is never read from the server, so its definition must match the generated
   * structure class exactly. {@code DynamicStructCodec} encodes and decodes fields in definition
   * order, and OPC 10000-3, 8.48 requires a subtype's fields to begin with its base type's fields.
   */
  @Test
  void seededStructureDefinitionsMatchGeneratedDefinitions() throws Exception {
    var namespaceTable = new NamespaceTable();

    List<DataType> seededStructures = new ArrayList<>();
    LazyClientDataTypeTreeSeed.createSeedTree()
        .traverse(
            dataType -> {
              if (dataType.getDataTypeDefinition() instanceof StructureDefinition) {
                seededStructures.add(dataType);
              }
            });

    assertFalse(seededStructures.isEmpty());

    List<String> mismatched = new ArrayList<>();

    for (DataType dataType : seededStructures) {
      StructureDefinition expected = generatedDefinition(dataType, namespaceTable);

      if (!expected.equals(dataType.getDataTypeDefinition())) {
        mismatched.add(dataType.getBrowseName().name());
      }
    }

    assertEquals(List.of(), mismatched);
  }

  /**
   * Calls the static {@code definition(NamespaceTable)} method of the generated structure class
   * named after {@code dataType}'s browse name. Java names cannot start with a digit, so the
   * generated classes spell a leading "3D" as "ThreeD".
   */
  private static StructureDefinition generatedDefinition(
      DataType dataType, NamespaceTable namespaceTable) throws ReflectiveOperationException {

    String browseName = dataType.getBrowseName().name();
    String className =
        browseName.startsWith("3D") ? "ThreeD" + browseName.substring(2) : browseName;

    Class<?> structureClass = Class.forName(STRUCTURED_PACKAGE + "." + className);

    return (StructureDefinition)
        structureClass.getMethod("definition", NamespaceTable.class).invoke(null, namespaceTable);
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.xml.bind.JAXBException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opcfoundation.opcua.binaryschema.StructuredType;

class BsdParserTest {

  @Test
  void parseOpcUaTypeDictionary() throws JAXBException {
    var dictionary = BsdParser.parseBuiltinTypeDictionary();
    assertEquals("http://opcfoundation.org/UA/", dictionary.getTargetNamespace());
    var argument =
        dictionary.getOpaqueTypeOrEnumeratedTypeOrStructuredType().stream()
            .filter(type -> type.getName().equals("Argument"))
            .map(type -> assertInstanceOf(StructuredType.class, type))
            .findFirst()
            .orElseThrow();
    assertEquals(
        List.of(
            "Name",
            "DataType",
            "ValueRank",
            "NoOfArrayDimensions",
            "ArrayDimensions",
            "Description"),
        argument.getField().stream().map(field -> field.getName()).toList());
    assertEquals("NoOfArrayDimensions", argument.getField().get(4).getLengthField());
  }
}

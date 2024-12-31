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

import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;

class BsdGeneratorTest {

  @Test
  void generate() throws JAXBException {
    TypeDictionary typeDictionary = BsdParser.parseBuiltinTypeDictionary();

    var outputStream = new ByteArrayOutputStream();
    BsdGenerator.generate(typeDictionary, outputStream);
  }
}

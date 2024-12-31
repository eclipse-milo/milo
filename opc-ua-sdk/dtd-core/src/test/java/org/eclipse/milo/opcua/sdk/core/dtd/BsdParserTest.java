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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.xml.bind.JAXBException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class BsdParserTest {

  @Test
  void parse() throws JAXBException {
    InputStream inputStream =
        BsdParserTest.class
            .getClassLoader()
            .getResourceAsStream("dictionaries/BsdParserTest.bsd.xml");

    assertNotNull(BsdParser.parse(inputStream));
  }

  @Test
  void parseOpcUaTypeDictionary() throws JAXBException {
    assertNotNull(BsdParser.parseBuiltinTypeDictionary());
  }
}

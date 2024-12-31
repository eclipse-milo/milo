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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.InputStream;
import org.opcfoundation.opcua.binaryschema.ObjectFactory;
import org.opcfoundation.opcua.binaryschema.TypeDictionary;

public class BsdParser {

  private BsdParser() {}

  /**
   * Parse an XML document containing a type dictionary conforming to the OPC Binary XML Schema.
   *
   * @param inputStream the {@link InputStream} to read the XML document from.
   * @return a {@link TypeDictionary}.
   * @throws JAXBException if parsing fails.
   */
  public static TypeDictionary parse(InputStream inputStream) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);

    return (TypeDictionary) context.createUnmarshaller().unmarshal(inputStream);
  }

  public static TypeDictionary parseBuiltinTypeDictionary() throws JAXBException {
    InputStream inputStream =
        BsdParser.class.getClassLoader().getResourceAsStream("Opc.Ua.Types.bsd.xml");

    return parse(inputStream);
  }
}

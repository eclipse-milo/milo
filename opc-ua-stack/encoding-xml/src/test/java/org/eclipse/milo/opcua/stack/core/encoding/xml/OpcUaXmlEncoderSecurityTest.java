/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class OpcUaXmlEncoderSecurityTest {

  private static final String CANARY = "MILO_XXE_CANARY";

  // An XmlElement can arrive over a binary channel and must not disclose server files when encoded.
  @Test
  void xmlElementDoesNotResolveExternalEntity(@TempDir Path tempDir) throws Exception {
    Path canaryFile = tempDir.resolve("canary.txt");
    Files.writeString(canaryFile, CANARY);

    String fragment = externalEntityFragment(canaryFile);

    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      try {
        encoder.encodeXmlElement("Value", XmlElement.of(fragment));
      } catch (UaSerializationException e) {
        return;
      }

      assertFalse(encoder.getOutputString().contains(CANARY));
    }
  }

  // XML-bodied ExtensionObjects use the same fragment parser as XmlElement values.
  @Test
  void extensionObjectDoesNotResolveExternalEntity(@TempDir Path tempDir) throws Exception {
    Path canaryFile = tempDir.resolve("canary.txt");
    Files.writeString(canaryFile, CANARY);

    ExtensionObject value =
        ExtensionObject.of(
            XmlElement.of(externalEntityFragment(canaryFile)), NodeIds.XVType_Encoding_DefaultXml);

    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      try {
        encoder.encodeExtensionObject("Value", value);
      } catch (UaSerializationException e) {
        return;
      }

      assertFalse(encoder.getOutputString().contains(CANARY));
    }
  }

  // DTD processing must not expand attacker-supplied internal entities either.
  @Test
  void xmlElementDoesNotExpandInternalEntity() throws Exception {
    String fragment = "<!DOCTYPE root [<!ENTITY xxe \"" + CANARY + "\">]><root>&xxe;</root>";

    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      try {
        encoder.encodeXmlElement("Value", XmlElement.of(fragment));
      } catch (UaSerializationException e) {
        return;
      }

      assertFalse(encoder.getOutputString().contains(CANARY));
    }
  }

  // Ordinary XML character references still pass through the fragment encoder.
  @Test
  void xmlElementPreservesOrdinaryFragment() throws Exception {
    try (var encoder = new OpcUaXmlEncoder(new DefaultEncodingContext())) {
      encoder.encodeXmlElement("Value", XmlElement.of("<root id=\"7\">A &amp; B</root>"));

      assertEquals(
          "<Value xmlns:uax=\"http://opcfoundation.org/UA/2008/02/Types.xsd\""
              + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
              + "<root id=\"7\">A &amp; B</root></Value>",
          encoder.getOutputString());
    }
  }

  private static String externalEntityFragment(Path file) {
    return "<!DOCTYPE root [<!ENTITY xxe SYSTEM \"" + file.toUri() + "\">]><root>&xxe;</root>";
  }
}

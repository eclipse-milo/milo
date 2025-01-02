/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class LocalizedTextSerializationTest extends BinarySerializationFixture {

  public static Object[][] getLocalizedTexts() {
    return new Object[][] {
      {new LocalizedText(null, null)},
      {new LocalizedText("locale", null)},
      {new LocalizedText(null, "text")},
      {LocalizedText.english("hello, world!")},
    };
  }

  @ParameterizedTest
  @MethodSource("getLocalizedTexts")
  @DisplayName("LocalizedText is round-trip serializable.")
  public void testLocalizedText(LocalizedText localizedText) throws Exception {
    writer.encodeLocalizedText(localizedText);
    LocalizedText decoded = reader.decodeLocalizedText();

    assertEquals(localizedText, decoded);
  }
}

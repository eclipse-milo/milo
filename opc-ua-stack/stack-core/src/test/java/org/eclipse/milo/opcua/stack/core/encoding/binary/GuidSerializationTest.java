/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class GuidSerializationTest extends BinarySerializationFixture {

  public static Object[][] getGuids() {
    return new Object[][] {
      {UUID.fromString("C496578A-0DFE-4b8f-870A-745238C6AEAE")},
      {UUID.fromString("72962B91-FA75-4ae6-8D28-B404DC7DAF63")},
      {UUID.randomUUID()},
      {UUID.randomUUID()},
      {UUID.randomUUID()},
    };
  }

  @ParameterizedTest
  @MethodSource("getGuids")
  @DisplayName("Guid is round-trip serializable.")
  public void testGuidRoundTrip(UUID uuid) throws Exception {
    writer.encodeGuid(uuid);
    UUID decoded = reader.decodeGuid();

    assertEquals(uuid, decoded);
  }
}

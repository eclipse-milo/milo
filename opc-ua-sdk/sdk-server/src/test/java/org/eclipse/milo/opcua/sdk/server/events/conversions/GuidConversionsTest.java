/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

public class GuidConversionsTest {

  @Test
  public void testGuidToByteString() {
    long msb = 0xABCDABCDABCDABCDL;
    long lsb = 0xEFEFEFEFEFEFEFEFL;
    UUID uuid = new UUID(msb, lsb);

    ByteString bs = GuidConversions.guidToByteString(uuid);

    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(msb).putLong(lsb);

    assertEquals(ByteString.of(bb.array()), bs);
  }

  @Test
  public void testGuidToString() {
    UUID uuid = UUID.randomUUID();

    assertEquals(uuid.toString(), GuidConversions.guidToString(uuid));
  }
}

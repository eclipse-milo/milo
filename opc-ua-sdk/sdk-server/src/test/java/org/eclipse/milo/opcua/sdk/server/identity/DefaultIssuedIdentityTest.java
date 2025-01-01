/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.testng.annotations.Test;

public class DefaultIssuedIdentityTest {

  @Test
  void equalTo() {
    var id1 = new DefaultIssuedIdentity(ByteString.of(new byte[] {0x01, 0x02, 0x03}));
    var id2 = new DefaultIssuedIdentity(ByteString.of(new byte[] {0x01, 0x02, 0x03}));
    var id3 = new DefaultIssuedIdentity(ByteString.of(new byte[] {0x04, 0x05, 0x06}));

    assertTrue(id1.equalTo(id2));
    assertFalse(id1.equalTo(id3));
  }
}

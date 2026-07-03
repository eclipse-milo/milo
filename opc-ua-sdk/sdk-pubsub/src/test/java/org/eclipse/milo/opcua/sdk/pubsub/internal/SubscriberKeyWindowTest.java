/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyMaterial;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

/**
 * Tests of the {@link SecurityKeyManager.SubscriberKeyWindow} token→key lookup: token =
 * firstTokenId + index within {prev, current, futures} (Part 14 §8.3.2 token/key association),
 * everything outside the window unknown.
 */
class SubscriberKeyWindowTest {

  private static SecurityKeyMaterial material(int seed) throws UaException {
    byte[] bytes = new byte[52]; // PubSub-Aes128-CTR key data length
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (seed + i);
    }
    return SecurityKeyMaterial.split(PubSubSecurityPolicy.PubSubAes128Ctr, ByteString.of(bytes));
  }

  @Test
  void lookupSpansPreviousCurrentAndFutures() throws Exception {
    SecurityKeyMaterial previous = material(1);
    SecurityKeyMaterial current = material(2);
    SecurityKeyMaterial future1 = material(3);
    SecurityKeyMaterial future2 = material(4);

    var window =
        new SecurityKeyManager.SubscriberKeyWindow(
            4, previous, 5, current, List.of(future1, future2));

    assertSame(previous, window.keyFor(4));
    assertSame(current, window.keyFor(5));
    assertSame(future1, window.keyFor(6));
    assertSame(future2, window.keyFor(7));

    // outside the window: before prev, after the last future, and nonsense values
    assertNull(window.keyFor(3));
    assertNull(window.keyFor(8));
    assertNull(window.keyFor(0));
    assertNull(window.keyFor(-1));
  }

  @Test
  void noPreviousKeyBeforeTheFirstSwitch() throws Exception {
    SecurityKeyMaterial current = material(2);

    var window = new SecurityKeyManager.SubscriberKeyWindow(0, null, 1, current, List.of());

    assertSame(current, window.keyFor(1));
    assertNull(window.keyFor(0));
    assertNull(window.keyFor(2));
  }
}

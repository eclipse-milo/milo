/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.testng.annotations.Test;

public class ClientHandleSequenceTest {

  @Test
  public void testRollover() {
    ClientHandleSequence sequence = new ClientHandleSequence(h -> false, UInteger.MAX_VALUE - 1);

    assertEquals(uint(UInteger.MAX_VALUE - 1), sequence.nextClientHandle());
    assertEquals(UInteger.MAX, sequence.nextClientHandle());
    assertEquals(uint(0), sequence.nextClientHandle());
    assertEquals(uint(1), sequence.nextClientHandle());
  }

  @Test
  public void testInUsePredicate() {
    ClientHandleSequence sequence = new ClientHandleSequence(h -> h.longValue() < 10);

    assertEquals(uint(10), sequence.nextClientHandle());
  }

  // Slow; enable to test manually.
  @Test(enabled = false)
  public void testThrowsIfAllUsed() {
    ClientHandleSequence sequence = new ClientHandleSequence(h -> true);

    assertThrows(IllegalStateException.class, sequence::nextClientHandle);
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.junit.jupiter.api.Test;

class OptionSetUIntegerTest {

  @Test
  public void equalsAndHashCode() {
    AccessRestrictionType[] arts = new AccessRestrictionType[8];
    for (int i = 0; i < 8; i++) {
      arts[i] = new AccessRestrictionType(ushort(i));
    }

    for (int i = 0; i < 8; i++) {
      AccessRestrictionType expected = new AccessRestrictionType(ushort(i));
      assertEquals(expected, arts[i]);
      assertEquals(expected.hashCode(), arts[i].hashCode());
    }
  }
}

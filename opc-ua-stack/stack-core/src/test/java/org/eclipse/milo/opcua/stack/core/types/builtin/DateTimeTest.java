/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

public class DateTimeTest {

  @Test
  public void getJavaInstant() {
    ZonedDateTime inputZdt =
        LocalDateTime.of(2020, Month.JANUARY, 10, 20, 30, 40, 123_456_789)
            .atZone(ZoneId.systemDefault());

    Instant endInstant = inputZdt.toInstant();

    DateTime dateTime = new DateTime(endInstant);
    Instant outputInstant = dateTime.getJavaInstant();

    ZonedDateTime outputZdt = ZonedDateTime.ofInstant(outputInstant, ZoneId.systemDefault());

    assertEquals(inputZdt.getYear(), outputZdt.getYear());
    assertEquals(inputZdt.getMonth(), outputZdt.getMonth());
    assertEquals(inputZdt.getDayOfMonth(), outputZdt.getDayOfMonth());
    assertEquals(inputZdt.getHour(), outputZdt.getHour());
    assertEquals(inputZdt.getMinute(), outputZdt.getMinute());
    assertEquals(inputZdt.getSecond(), outputZdt.getSecond());
    // we lose precision here because DateTime uses 100-nanosecond intervals
    assertEquals(123_456_700, outputZdt.getNano());
  }
}

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

import static org.testng.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.testng.annotations.Test;

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

    assertEquals(outputZdt.getYear(), inputZdt.getYear());
    assertEquals(outputZdt.getMonth(), inputZdt.getMonth());
    assertEquals(outputZdt.getDayOfMonth(), inputZdt.getDayOfMonth());
    assertEquals(outputZdt.getHour(), inputZdt.getHour());
    assertEquals(outputZdt.getMinute(), inputZdt.getMinute());
    assertEquals(outputZdt.getSecond(), inputZdt.getSecond());
    assertEquals(outputZdt.getNano(), 123_456_789);
  }
}

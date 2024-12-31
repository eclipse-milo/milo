/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode.InfoType;
import org.junit.jupiter.api.Test;

class StatusCodesTest {

  @Test
  void lookupGood() {
    String[] ss = StatusCodes.lookup(StatusCodes.Good).orElseThrow();
    assertEquals("Good", ss[0]);
    assertEquals("The operation succeeded.", ss[1]);

    ss = StatusCodes.lookup(StatusCodes.Good_Cascade).orElseThrow();
    assertEquals("Good_Cascade", ss[0]);
    assertEquals(
        "The value is accurate, and the signal source supports cascade handshaking.", ss[1]);
  }

  @Test
  void lookupUncertain() {
    String[] ss = StatusCodes.lookup(StatusCodes.Uncertain).orElseThrow();
    assertEquals("Uncertain", ss[0]);
    assertEquals("The operation was uncertain.", ss[1]);

    ss = StatusCodes.lookup(StatusCodes.Uncertain_ConfigurationError).orElseThrow();
    assertEquals("Uncertain_ConfigurationError", ss[0]);
    assertEquals("The value may not be accurate due to a configuration issue.", ss[1]);
  }

  @Test
  void lookupBad() {
    String[] ss = StatusCodes.lookup(StatusCodes.Bad).orElseThrow();
    assertEquals("Bad", ss[0]);
    assertEquals("The operation failed.", ss[1]);

    ss = StatusCodes.lookup(StatusCodes.Bad_ServiceUnsupported).orElseThrow();
    assertEquals("Bad_ServiceUnsupported", ss[0]);
    assertEquals("The server does not support the requested service.", ss[1]);
  }

  @Test
  void infoBitsNotUsed() {
    assertSame(StatusCode.GOOD.getInfoType(), InfoType.NotUsed);
  }

  @Test
  void infoBitsDataValue() {
    StatusCode statusCode = StatusCode.GOOD.withDataValueInfoType();
    assertSame(statusCode.getInfoType(), InfoType.DataValue);
    assertEquals(
        statusCode.getDataValueInfoBits().map(StatusCode.DataValueInfoBits::getBits).orElseThrow(),
        0);

    assertSame(statusCode.withoutDataValueInfoType().getInfoType(), InfoType.NotUsed);
  }

  @Test
  void infoBitsDataValueWithOverflow() {
    StatusCode withOverflow = StatusCode.GOOD.withOverflow();
    assertSame(withOverflow.getInfoType(), InfoType.DataValue);
    assertTrue(
        withOverflow
            .getDataValueInfoBits()
            .map(StatusCode.DataValueInfoBits::isOverflow)
            .orElse(false));
    assertTrue(withOverflow.isOverflowSet());
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.junit.jupiter.api.Test;

class MethodCallResultTest {

  private static final ResponseHeader HEADER =
      new ResponseHeader(
          DateTime.now(),
          uint(1),
          StatusCode.GOOD,
          DiagnosticInfo.NULL_VALUE,
          new String[] {"mode must be 0..3"},
          null);

  private static CallMethodResult raw(long status, Variant... outputs) {
    return new CallMethodResult(
        new StatusCode(status), new StatusCode[0], new DiagnosticInfo[0], outputs);
  }

  // Part 4 §5.12.2.2: a Bad operation status carries no outputs, so nothing must be presented as
  // decoded; the result is still a result, not an exception, until requireGood is asked for.
  @Test
  void badStatusIsAResultWithoutOutputsAndRequireGoodThrowsWithHeader() {
    CallMethodResult raw =
        new CallMethodResult(
            new StatusCode(StatusCodes.Bad_InvalidArgument),
            new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
            new DiagnosticInfo[] {new DiagnosticInfo(-1, -1, -1, 0, null, null, null)},
            new Variant[0]);

    MethodCallResult<Variant[]> result = MethodCallResult.of(raw, HEADER);

    assertFalse(result.hasOutputs());
    assertNull(result.outputs());
    assertNull(result.conversionFailure());
    assertEquals(1, result.inputArgumentResults().length);

    UaMethodException e = assertThrows(UaMethodException.class, result::requireGood);
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), e.getStatusCode());
    assertArrayEquals(raw.getInputArgumentResults(), e.getInputArgumentResults());
    assertSame(HEADER, e.getResponseHeader(), "diagnostics index the header's string table");
  }

  // Uncertain results carry outputs a caller may still want, so conversion runs for them too.
  @Test
  void uncertainOutputsAreConvertedAndRequireGoodStillRejects() throws UaException {
    MethodCallResult<Integer> result =
        MethodCallResult.of(raw(StatusCodes.Uncertain_SubNormal, new Variant(7)), HEADER)
            .map(outputs -> (Integer) outputs[0].value());

    assertTrue(result.hasOutputs());
    assertEquals(7, result.outputs());
    assertTrue(result.statusCode().isUncertain());
    assertThrows(UaMethodException.class, result::requireGood);
  }

  // A decoding problem must not be confused with the server's verdict: the status stays Good, the
  // raw outputs stay available, and the failure is reported on its own.
  @Test
  void conversionFailureIsKeptApartFromTheOperationStatus() {
    MethodCallResult<Variant[]> good = MethodCallResult.of(raw(0L, new Variant("x")), HEADER);

    MethodCallResult<Integer> failed =
        good.map(
            outputs -> {
              throw new UaException(StatusCodes.Bad_DecodingError, "not an integer");
            });

    assertTrue(failed.statusCode().isGood());
    assertFalse(failed.hasOutputs());
    assertNotNull(failed.conversionFailure());
    assertEquals(
        new StatusCode(StatusCodes.Bad_DecodingError), failed.conversionFailure().getStatusCode());
    assertEquals("x", failed.rawOutputs()[0].value());

    UaException e = assertThrows(UaException.class, failed::requireGood);
    assertSame(failed.conversionFailure(), e);

    MethodCallResult<Integer> crashed = good.map(outputs -> (Integer) outputs[0].value());
    assertNotNull(crashed.conversionFailure(), "a ClassCastException is a conversion failure");
    assertEquals(
        new StatusCode(StatusCodes.Bad_DecodingError), crashed.conversionFailure().getStatusCode());
  }

  // A Method whose single output is null has outputs; hasOutputs is what tells that apart from a
  // result that decoded nothing.
  @Test
  void nullConvertedOutputIsStillAnOutput() throws UaException {
    MethodCallResult<Object> result =
        MethodCallResult.of(raw(0L, Variant.NULL_VALUE), HEADER).map(outputs -> null);

    assertTrue(result.hasOutputs());
    assertNull(result.outputs());
    assertNull(result.requireGood());
  }
}

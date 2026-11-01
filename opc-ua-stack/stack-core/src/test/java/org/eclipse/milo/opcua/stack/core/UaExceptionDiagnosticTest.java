/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UaExceptionDiagnosticTest {

  private static final Diagnostic RUN_MODE =
      Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE").withText("Blocked in RUN mode");

  @Nested
  class CauseChain {

    // The blocking client API wraps a failed future's cause in new UaException(cause); a caller
    // catching UaException must still see the server's explanation.
    @Test
    void wrappingCopiesTheDiagnosticFromTheCause() {
      var fault = new UaException(StatusCodes.Bad_NotWritable, "blocked", RUN_MODE);

      var wrapped = new UaException(new ExecutionException(fault));

      assertEquals(new StatusCode(StatusCodes.Bad_NotWritable), wrapped.getStatusCode());
      assertEquals(RUN_MODE, wrapped.getDiagnostic().orElseThrow());
    }

    // A diagnostic explains one status. Pairing it with the status of an exception that wraps it
    // would let the diagnostics policy judge the wrong status, for example passing the explanation
    // of an access-denied failure because the wrapper says Bad_InternalError.
    @Test
    void wrappingTakesTheDiagnosticOfTheExceptionThatSuppliesTheStatus() {
      var inner = new UaException(StatusCodes.Bad_UserAccessDenied, "denied", RUN_MODE);
      var middle = new UaException(StatusCodes.Bad_InternalError, inner);

      var wrapped = new UaException(new CompletionException(middle));

      assertEquals(new StatusCode(StatusCodes.Bad_InternalError), wrapped.getStatusCode());
      assertTrue(wrapped.getDiagnostic().isEmpty());
      assertTrue(UaException.extractDiagnostic(middle).isEmpty());
    }

    @Test
    void runtimeExceptionsCarryDiagnosticsThroughTheChain() {
      var runtime =
          new UaRuntimeException(new StatusCode(StatusCodes.Bad_Timeout), "timeout", RUN_MODE);

      var wrapped = new UaException(new RuntimeException(runtime));

      assertEquals(RUN_MODE, wrapped.getDiagnostic().orElseThrow());
    }

    @Test
    void wrappingAnExceptionWithoutADiagnosticHasNone() {
      var wrapped =
          new UaException(new ExecutionException(new UaException(StatusCodes.Bad_Timeout)));

      assertTrue(wrapped.getDiagnostic().isEmpty());
    }

    // An explicit status replaces the cause's; the cause's diagnostic explains a different status
    // and must not be attached to this one.
    @Test
    void explicitStatusDoesNotTakeTheCauseDiagnostic() {
      var cause = new UaException(StatusCodes.Bad_NotWritable, "blocked", RUN_MODE);

      var wrapped = new UaException(StatusCodes.Bad_InternalError, cause);

      assertTrue(wrapped.getDiagnostic().isEmpty());
    }
  }

  @Nested
  class ServiceFaults {

    @Test
    void faultDiagnosticResolvesAgainstTheFaultStringTable() {
      var serviceDiagnostics = new DiagnosticInfo(0, 1, -1, 2, null, null, null);
      ServiceFault fault =
          fault(
              serviceDiagnostics,
              new String[] {"urn:acme:plc", "E_RUN_MODE", "Blocked in RUN mode"});

      var exception = new UaServiceFaultException(fault);

      assertEquals(RUN_MODE, exception.getDiagnostic().orElseThrow());
    }

    // Every log line that prints a fault then shows the server's explanation.
    @Test
    void faultMessageIncludesTheSymbolicIdAndText() {
      var serviceDiagnostics = new DiagnosticInfo(0, 1, -1, 2, null, null, null);
      ServiceFault fault =
          fault(
              serviceDiagnostics,
              new String[] {"urn:acme:plc", "E_RUN_MODE", "Blocked in RUN mode"});

      var exception = new UaServiceFaultException(fault);

      assertTrue(exception.getMessage().contains("symbolicId=E_RUN_MODE"), exception.getMessage());
      assertTrue(
          exception.getMessage().contains("text=Blocked in RUN mode"), exception.getMessage());
      assertTrue(exception.toString().contains("E_RUN_MODE"), exception.toString());
    }

    @Test
    void faultWithoutDiagnosticsKeepsItsMessage() {
      var exception = new UaServiceFaultException(fault(null, null));

      assertFalse(exception.getDiagnostic().isPresent());
      assertEquals(
          new UaException(StatusCodes.Bad_NotWritable).getMessage(), exception.getMessage());
    }

    // The blocking client wraps a fault the same way it wraps any other failure.
    @Test
    void wrappedFaultKeepsItsDiagnostic() {
      var serviceDiagnostics = new DiagnosticInfo(0, 1, -1, 2, null, null, null);
      ServiceFault fault =
          fault(
              serviceDiagnostics,
              new String[] {"urn:acme:plc", "E_RUN_MODE", "Blocked in RUN mode"});

      var wrapped = new UaException(new ExecutionException(new UaServiceFaultException(fault)));

      assertEquals(RUN_MODE, wrapped.getDiagnostic().orElseThrow());
    }

    private static ServiceFault fault(
        @Nullable DiagnosticInfo serviceDiagnostics, String @Nullable [] stringTable) {

      return new ServiceFault(
          new ResponseHeader(
              DateTime.now(),
              uint(1),
              new StatusCode(StatusCodes.Bad_NotWritable),
              serviceDiagnostics,
              stringTable,
              null));
    }
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.server.methods;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.CallContext;
import org.eclipse.milo.opcua.sdk.server.CallOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SynchronousMethodOutcomeTest {
  private final AtomicInteger calls = new AtomicInteger();
  private final UaMethodNode node = mock(UaMethodNode.class);
  private final AccessContext access = Optional::empty;
  private final Argument[] arguments = {
    new Argument("Value", NodeIds.Int32, -1, null, LocalizedText.NULL_VALUE),
    new Argument("Label", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
  };

  SynchronousMethodOutcomeTest() {
    UaNodeContext context = mock(UaNodeContext.class);
    OpcUaServer server = mock(OpcUaServer.class);
    when(node.getNodeContext()).thenReturn(context);
    when(context.getServer()).thenReturn(server);
    when(server.getDataTypeTree()).thenReturn(mock(DataTypeTree.class));
  }

  private AbstractMethodInvocationHandler handler(int required, MethodResult outcome) {
    return new AbstractMethodInvocationHandler(node) {
      public Argument[] getInputArguments() {
        return arguments;
      }

      public Argument[] getOutputArguments() {
        return new Argument[0];
      }

      protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
        return required;
      }

      protected MethodResult invokeResult(InvocationContext context, Variant[] supplied) {
        calls.incrementAndGet();
        return outcome == null ? MethodResult.uncertain(StatusCode.UNCERTAIN, supplied) : outcome;
      }
    };
  }

  private CallMethodResult call(MethodInvocationHandler handler, Variant... inputs) {
    return handler.invoke(
        access, new CallMethodRequest(new NodeId(1, 1), new NodeId(1, 2), inputs));
  }

  @Test
  void omissionAndSuppliedNullRetainDifferentCountsAndUncertainOutputs() {
    AbstractMethodInvocationHandler handler = handler(1, null);
    Variant[] inputs = {new Variant(7), Variant.NULL_VALUE};
    CallMethodResult omitted = call(handler, new Variant(7));
    CallMethodResult supplied = call(handler, inputs);
    assertEquals(StatusCode.UNCERTAIN, omitted.getStatusCode());
    assertEquals(1, omitted.getOutputArguments().length);
    assertEquals(2, supplied.getOutputArguments().length);
    assertSame(Variant.NULL_VALUE, supplied.getOutputArguments()[1]);
    assertNotSame(inputs, supplied.getOutputArguments());
    assertEquals(2, calls.get());
  }

  @Test
  void badCountsAndTypesSuppressApplicationCallback() {
    AbstractMethodInvocationHandler handler = handler(1, null);
    assertEquals(new StatusCode(StatusCodes.Bad_ArgumentsMissing), call(handler).getStatusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_TooManyArguments),
        call(handler, new Variant(7), Variant.NULL_VALUE, Variant.NULL_VALUE).getStatusCode());
    CallMethodResult wrong = call(handler, new Variant("wrong"));
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), wrong.getStatusCode());
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_TypeMismatch)},
        wrong.getInputArgumentResults());
    assertEquals(0, calls.get());
  }

  @Test
  void invalidConfiguredCountsSuppressCallback() {
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        call(handler(-1, null), new Variant(7)).getStatusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        call(handler(3, null), new Variant(7)).getStatusCode());
    assertEquals(0, calls.get());
  }

  @Test
  void valueValidationRunsAfterShapeValidationAndMayRejectConcreteEnums() {
    AbstractMethodInvocationHandler handler =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return new Argument[] {arguments[0]};
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected void validateInputArgumentValues(Variant[] supplied)
              throws InvalidArgumentException {
            calls.incrementAndGet();
            throw new InvalidArgumentException(
                new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)});
          }
        };
    assertEquals(
        new StatusCode(StatusCodes.Bad_InvalidArgument),
        call(handler, new Variant("wrong")).getStatusCode());
    assertEquals(0, calls.get());
    CallMethodResult result = call(handler, new Variant(99));
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
        result.getInputArgumentResults());
    assertEquals(1, calls.get());
  }

  @Test
  void legacySubclassRetainsExactCountAndGoodOutcome() {
    AbstractMethodInvocationHandler legacy =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return arguments;
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected Variant[] invoke(InvocationContext context, Variant[] values) {
            calls.incrementAndGet();
            return values;
          }
        };
    assertEquals(
        new StatusCode(StatusCodes.Bad_ArgumentsMissing),
        call(legacy, new Variant(7)).getStatusCode());
    assertEquals(StatusCode.GOOD, call(legacy, new Variant(7), Variant.NULL_VALUE).getStatusCode());
    assertEquals(1, calls.get());
  }

  // A handler may return the same MethodResult for every call. Each Call result gets its own
  // output array, so a caller that changes one response cannot change the next.
  @Test
  void reusedResultGivesEachCallItsOwnOutputs() {
    AbstractMethodInvocationHandler handler =
        handler(1, MethodResult.uncertain(StatusCode.UNCERTAIN, new Variant(1)));

    CallMethodResult first = call(handler, new Variant(7));
    first.getOutputArguments()[0] = new Variant(2);
    CallMethodResult second = call(handler, new Variant(7));

    assertArrayEquals(new Variant[] {new Variant(1)}, second.getOutputArguments());
  }

  @Test
  void goodSubcodesAreDelivered() {
    MethodResult outcome = MethodResult.good(new StatusCode(StatusCodes.Good_Clamped));
    assertEquals(
        outcome.getStatusCode(), call(handler(1, outcome), new Variant(7)).getStatusCode());
  }

  // The exception path is checked by the same rules as a returned result.
  @Test
  void invalidArgumentExceptionWithMismatchedResultsBecomesInternalError() {
    AbstractMethodInvocationHandler handler =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return arguments;
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected int getRequiredInputArgumentCount(Argument[] inputArguments) {
            return 1;
          }

          protected void validateInputArgumentValues(Variant[] supplied)
              throws InvalidArgumentException {
            throw new InvalidArgumentException(
                new StatusCode[] {StatusCode.GOOD, new StatusCode(StatusCodes.Bad_OutOfRange)});
          }
        };
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        call(handler, new Variant(7)).getStatusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InvalidArgument),
        call(handler, new Variant(7), new Variant("label")).getStatusCode());
  }

  @Test
  void nullCompleteOutcomeBecomesInternalError() {
    AbstractMethodInvocationHandler handler =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return new Argument[0];
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected MethodResult invokeResult(InvocationContext context, Variant[] values) {
            return null;
          }
        };
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), call(handler).getStatusCode());
  }

  @Test
  void metadataIsReadOnceBeforeValidationAndLegacyNullOutputsArePreserved() {
    AtomicInteger metadataReads = new AtomicInteger();
    AbstractMethodInvocationHandler legacy =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return metadataReads.getAndIncrement() == 0 ? new Argument[] {arguments[0]} : arguments;
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected Variant[] invoke(InvocationContext context, Variant[] values) {
            return null;
          }
        };
    CallMethodResult result = call(legacy, new Variant(7));
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertNull(result.getOutputArguments());
    assertEquals(1, metadataReads.get());
  }

  /** Diagnostics reach the Call service through the CallContext the handler is invoked with. */
  @Nested
  class Diagnostics {

    private final RecordingDiagnostics recorded = new RecordingDiagnostics();

    private final CallContext context =
        new CallContext(mock(OpcUaServer.class), null, recorded, null, uint(0), null);

    private final Diagnostic explanation = Diagnostic.text("explained");

    @Test
    void diagnosticOfAThrownExceptionExplainsTheOperation() {
      AbstractMethodInvocationHandler handler =
          resultHandler(
              () -> {
                throw new UaException(StatusCodes.Bad_DeviceFailure, "failed", explanation);
              });

      CallMethodResult result = handler.invoke(context, request);

      assertEquals(new StatusCode(StatusCodes.Bad_DeviceFailure), result.getStatusCode());
      assertEquals(explanation, recorded.operations.get(request));
    }

    @Test
    void diagnosticOfAResultExplainsTheOperation() {
      AbstractMethodInvocationHandler handler =
          resultHandler(
              () ->
                  MethodResult.bad(new StatusCode(StatusCodes.Bad_DeviceFailure))
                      .withDiagnostic(explanation));

      handler.invoke(context, request);

      assertEquals(explanation, recorded.operations.get(request));
    }

    @Test
    void argumentDiagnosticsOfAnInvalidArgumentExceptionExplainTheArguments() {
      AbstractMethodInvocationHandler handler =
          resultHandler(
              () -> {
                throw InvalidArgumentException.builder(2)
                    .reject(1, StatusCodes.Bad_OutOfRange, explanation)
                    .build();
              });

      CallMethodResult result = handler.invoke(context, request);

      assertArrayEquals(
          new StatusCode[] {StatusCode.GOOD, new StatusCode(StatusCodes.Bad_OutOfRange)},
          result.getInputArgumentResults());
      assertEquals(0, result.getInputArgumentDiagnosticInfos().length, "reported, not inlined");
      assertEquals(Arrays.asList(null, explanation), recorded.arguments.get(request));
    }

    // A result that breaks the rules becomes Bad_InternalError; its diagnostics would explain a
    // status the client never sees.
    @Test
    void diagnosticsOfAnInvalidOutcomeAreNotReported() {
      AbstractMethodInvocationHandler handler =
          resultHandler(
              () -> {
                throw InvalidArgumentException.builder(3)
                    .reject(0, StatusCodes.Bad_OutOfRange, explanation)
                    .build();
              });

      CallMethodResult result = handler.invoke(context, request);

      assertEquals(new StatusCode(StatusCodes.Bad_InternalError), result.getStatusCode());
      assertTrue(recorded.operations.isEmpty());
      assertTrue(recorded.arguments.isEmpty());
    }

    // Outside a Call request there is no response to carry diagnostics; the outcome is unchanged.
    @Test
    void directInvocationDropsDiagnostics() {
      AbstractMethodInvocationHandler handler =
          resultHandler(() -> MethodResult.good().withDiagnostic(explanation));

      CallMethodResult result = handler.invoke(access, request);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertTrue(recorded.operations.isEmpty());
    }

    private final CallMethodRequest request =
        new CallMethodRequest(
            new NodeId(1, 1), new NodeId(1, 2), new Variant[] {new Variant(7), new Variant("x")});

    private AbstractMethodInvocationHandler resultHandler(Outcome outcome) {
      return new AbstractMethodInvocationHandler(node) {
        public Argument[] getInputArguments() {
          return arguments;
        }

        public Argument[] getOutputArguments() {
          return new Argument[0];
        }

        protected MethodResult invokeResult(InvocationContext context, Variant[] values)
            throws UaException {
          return outcome.get();
        }
      };
    }
  }

  @FunctionalInterface
  interface Outcome {
    MethodResult get() throws UaException;
  }

  /** Records what a handler reports, by request instance. */
  static final class RecordingDiagnostics implements CallOperationDiagnostics {
    final Map<CallMethodRequest, Diagnostic> operations = new IdentityHashMap<>();
    final Map<CallMethodRequest, List<?>> arguments = new IdentityHashMap<>();

    @Override
    public boolean isRequested() {
      return true;
    }

    @Override
    public void report(CallMethodRequest operation, Diagnostic diagnostic) {
      operations.put(operation, diagnostic);
    }

    @Override
    public void reportArguments(
        CallMethodRequest request, List<? extends @Nullable Diagnostic> diagnostics) {
      arguments.put(request, diagnostics);
    }
  }
}

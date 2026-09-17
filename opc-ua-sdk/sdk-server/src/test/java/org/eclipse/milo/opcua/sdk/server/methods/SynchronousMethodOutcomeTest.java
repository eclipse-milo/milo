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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

  private AbstractMethodInvocationHandler handler(int required, CallMethodResult outcome) {
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

      protected CallMethodResult invokeResult(InvocationContext context, Variant[] supplied) {
        calls.incrementAndGet();
        return outcome == null
            ? new CallMethodResult(StatusCode.UNCERTAIN, null, null, supplied)
            : outcome;
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

  // One handler shape covers every outcome: a callback returns outputs, and reports a Good subcode
  // or Uncertain by setting it on the context. Bad must be thrown, never set.
  @Test
  void contextStatusIsReturnedWithOutputsAndBadContextStatusIsRejected() {
    AbstractMethodInvocationHandler handler =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return new Argument[] {arguments[0]};
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected Variant[] invoke(InvocationContext context, Variant[] values) {
            assertEquals(StatusCode.GOOD, context.getStatusCode(), "default status");
            int value = (int) values[0].value();
            if (value < 0) {
              context.setStatusCode(new StatusCode(StatusCodes.Bad_OutOfRange));
            } else if (value == 0) {
              context.setStatusCode(new StatusCode(StatusCodes.Uncertain_SubNormal));
            }
            return values;
          }
        };
    CallMethodResult uncertain = call(handler, new Variant(0));
    assertEquals(new StatusCode(StatusCodes.Uncertain_SubNormal), uncertain.getStatusCode());
    assertEquals(1, uncertain.getOutputArguments().length, "Uncertain keeps its outputs");
    assertEquals(StatusCode.GOOD, call(handler, new Variant(1)).getStatusCode());
    assertThrows(IllegalArgumentException.class, () -> call(handler, new Variant(-1)));
  }

  // Omission is observable through the context without changing the handler signature.
  @Test
  void suppliedInputCountDistinguishesOmissionFromSuppliedNull() {
    AtomicInteger supplied = new AtomicInteger();
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

          protected Variant[] invoke(InvocationContext context, Variant[] values) {
            supplied.set(context.suppliedInputCount());
            return new Variant[0];
          }
        };
    assertEquals(StatusCode.GOOD, call(handler, new Variant(7)).getStatusCode());
    assertEquals(1, supplied.get());
    assertEquals(
        StatusCode.GOOD, call(handler, new Variant(7), Variant.NULL_VALUE).getStatusCode());
    assertEquals(2, supplied.get());
  }

  // A handler rejecting one argument by index should not have to know how many inputs arrived;
  // the result is still one status per supplied input, as Part 4 §5.12.2.2 requires.
  @Test
  void sparseInvalidArgumentIsPaddedToSuppliedInputsAndCarriesDiagnosticText() {
    AbstractMethodInvocationHandler handler =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return arguments;
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected Variant[] invoke(InvocationContext context, Variant[] values)
              throws InvalidArgumentException {
            throw InvalidArgumentException.builder()
                .argument(0, StatusCodes.Bad_OutOfRange, "value must be positive")
                .build();
          }
        };
    CallMethodResult result = call(handler, new Variant(-1), new Variant("label"));
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange), StatusCode.GOOD},
        result.getInputArgumentResults());
    DiagnosticInfo[] diagnostics = result.getInputArgumentDiagnosticInfos();
    assertEquals(2, diagnostics.length);
    assertEquals("value must be positive", diagnostics[0].additionalInfo());
    assertEquals(DiagnosticInfo.NULL_VALUE, diagnostics[1]);
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

  static Stream<CallMethodResult> invalidOutcomes() {
    return Stream.of(
        new CallMethodResult(
            new StatusCode(StatusCodes.Bad_NotSupported),
            null,
            null,
            new Variant[] {new Variant(1)}),
        new CallMethodResult(StatusCode.GOOD, new StatusCode[] {StatusCode.GOOD}, null, null),
        new CallMethodResult(
            new StatusCode(StatusCodes.Bad_InvalidArgument),
            new StatusCode[] {StatusCode.GOOD, StatusCode.GOOD},
            null,
            null),
        new CallMethodResult(
            StatusCode.GOOD,
            null,
            new DiagnosticInfo[] {DiagnosticInfo.NULL_VALUE, DiagnosticInfo.NULL_VALUE},
            null),
        new CallMethodResult(null, null, null, null));
  }

  // Part 4 Call outcomes cannot attach outputs to Bad or populate input results for other statuses.
  @ParameterizedTest
  @MethodSource("invalidOutcomes")
  void invalidCompleteOutcomesBecomeInternalError(CallMethodResult outcome) {
    CallMethodResult result = call(handler(1, outcome), new Variant(7));
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), result.getStatusCode());
    assertEquals(0, result.getOutputArguments().length);
  }

  @Test
  void goodSubcodesAreDelivered() {
    CallMethodResult outcome =
        new CallMethodResult(new StatusCode(StatusCodes.Good_Clamped), null, null, null);
    assertEquals(
        outcome.getStatusCode(), call(handler(1, outcome), new Variant(7)).getStatusCode());
  }

  // A builder-built rejection of an input the caller omitted cannot be represented per argument;
  // the call still reports Bad_InvalidArgument sized to the supplied inputs, not Bad_InternalError.
  @Test
  void sparseInvalidArgumentBeyondSuppliedInputsIsTrimmedToSuppliedInputs() {
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

          protected Variant[] invoke(InvocationContext context, Variant[] values)
              throws InvalidArgumentException {
            throw InvalidArgumentException.builder()
                .argument(1, StatusCodes.Bad_ArgumentsMissing, "label is required in this mode")
                .build();
          }
        };
    CallMethodResult result = call(handler, new Variant(7));
    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, result.getInputArgumentResults());
    assertArrayEquals(
        new DiagnosticInfo[] {DiagnosticInfo.NULL_VALUE}, result.getInputArgumentDiagnosticInfos());
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

          protected CallMethodResult invokeResult(InvocationContext context, Variant[] values) {
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
}

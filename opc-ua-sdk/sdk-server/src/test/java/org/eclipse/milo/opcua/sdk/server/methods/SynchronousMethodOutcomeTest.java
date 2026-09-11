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
        new CallMethodResult(new StatusCode(StatusCodes.Good_Clamped), null, null, null),
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

  // The resource was compiled against the recorded published baseline, not the current reactor.
  @Test
  void oldCompiledSubclassLinksAndRetainsLegacyBehavior() throws Exception {
    byte[] encoded;
    try (var stream =
        getClass().getResourceAsStream("/methods/legacy-handler/LegacyMethodHandler.class.b64")) {
      encoded = java.util.Objects.requireNonNull(stream).readAllBytes();
    }
    byte[] bytes = java.util.Base64.getMimeDecoder().decode(encoded);
    Class<?> oldClass =
        new ClassLoader(getClass().getClassLoader()) {
          Class<?> loadLegacy() {
            return defineClass(null, bytes, 0, bytes.length);
          }
        }.loadLegacy();
    MethodInvocationHandler old =
        (MethodInvocationHandler) oldClass.getConstructor(UaMethodNode.class).newInstance(node);
    assertEquals(StatusCode.GOOD, call(old).getStatusCode());
    assertArrayEquals(new Variant[] {new Variant("legacy")}, call(old).getOutputArguments());
    assertEquals(
        new StatusCode(StatusCodes.Bad_TooManyArguments),
        call(old, Variant.NULL_VALUE).getStatusCode());
  }

  @Test
  void diagnosticBudgetFailureReturnsInternalErrorWithoutRetry() {
    var diagnostics =
        new org.eclipse.milo.opcua.sdk.server.DiagnosticsContext<>(
            uint(0x20),
            new org.eclipse.milo.opcua.stack.core.channel.EncodingLimits(8196, 1, 8, 128));
    AbstractMethodInvocationHandler handler =
        new AbstractMethodInvocationHandler(node) {
          public Argument[] getInputArguments() {
            return new Argument[0];
          }

          public Argument[] getOutputArguments() {
            return new Argument[0];
          }

          protected CallMethodResult invokeResult(InvocationContext context, Variant[] values) {
            calls.incrementAndGet();
            diagnostics.addString("too long");
            throw new AssertionError("budget must reject the diagnostic");
          }
        };
    CallMethodResult result = call(handler);
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), result.getStatusCode());
    assertEquals(0, result.getOutputArguments().length);
    assertEquals(1, calls.get());
  }
}

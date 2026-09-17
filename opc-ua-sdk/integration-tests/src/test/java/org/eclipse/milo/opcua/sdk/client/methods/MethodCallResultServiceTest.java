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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.InvalidArgumentException;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The complete client outcome of a Method call, driven end to end against a handler that uses the
 * server-side context status and argument builder.
 */
class MethodCallResultServiceTest extends AbstractClientServerTest {

  private static final Argument MODE =
      new Argument("Mode", NodeIds.Byte, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE);
  private static final Argument FILE_HANDLE =
      new Argument("FileHandle", NodeIds.UInt32, ValueRanks.Scalar, null, LocalizedText.NULL_VALUE);

  private UaMethod open;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          UaMethodNode node =
              UaMethodNode.builder(context)
                  .setNodeId(newNodeId("Open"))
                  .setBrowseName(newQualifiedName("Open"))
                  .setDisplayName(LocalizedText.english("Open"))
                  .addReference(
                      new Reference(
                          newNodeId("Open"),
                          NodeIds.HasComponent,
                          NodeIds.ObjectsFolder.expanded(),
                          Reference.Direction.INVERSE))
                  .buildAndAdd();

          node.setInputArguments(new Argument[] {MODE});
          node.setOutputArguments(new Argument[] {FILE_HANDLE});
          node.setInvocationHandler(
              new AbstractMethodInvocationHandler(node) {
                @Override
                public Argument[] getInputArguments() {
                  return new Argument[] {MODE};
                }

                @Override
                public Argument[] getOutputArguments() {
                  return new Argument[] {FILE_HANDLE};
                }

                @Override
                protected Variant[] invoke(InvocationContext context, Variant[] inputValues)
                    throws UaException {
                  int mode = ((UByte) inputValues[0].value()).intValue();
                  if (mode > 3) {
                    throw InvalidArgumentException.builder()
                        .argument(0, StatusCodes.Bad_OutOfRange, "mode must be 0..3")
                        .build();
                  }
                  if (mode == 3) {
                    context.setStatusCode(new StatusCode(StatusCodes.Uncertain_SubNormal));
                  }
                  return new Variant[] {new Variant(uint(100 + mode))};
                }
              });
        });
  }

  @BeforeEach
  void lookUpMethod() throws UaException {
    open =
        client
            .getAddressSpace()
            .getObjectNode(NodeIds.ObjectsFolder)
            .getMethod(new QualifiedName(newNodeId("Open").getNamespaceIndex(), "Open"));
  }

  // The result form reports a Bad operation status instead of throwing, keeps the per-argument
  // statuses, and by default requests no diagnostics, so none arrive.
  @Test
  void badOperationStatusIsAResultWithArgumentStatusesAndNoDefaultDiagnostics() throws UaException {
    MethodCallResult<Variant[]> result = open.callResult(new Variant[] {new Variant(ubyte(7))});

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.statusCode());
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
        result.inputArgumentResults());
    assertEquals(0, result.inputArgumentDiagnosticInfos().length);
    assertFalse(result.hasOutputs());
    assertNull(result.conversionFailure());

    UaMethodException e = assertThrows(UaMethodException.class, result::requireGood);
    assertEquals(result.statusCode(), e.getStatusCode());
    assertNotNull(e.getResponseHeader());
  }

  // Part 4 §7.8: operation-level diagnostics are returned only under the requested mask. The
  // handler's diagnostic text must reach the client as AdditionalInfo when asked for.
  @Test
  void perCallReturnDiagnosticsSurfaceTheHandlersArgumentText() throws UaException {
    MethodCallOptions options =
        MethodCallOptions.builder()
            .returnDiagnostics(
                ReturnDiagnostics.of(ReturnDiagnostics.OPERATION_LEVEL_ADDITIONAL_INFO))
            .build();

    MethodCallResult<Variant[]> result =
        open.callResult(options, new Variant[] {new Variant(ubyte(7))});

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.statusCode());
    DiagnosticInfo[] diagnostics = result.inputArgumentDiagnosticInfos();
    assertEquals(1, diagnostics.length);
    assertEquals("mode must be 0..3", diagnostics[0].additionalInfo());
  }

  // Uncertain outputs are decoded and available through the result form, while the convenient
  // call still rejects anything but Good.
  @Test
  void uncertainOutputsAreAvailableThroughTheResultFormOnly() throws UaException {
    Variant[] inputs = {new Variant(ubyte(3))};

    MethodCallResult<UInteger> result =
        open.callResult(inputs).map(outputs -> (UInteger) outputs[0].value());

    assertEquals(new StatusCode(StatusCodes.Uncertain_SubNormal), result.statusCode());
    assertTrue(result.hasOutputs());
    assertEquals(uint(103), result.outputs());

    UaMethodException e = assertThrows(UaMethodException.class, () -> open.call(inputs));
    assertEquals(new StatusCode(StatusCodes.Uncertain_SubNormal), e.getStatusCode());
    assertNotNull(e.getResponseHeader());
  }

  @Test
  void goodCallReturnsOutputsThroughBothForms() throws UaException {
    Variant[] inputs = {new Variant(ubyte(1))};

    assertEquals(uint(101), open.call(inputs)[0].value());
    assertEquals(uint(101), open.callResult(inputs).requireGood()[0].value());
  }
}

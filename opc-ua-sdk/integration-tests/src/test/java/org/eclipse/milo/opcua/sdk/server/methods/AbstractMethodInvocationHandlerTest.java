/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_ArgumentsMissing;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_InvalidArgument;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TooManyArguments;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractMethodInvocationHandlerTest extends AbstractClientServerTest {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractMethodInvocationHandlerTest.class);

  /** Captures the server-side value of the "Input" argument to {@code structArrayEcho()}. */
  private final AtomicReference<Object> structArrayEchoInput = new AtomicReference<>();

  @BeforeAll
  void configureStructArrayEcho() {
    testNamespace.configure(
        (context, nodeManager) ->
            UaMethodNode.build(
                context,
                b -> {
                  b.setNodeId(new NodeId(2, "structArrayEcho()"));
                  b.setBrowseName(new QualifiedName(2, "structArrayEcho()"));
                  b.setDisplayName(LocalizedText.english("structArrayEcho()"));

                  b.addReference(
                      new Reference(
                          b.getNodeId(),
                          NodeIds.HasOrderedComponent,
                          NodeIds.ObjectsFolder.expanded(),
                          Reference.Direction.INVERSE));

                  UaMethodNode methodNode = b.buildAndAdd();

                  methodNode.setInvocationHandler(
                      new AbstractMethodInvocationHandler(methodNode) {
                        @Override
                        public Argument[] getInputArguments() {
                          return new Argument[] {
                            new Argument(
                                "Input",
                                NodeIds.XVType,
                                ValueRanks.OneDimension,
                                null,
                                LocalizedText.NULL_VALUE)
                          };
                        }

                        @Override
                        public Argument[] getOutputArguments() {
                          return new Argument[] {
                            new Argument(
                                "Output",
                                NodeIds.XVType,
                                ValueRanks.OneDimension,
                                null,
                                LocalizedText.NULL_VALUE)
                          };
                        }

                        @Override
                        protected Variant[] invoke(
                            InvocationContext invocationContext, Variant[] inputValues) {

                          structArrayEchoInput.set(inputValues[0].value());

                          return new Variant[] {inputValues[0]};
                        }
                      });

                  return methodNode;
                }));
  }

  @Test
  public void inputArgumentResultsIsEmptyOnSuccess() throws UaException {
    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    NodeId.parse("ns=2;s=onlyAcceptsPositiveInputs()"),
                    new Variant[] {new Variant(1)})));

    CallMethodResult result = requireNonNull(response.getResults())[0];
    StatusCode[] inputArgumentResults = requireNonNull(result.getInputArgumentResults());

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(0, inputArgumentResults.length);
  }

  @Test
  public void implementationCanValidateArguments() throws UaException {
    AddressSpace addressSpace = client.getAddressSpace();

    UaObjectNode objectsNode = addressSpace.getObjectNode(NodeIds.ObjectsFolder);

    try {
      objectsNode.callMethod(
          new QualifiedName(2, "onlyAcceptsPositiveInputs()"), new Variant[] {new Variant(-1)});
    } catch (UaMethodException e) {
      LOGGER.debug("result: {}", e.getStatusCode());
      LOGGER.debug("inputArgumentResults: {}", Arrays.toString(e.getInputArgumentResults()));

      assertEquals(StatusCode.of(Bad_InvalidArgument), e.getStatusCode());
      assertEquals(StatusCode.of(Bad_OutOfRange), e.getInputArgumentResults()[0]);
    }
  }

  @Test
  void wrongNumberOfArguments() throws UaException {
    // too few arguments
    {
      CallResponse response =
          client.call(
              List.of(
                  new CallMethodRequest(
                      NodeIds.ObjectsFolder,
                      NodeId.parse("ns=2;s=onlyAcceptsPositiveInputs()"),
                      new Variant[] {})));

      CallMethodResult result = requireNonNull(response.getResults())[0];

      assertEquals(StatusCode.of(Bad_ArgumentsMissing), result.getStatusCode());
    }

    // too many arguments
    {
      CallResponse response =
          client.call(
              List.of(
                  new CallMethodRequest(
                      NodeIds.ObjectsFolder,
                      NodeId.parse("ns=2;s=onlyAcceptsPositiveInputs()"),
                      new Variant[] {new Variant(1), new Variant(2)})));

      CallMethodResult result = requireNonNull(response.getResults())[0];

      assertEquals(StatusCode.of(Bad_TooManyArguments), result.getStatusCode());
    }
  }

  @Test
  void scalarAbstractTypeEcho() throws UaException {
    Variant[] inputs =
        new Variant[] {Variant.ofInt32(42), Variant.ofUInt32(uint(42)), Variant.ofDouble(42.0)};

    for (Variant input : inputs) {
      CallResponse response =
          client.call(
              List.of(
                  new CallMethodRequest(
                      NodeIds.ObjectsFolder,
                      NodeId.parse("ns=2;s=scalarAbstractTypeEcho()"),
                      new Variant[] {input})));

      CallMethodResult result = requireNonNull(response.getResults())[0];

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertEquals(0, requireNonNull(result.getInputArgumentResults()).length);
      assertEquals(input, requireNonNull(result.getOutputArguments())[0]);
    }
  }

  @Test
  void scalarSimpleTypeEcho() throws UaException {
    Variant input = Variant.ofDouble(42.0);

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    NodeId.parse("ns=2;s=scalarSimpleTypeEcho()"),
                    new Variant[] {input})));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(0, requireNonNull(result.getInputArgumentResults()).length);
    assertEquals(input, requireNonNull(result.getOutputArguments())[0]);
  }

  @Test
  void scalarStructureEcho() throws UaException {
    var struct = new XVType(1.0, 2.0f);
    var xo = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, struct);
    var input = Variant.ofExtensionObject(xo);

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    NodeId.parse("ns=2;s=scalarStructureEcho()"),
                    new Variant[] {input})));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(0, requireNonNull(result.getInputArgumentResults()).length);

    // The handler receives and echoes the decoded struct; it is re-encoded on the wire.
    Object outputValue = requireNonNull(result.getOutputArguments())[0].value();
    ExtensionObject outputXo = assertInstanceOf(ExtensionObject.class, outputValue);
    assertEquals(struct, outputXo.decode(DefaultEncodingContext.INSTANCE));
  }

  @Test
  void scalarAbstractStructureEcho() throws UaException {
    var struct1 = new XVType(1.0, 2.0f);
    var struct2 = new ThreeDVector(1.0, 2.0, 3.0);

    for (UaStructuredType struct : List.<UaStructuredType>of(struct1, struct2)) {
      var xo = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, struct);
      var input = Variant.ofExtensionObject(xo);

      CallResponse response =
          client.call(
              List.of(
                  new CallMethodRequest(
                      NodeIds.ObjectsFolder,
                      NodeId.parse("ns=2;s=scalarAbstractStructureEcho()"),
                      new Variant[] {input})));

      CallMethodResult result = requireNonNull(response.getResults())[0];

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertEquals(0, requireNonNull(result.getInputArgumentResults()).length);

      // The handler receives and echoes the decoded struct; it is re-encoded on the wire.
      Object outputValue = requireNonNull(result.getOutputArguments())[0].value();
      ExtensionObject outputXo = assertInstanceOf(ExtensionObject.class, outputValue);
      assertEquals(struct, outputXo.decode(DefaultEncodingContext.INSTANCE));
    }

    var input3 = Variant.ofInt32(42);

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    NodeId.parse("ns=2;s=scalarAbstractStructureEcho()"),
                    new Variant[] {input3})));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.of(Bad_InvalidArgument), result.getStatusCode());
    assertEquals(
        StatusCode.of(Bad_TypeMismatch), requireNonNull(result.getInputArgumentResults())[0]);
  }

  @Test
  void structArrayEcho() throws UaException {
    structArrayEchoInput.set(null);

    var struct1 = new XVType(1.0, 2.0f);
    var struct2 = new XVType(3.0, 4.0f);
    var xo1 = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, struct1);
    var xo2 = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, struct2);
    var input = Variant.ofExtensionObjectArray(new ExtensionObject[] {xo1, xo2});

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    new NodeId(2, "structArrayEcho()"),
                    new Variant[] {input})));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(0, requireNonNull(result.getInputArgumentResults()).length);

    // Server-side, the handler received the decoded, correctly-typed struct array.
    XVType[] serverSideValue = assertInstanceOf(XVType[].class, structArrayEchoInput.get());
    assertArrayEquals(new XVType[] {struct1, struct2}, serverSideValue);

    // Client-side, the echoed array decodes element-wise to the original structs.
    Object outputValue = requireNonNull(result.getOutputArguments())[0].value();
    ExtensionObject[] outputXos = assertInstanceOf(ExtensionObject[].class, outputValue);
    assertEquals(2, outputXos.length);
    assertEquals(struct1, outputXos[0].decode(DefaultEncodingContext.INSTANCE));
    assertEquals(struct2, outputXos[1].decode(DefaultEncodingContext.INSTANCE));
  }

  @Test
  void emptyStructArrayEcho() throws UaException {
    structArrayEchoInput.set(null);

    var input = Variant.ofExtensionObjectArray(new ExtensionObject[0]);

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    new NodeId(2, "structArrayEcho()"),
                    new Variant[] {input})));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(0, requireNonNull(result.getInputArgumentResults()).length);

    // Even an empty array is delivered as the correctly-typed array.
    XVType[] serverSideValue = assertInstanceOf(XVType[].class, structArrayEchoInput.get());
    assertEquals(0, serverSideValue.length);
  }

  @Test
  void structArrayElementTypeMismatch() throws UaException {
    var xo1 = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, new XVType(1.0, 2.0f));
    var xo2 =
        ExtensionObject.encode(DefaultEncodingContext.INSTANCE, new ThreeDVector(1.0, 2.0, 3.0));
    var input = Variant.ofExtensionObjectArray(new ExtensionObject[] {xo1, xo2});

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.ObjectsFolder,
                    new NodeId(2, "structArrayEcho()"),
                    new Variant[] {input})));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCode.of(Bad_InvalidArgument), result.getStatusCode());
    assertEquals(
        StatusCode.of(Bad_TypeMismatch), requireNonNull(result.getInputArgumentResults())[0]);
  }
}

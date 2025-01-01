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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;

public class AbstractMethodInvocationHandlerTest extends AbstractClientServerTest {

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
      System.out.println("result: " + e.getStatusCode());
      System.out.println("inputArgumentResults: " + Arrays.toString(e.getInputArgumentResults()));

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
    var xo = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, new XVType(1.0, 2.0f));
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
    assertEquals(input, requireNonNull(result.getOutputArguments())[0]);
  }

  @Test
  void scalarAbstractStructureEcho() throws UaException {
    var xo1 = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, new XVType(1.0, 2.0f));
    var xo2 =
        ExtensionObject.encode(DefaultEncodingContext.INSTANCE, new ThreeDVector(1.0, 2.0, 3.0));
    var input1 = Variant.ofExtensionObject(xo1);
    var input2 = Variant.ofExtensionObject(xo2);

    for (Variant input : List.of(input1, input2)) {
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
      assertEquals(input, requireNonNull(result.getOutputArguments())[0]);
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
}

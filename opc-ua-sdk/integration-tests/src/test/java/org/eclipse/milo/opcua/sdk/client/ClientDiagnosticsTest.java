/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.sdk.server.ReportingNamespace.BLOCKED;
import static org.eclipse.milo.opcua.sdk.server.ReportingNamespace.OK;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.ReportingNamespace;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ResponseDiagnostics;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/** A client configured to request diagnostics receives them through the high-level API. */
class ClientDiagnosticsTest extends AbstractClientServerTest {

  private ReportingNamespace namespace;

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {
    configBuilder.setReturnDiagnostics(ReturnDiagnostics.ALL);
  }

  @Override
  protected void configureTestNamespace(TestNamespace testNamespace) {
    namespace =
        new ReportingNamespace(server, "urn:eclipse:milo:test:diagnostics:client", "client");
    namespace.startup();
  }

  @AfterAll
  void stopNamespace() {
    if (namespace != null) namespace.shutdown();
  }

  @Test
  void requestHeadersCarryTheConfiguredMask() {
    assertEquals(uint(0x3FF), client.newRequestHeader().getReturnDiagnostics());
  }

  // The high-level service methods use newRequestHeader, so a raw response carries diagnostics
  // that ResponseDiagnostics resolves.
  @Test
  void writeResponseDiagnosticsResolveThroughTheResponseHeader() throws UaException {
    WriteResponse response = client.write(List.of(writeValue(BLOCKED), writeValue(OK)));

    ResponseDiagnostics diagnostics = ResponseDiagnostics.of(response.getResponseHeader());
    List<@Nullable Diagnostic> perWrite = diagnostics.resolve(response.getDiagnosticInfos());

    assertEquals(Arrays.asList(namespace.diagnostic(), null), perWrite);
  }

  // UaMethodException resolves both the operation diagnostic and the argument diagnostics
  // against the Call response's string table.
  @Test
  void methodExceptionCarriesResolvedOperationAndArgumentDiagnostics() throws UaException {
    UaObjectNode owner = client.getAddressSpace().getObjectNode(namespace.ownerId());
    var methodName = new QualifiedName(namespace.getNamespaceIndex(), BLOCKED);

    UaMethodException e =
        assertThrows(
            UaMethodException.class,
            () -> owner.callMethod(methodName, new Variant[] {new Variant(1.0)}));

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), e.getStatusCode());
    assertEquals(namespace.diagnostic(), e.getDiagnostic().orElseThrow());
    assertEquals(List.of(namespace.argumentDiagnostic()), e.getInputArgumentDiagnostics());
  }

  private WriteValue writeValue(String name) {
    return new WriteValue(
        namespace.nodeId(name), AttributeId.Value.uid(), null, new DataValue(new Variant(1)));
  }
}

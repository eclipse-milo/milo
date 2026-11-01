/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsPolicy;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ResponseDiagnostics;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ServiceDiagnosticsTest {

  private static final DiagnosticsPolicy PASS_THROUGH = (request, status, diagnostic) -> diagnostic;

  private static final StatusCode NOT_WRITABLE = new StatusCode(StatusCodes.Bad_NotWritable);

  // A request without diagnostics must get exactly the response it got before diagnostics were
  // supported: empty lists, no string table, no service diagnostics.
  @Test
  void requestWithoutOperationDiagnosticsKeepsTheResponseShape() {
    ServiceDiagnostics diagnostics = diagnostics(0x01F);
    WriteValue operation = writeValue();

    OperationDiagnostics<WriteValue> collected = diagnostics.collect(List.of(operation));
    collected.report(operation, Diagnostic.text("ignored"));

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(collected, List.of(NOT_WRITABLE), Function.identity());
    ResponseHeader header = diagnostics.responseHeader(request());

    assertSame(OperationDiagnostics.none(), collected);
    assertFalse(collected.isRequested());
    assertEquals(0, diagnosticInfos.length);
    assertNull(header.getStringTable());
    assertNull(header.getServiceDiagnostics());
  }

  @Test
  void operationDiagnosticsAreOnePerOperationAndResolveThroughTheHeader() {
    ServiceDiagnostics diagnostics = diagnostics(0x3E0);
    WriteValue first = writeValue();
    WriteValue second = writeValue();

    OperationDiagnostics<WriteValue> collected = diagnostics.collect(List.of(first, second));
    collected.report(second, Diagnostic.text("blocked"));

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(
            collected, List.of(StatusCode.GOOD, NOT_WRITABLE), Function.identity());
    ResponseHeader header = diagnostics.responseHeader(request());

    assertEquals(
        Arrays.asList(null, Diagnostic.text("blocked")),
        ResponseDiagnostics.of(header).resolve(diagnosticInfos));
  }

  // A failing policy must not fail the response; the operation results are already final.
  @Test
  void policyThatThrowsSendsNoDiagnostic() {
    DiagnosticsPolicy failing =
        (request, status, diagnostic) -> {
          throw new IllegalStateException("policy bug");
        };
    var diagnostics =
        new ServiceDiagnostics(
            ReturnDiagnostics.decode(uint(0x3E0)), failing, null, Service.ATTRIBUTE_WRITE);
    WriteValue operation = writeValue();

    OperationDiagnostics<WriteValue> collected = diagnostics.collect(List.of(operation));
    collected.report(operation, Diagnostic.text("blocked"));

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(collected, List.of(NOT_WRITABLE), Function.identity());

    assertEquals(0, diagnosticInfos.length);
  }

  // A fixed budget can outgrow a client that accepts only small responses; the budget follows the
  // smaller of the server's message size and the Session's MaxResponseMessageSize.
  @ParameterizedTest
  @CsvSource({
    "0, 65536", // the server's 2 MiB limit allows the full default
    "131072, 16384", // an eighth of a 128 KiB Session limit
    "8192, 1024"
  })
  void byteBudgetFollowsTheSmallerResponseLimit(long sessionLimit, int expectedBudget) {
    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(server.getConfig()).thenReturn(config);
    when(config.getEncodingLimits()).thenReturn(EncodingLimits.DEFAULT);

    Session session = mock(Session.class);
    when(session.getMaxResponseMessageSize()).thenReturn(uint(sessionLimit));

    assertEquals(expectedBudget, ServiceDiagnostics.byteBudget(server, session));
  }

  private static ServiceDiagnostics diagnostics(int mask) {
    return new ServiceDiagnostics(
        ReturnDiagnostics.decode(uint(mask)), PASS_THROUGH, null, Service.ATTRIBUTE_WRITE);
  }

  private static WriteValue writeValue() {
    return new WriteValue(
        new NodeId(1, "node"), AttributeId.Value.uid(), null, new DataValue(new Variant(1)));
  }

  private static WriteRequest request() {
    return new WriteRequest(
        new RequestHeader(NodeId.NULL_VALUE, null, uint(7), uint(0), null, uint(0), null),
        new WriteValue[0]);
  }
}

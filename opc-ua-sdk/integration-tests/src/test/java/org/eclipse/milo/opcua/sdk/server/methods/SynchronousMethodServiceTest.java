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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsContext;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class SynchronousMethodServiceTest extends AbstractClientServerTest {
  private DiagnosticNamespace first;
  private DiagnosticNamespace second;

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    builder.setIdentityProvider(new UsernameProvider("user1", "password"));
  }

  @Override
  protected void configureTestNamespace(TestNamespace ignored) {
    first = new DiagnosticNamespace("urn:milo:test:method-diagnostics:first");
    second = new DiagnosticNamespace("urn:milo:test:method-diagnostics:second");
    first.startup();
    second.startup();
  }

  @AfterAll
  void stopNamespaces() {
    if (first != null) first.shutdown();
    if (second != null) second.shutdown();
  }

  // The request crosses two namespace dispatch groups and one denied access group.
  @Test
  void authenticatedCallSharesDiagnosticTableAcrossGroupsAndRetainsDeniedPosition()
      throws Exception {
    int deniedBefore = first.deniedCalls.get();
    CallResponse response =
        call(
            0x3e0,
            first.request("diagnostic", Variant.NULL_VALUE),
            first.request("denied", Variant.NULL_VALUE),
            second.request("diagnostic", Variant.NULL_VALUE));
    assertEquals(StatusCode.GOOD, response.getResponseHeader().getServiceResult());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), response.getResults()[1].getStatusCode());
    assertEquals(deniedBefore, first.deniedCalls.get());
    for (int index : new int[] {0, 2}) {
      CallMethodResult result = response.getResults()[index];
      assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
      assertEquals(1, result.getInputArgumentDiagnosticInfos().length);
      DiagnosticInfo info = result.getInputArgumentDiagnosticInfos()[0];
      String[] table = response.getResponseHeader().getStringTable();
      assertEquals("urn:vendor", table[info.namespaceUri()]);
      assertEquals("range", table[info.symbolicId()]);
      assertEquals("en", table[info.locale()]);
      assertEquals("outside range", table[info.localizedText()]);
      assertEquals("argument", info.additionalInfo());
    }
    assertEquals(4, response.getResponseHeader().getStringTable().length);
    assertTrue(response.getDiagnosticInfos() == null || response.getDiagnosticInfos().length == 0);
    assertTrue(
        response.getResponseHeader().getServiceDiagnostics() == null
            || response
                .getResponseHeader()
                .getServiceDiagnostics()
                .equals(DiagnosticInfo.NULL_VALUE));
  }

  @Test
  void zeroAndServiceOnlyMasksSuppressArgumentDiagnosticsOnWire() throws Exception {
    for (int mask : new int[] {0, 31}) {
      CallResponse response = call(mask, first.request("diagnostic", Variant.NULL_VALUE));
      assertEquals(
          new StatusCode(StatusCodes.Bad_InvalidArgument),
          response.getResults()[0].getStatusCode());
      DiagnosticInfo[] diagnostics = response.getResults()[0].getInputArgumentDiagnosticInfos();
      assertTrue(diagnostics == null || diagnostics.length == 0);
      String[] table = response.getResponseHeader().getStringTable();
      assertTrue(table == null || table.length == 0);
    }
  }

  @Test
  void optionalSuffixAndUncertainOutputsCrossActualCallService() throws Exception {
    CallResponse response =
        call(
            0,
            first.request("optional"),
            first.request("optional", Variant.NULL_VALUE),
            first.request("optional", new Variant("label")),
            first.request("optional", new Variant(7)));
    assertEquals(StatusCode.UNCERTAIN, response.getResults()[0].getStatusCode());
    assertArrayEquals(
        new Variant[] {new Variant("omitted")}, response.getResults()[0].getOutputArguments());
    assertEquals(StatusCode.UNCERTAIN, response.getResults()[1].getStatusCode());
    assertArrayEquals(
        new Variant[] {Variant.NULL_VALUE}, response.getResults()[1].getOutputArguments());
    assertArrayEquals(
        new Variant[] {new Variant("label")}, response.getResults()[2].getOutputArguments());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InvalidArgument), response.getResults()[3].getStatusCode());
  }

  @Test
  void malformedRequestedIndexBecomesPerOperationInternalError() throws Exception {
    CallResponse response =
        call(
            0x20,
            first.request("malformed", Variant.NULL_VALUE),
            second.request("optional", new Variant("survives")));
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), response.getResults()[0].getStatusCode());
    assertEquals(StatusCode.UNCERTAIN, response.getResults()[1].getStatusCode());
    assertArrayEquals(
        new Variant[] {new Variant("survives")}, response.getResults()[1].getOutputArguments());
  }

  @Test
  void diagnosticStringBudgetViolationReturnsInternalErrorWithoutRetry() throws Exception {
    int before = first.budgetCalls.get();
    CallResponse response =
        call(
            0x20,
            first.request("budget", Variant.NULL_VALUE),
            second.request("optional", new Variant("survives")));
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError), response.getResults()[0].getStatusCode());
    Variant[] outputs = response.getResults()[0].getOutputArguments();
    assertTrue(outputs == null || outputs.length == 0);
    assertEquals(before + 1, first.budgetCalls.get());
    assertEquals(StatusCode.UNCERTAIN, response.getResults()[1].getStatusCode());
  }

  private CallResponse call(int mask, CallMethodRequest... operations) throws Exception {
    var session = client.getSessionAsync().get(10, TimeUnit.SECONDS);
    RequestHeader base = client.newRequestHeader(session.getAuthenticationToken());
    RequestHeader header =
        new RequestHeader(
            base.getAuthenticationToken(),
            base.getTimestamp(),
            base.getRequestHandle(),
            uint(mask),
            base.getAuditEntryId(),
            base.getTimeoutHint(),
            base.getAdditionalHeader());
    return (CallResponse)
        client.sendRequestAsync(new CallRequest(header, operations)).get(10, TimeUnit.SECONDS);
  }

  private final class DiagnosticNamespace extends ManagedNamespaceWithLifecycle {
    private final UaObjectNode owner;
    private final AtomicInteger deniedCalls = new AtomicInteger();
    private final AtomicInteger budgetCalls = new AtomicInteger();

    DiagnosticNamespace(String uri) {
      super(SynchronousMethodServiceTest.this.server, uri);
      owner =
          UaObjectNode.builder(getNodeContext())
              .setNodeId(newNodeId("owner"))
              .setBrowseName(newQualifiedName("owner"))
              .setDisplayName(LocalizedText.english("owner"))
              .buildAndAdd();
      for (String name : List.of("diagnostic", "denied", "malformed", "optional", "budget")) {
        UaMethodNode method =
            UaMethodNode.builder(getNodeContext())
                .setNodeId(newNodeId(name))
                .setBrowseName(newQualifiedName(name))
                .setDisplayName(LocalizedText.english(name))
                .buildAndAdd();
        owner.addComponent(method);
        method.setUserExecutable(!name.equals("denied"));
        Argument[] inputs = {
          new Argument("Label", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
        };
        Argument[] outputs =
            name.equals("optional")
                ? new Argument[] {
                  new Argument("Label", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
                }
                : new Argument[0];
        method.setInputArguments(inputs);
        method.setOutputArguments(outputs);
        method.setInvocationHandler(
            new AbstractMethodInvocationHandler(method) {
              public Argument[] getInputArguments() {
                return inputs;
              }

              public Argument[] getOutputArguments() {
                return outputs;
              }

              protected int getRequiredInputArgumentCount(Argument[] metadata) {
                return name.equals("optional") ? 0 : 1;
              }

              protected CallMethodResult invokeResult(InvocationContext context, Variant[] values) {
                assertTrue(context.getSession().isPresent());
                if (name.equals("optional")) {
                  return new CallMethodResult(
                      StatusCode.UNCERTAIN,
                      null,
                      null,
                      values.length == 0 ? new Variant[] {new Variant("omitted")} : values);
                }
                if (name.equals("denied")) deniedCalls.incrementAndGet();
                DiagnosticsContext<CallMethodRequest> diagnostics =
                    context.getCallDiagnostics().orElseThrow();
                if (name.equals("budget")) {
                  budgetCalls.incrementAndGet();
                  diagnostics.addString(
                      "x".repeat(getServer().getConfig().getEncodingLimits().getMaxMessageSize()));
                  throw new AssertionError("budget must reject this string");
                }
                DiagnosticInfo info =
                    name.equals("malformed")
                        ? new DiagnosticInfo(-1, 999, -1, -1, null, null, null)
                        : new DiagnosticInfo(
                            diagnostics.addString("urn:vendor"),
                            diagnostics.addString("range"),
                            diagnostics.addString("en"),
                            diagnostics.addString("outside range"),
                            "argument",
                            new StatusCode(StatusCodes.Bad_OutOfRange),
                            null);
                return new CallMethodResult(
                    new StatusCode(StatusCodes.Bad_InvalidArgument),
                    new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
                    new DiagnosticInfo[] {info},
                    new Variant[0]);
              }
            });
      }
    }

    CallMethodRequest request(String name, Variant... inputs) {
      return new CallMethodRequest(owner.getNodeId(), newNodeId(name), inputs);
    }

    public void onDataItemsCreated(List<org.eclipse.milo.opcua.sdk.server.items.DataItem> items) {}

    public void onDataItemsModified(List<org.eclipse.milo.opcua.sdk.server.items.DataItem> items) {}

    public void onDataItemsDeleted(List<org.eclipse.milo.opcua.sdk.server.items.DataItem> items) {}

    public void onMonitoringModeChanged(
        List<org.eclipse.milo.opcua.sdk.server.items.MonitoredItem> items) {}
  }
}

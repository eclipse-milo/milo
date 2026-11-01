/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.sdk.server.ReportingNamespace.BLOCKED;
import static org.eclipse.milo.opcua.sdk.server.ReportingNamespace.OK;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.ResponseDiagnostics;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Operation-level diagnostics, end to end, for every service that returns them.
 *
 * <p>Each request has two operations in a {@link ReportingNamespace}: a blocked one that fails with
 * a diagnostic, and an ok one that succeeds without one.
 */
class OperationDiagnosticsServiceTest extends AbstractClientServerTest {

  private static final int ALL = 0x3FF;

  private ReportingNamespace first;
  private ReportingNamespace second;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    first = new ReportingNamespace(server, "urn:eclipse:milo:test:diagnostics:first", "first");
    second = new ReportingNamespace(server, "urn:eclipse:milo:test:diagnostics:second", "second");

    first.startup();
    second.startup();
  }

  @AfterAll
  void stopNamespaces() {
    if (first != null) first.shutdown();
    if (second != null) second.shutdown();
  }

  // Part 4 §7.32: with returnDiagnostics 0 "diagnostics information is not returned". The response
  // must look exactly as it did before diagnostics were supported, even though the address space
  // reported a diagnostic for the blocked operation.
  @ParameterizedTest(name = "{0}")
  @MethodSource("services")
  void maskZeroReturnsTheResponseWithoutDiagnostics(ServiceCase service) throws Exception {
    UaResponseMessageType response = send(0, service.request(), first);
    ResponseHeader header = response.getResponseHeader();

    assertEquals(0, lengthOf(service.diagnosticInfos().apply(response)));
    assertEquals(0, lengthOf(service.nestedDiagnosticInfos().apply(response)));
    assertNull(header.getStringTable());
    assertNull(header.getServiceDiagnostics());
  }

  // Part 4 §7.33 and the per-service parameter tables: the list has the size and order of the
  // request, with a null entry for each operation that has nothing to report.
  @ParameterizedTest(name = "{0}")
  @MethodSource("services")
  void everyFieldRequestedReturnsOneDiagnosticPerOperation(ServiceCase service) throws Exception {
    UaResponseMessageType response = send(ALL, service.request(), first);

    List<@Nullable Diagnostic> resolved =
        ResponseDiagnostics.of(response.getResponseHeader())
            .resolve(service.diagnosticInfos().apply(response));

    assertEquals(Arrays.asList(first.diagnostic(), null), resolved);
  }

  // Service-level bits do not authorize operation-level diagnostics.
  @ParameterizedTest(name = "{0}")
  @MethodSource("services")
  void serviceLevelMaskAloneReturnsNoOperationDiagnostics(ServiceCase service) throws Exception {
    UaResponseMessageType response = send(0x01F, service.request(), first);

    assertEquals(0, lengthOf(service.diagnosticInfos().apply(response)));
    assertNull(response.getResponseHeader().getStringTable());
  }

  // Part 4 §7.32: each bit selects one field. SymbolicId travels with its namespace.
  @Test
  void symbolicIdAloneReturnsOnlyTheSymbolicIdAndNamespace() throws Exception {
    var response = (ReadResponse) send(0x020, OperationDiagnosticsServiceTest::read, first);

    List<@Nullable Diagnostic> resolved =
        ResponseDiagnostics.of(response.getResponseHeader()).resolve(response.getDiagnosticInfos());

    assertEquals(
        Arrays.asList(Diagnostic.symbolic(first.getNamespaceUri(), "E_BLOCKED"), null), resolved);
  }

  // Part 4 §5.12.2.2: inputArgumentDiagnosticInfos index the Call response's string table, the
  // same one the operation diagnostics use.
  @Test
  void callArgumentDiagnosticsResolveThroughTheResponseStringTable() throws Exception {
    var response = (CallResponse) send(ALL, OperationDiagnosticsServiceTest::call, first);
    CallMethodResult blocked = response.getResults()[0];

    List<@Nullable Diagnostic> resolved =
        ResponseDiagnostics.of(response.getResponseHeader())
            .resolve(blocked.getInputArgumentDiagnosticInfos());

    assertEquals(Collections.singletonList(first.argumentDiagnostic()), resolved);
  }

  // Part 4 §5.11.5.2: HistoryUpdateResult.diagnosticInfos index the response's string table and
  // have one entry per operationResults entry.
  @Test
  void historyUpdateResultDiagnosticsResolveThroughTheResponseStringTable() throws Exception {
    var response =
        (HistoryUpdateResponse) send(ALL, OperationDiagnosticsServiceTest::historyUpdate, first);
    HistoryUpdateResult blocked = response.getResults()[0];

    List<@Nullable Diagnostic> resolved =
        ResponseDiagnostics.of(response.getResponseHeader()).resolve(blocked.getDiagnosticInfos());

    assertEquals(Arrays.asList(null, first.valueDiagnostic()), resolved);
  }

  // A request routed to two namespaces is split into groups and collated again. Each
  // diagnostic must land at its operation's position, and shared strings appear once.
  @Test
  void requestSpanningTwoNamespacesKeepsEachDiagnosticAtItsOperation() throws Exception {
    List<ReadValueId> nodes =
        List.of(
            readValueId(first, BLOCKED),
            readValueId(second, OK),
            readValueId(second, BLOCKED),
            readValueId(first, OK),
            readValueId(first, BLOCKED));

    var request =
        new ReadRequest(
            header(ALL), 0.0, TimestampsToReturn.Neither, nodes.toArray(new ReadValueId[0]));
    var response = (ReadResponse) client.sendRequestAsync(request).get(10, TimeUnit.SECONDS);

    List<@Nullable Diagnostic> resolved =
        ResponseDiagnostics.of(response.getResponseHeader()).resolve(response.getDiagnosticInfos());

    assertEquals(
        Arrays.asList(first.diagnostic(), null, second.diagnostic(), null, first.diagnostic()),
        resolved);

    String[] stringTable = response.getResponseHeader().getStringTable();
    assertEquals(
        1,
        Arrays.stream(stringTable).filter("E_BLOCKED"::equals).count(),
        "a string shared by both namespaces appears once");
  }

  /** Builds a two-operation request, blocked then ok, for one service. */
  @FunctionalInterface
  interface RequestFactory {
    UaRequestMessageType create(
        RequestHeader header, ReportingNamespace namespace, EncodingContext context);
  }

  record ServiceCase(
      String name,
      RequestFactory request,
      Function<UaResponseMessageType, DiagnosticInfo @Nullable []> diagnosticInfos,
      Function<UaResponseMessageType, DiagnosticInfo @Nullable []> nestedDiagnosticInfos) {

    ServiceCase(
        String name,
        RequestFactory request,
        Function<UaResponseMessageType, DiagnosticInfo @Nullable []> diagnosticInfos) {

      this(name, request, diagnosticInfos, response -> null);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  static Stream<ServiceCase> services() {
    return Stream.of(
        new ServiceCase(
            "Read",
            OperationDiagnosticsServiceTest::read,
            r -> ((ReadResponse) r).getDiagnosticInfos()),
        new ServiceCase(
            "Write",
            OperationDiagnosticsServiceTest::write,
            r -> ((WriteResponse) r).getDiagnosticInfos()),
        new ServiceCase(
            "HistoryRead",
            OperationDiagnosticsServiceTest::historyRead,
            r -> ((HistoryReadResponse) r).getDiagnosticInfos()),
        new ServiceCase(
            "HistoryUpdate",
            OperationDiagnosticsServiceTest::historyUpdate,
            r -> ((HistoryUpdateResponse) r).getDiagnosticInfos(),
            r -> ((HistoryUpdateResponse) r).getResults()[0].getDiagnosticInfos()),
        new ServiceCase(
            "Call",
            OperationDiagnosticsServiceTest::call,
            r -> ((CallResponse) r).getDiagnosticInfos(),
            r -> ((CallResponse) r).getResults()[0].getInputArgumentDiagnosticInfos()),
        new ServiceCase(
            "AddNodes",
            OperationDiagnosticsServiceTest::addNodes,
            r -> ((AddNodesResponse) r).getDiagnosticInfos()),
        new ServiceCase(
            "DeleteNodes",
            OperationDiagnosticsServiceTest::deleteNodes,
            r -> ((DeleteNodesResponse) r).getDiagnosticInfos()),
        new ServiceCase(
            "AddReferences",
            OperationDiagnosticsServiceTest::addReferences,
            r -> ((AddReferencesResponse) r).getDiagnosticInfos()),
        new ServiceCase(
            "DeleteReferences",
            OperationDiagnosticsServiceTest::deleteReferences,
            r -> ((DeleteReferencesResponse) r).getDiagnosticInfos()));
  }

  private static ReadRequest read(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new ReadRequest(
        header,
        0.0,
        TimestampsToReturn.Neither,
        new ReadValueId[] {readValueId(namespace, BLOCKED), readValueId(namespace, OK)});
  }

  private static WriteRequest write(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new WriteRequest(
        header, new WriteValue[] {writeValue(namespace, BLOCKED), writeValue(namespace, OK)});
  }

  private static HistoryReadRequest historyRead(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    var details =
        new ReadRawModifiedDetails(false, DateTime.MIN_VALUE, DateTime.now(), uint(0), false);

    return new HistoryReadRequest(
        header,
        ExtensionObject.encode(context, details),
        TimestampsToReturn.Neither,
        false,
        new HistoryReadValueId[] {
          historyReadValueId(namespace, BLOCKED), historyReadValueId(namespace, OK)
        });
  }

  private static HistoryUpdateRequest historyUpdate(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    var blocked =
        new UpdateDataDetails(
            namespace.nodeId(BLOCKED),
            PerformUpdateType.Insert,
            new DataValue[] {new DataValue(new Variant(1)), new DataValue(new Variant(2))});
    var ok =
        new UpdateDataDetails(namespace.nodeId(OK), PerformUpdateType.Insert, new DataValue[0]);

    return new HistoryUpdateRequest(
        header,
        new ExtensionObject[] {
          ExtensionObject.encode(context, blocked), ExtensionObject.encode(context, ok)
        });
  }

  private static CallRequest call(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new CallRequest(
        header,
        new CallMethodRequest[] {namespace.callRequest(BLOCKED), namespace.callRequest(OK)});
  }

  private static AddNodesRequest addNodes(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new AddNodesRequest(
        header, new AddNodesItem[] {addNodesItem(namespace, BLOCKED), addNodesItem(namespace, OK)});
  }

  private static DeleteNodesRequest deleteNodes(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new DeleteNodesRequest(
        header,
        new DeleteNodesItem[] {
          new DeleteNodesItem(namespace.nodeId(BLOCKED), false),
          new DeleteNodesItem(namespace.nodeId(OK), false)
        });
  }

  private static AddReferencesRequest addReferences(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new AddReferencesRequest(
        header,
        new AddReferencesItem[] {
          addReferencesItem(namespace, BLOCKED), addReferencesItem(namespace, OK)
        });
  }

  private static DeleteReferencesRequest deleteReferences(
      RequestHeader header, ReportingNamespace namespace, EncodingContext context) {

    return new DeleteReferencesRequest(
        header,
        new DeleteReferencesItem[] {
          deleteReferencesItem(namespace, BLOCKED), deleteReferencesItem(namespace, OK)
        });
  }

  private static ReadValueId readValueId(ReportingNamespace namespace, String name) {
    return new ReadValueId(
        namespace.nodeId(name), AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
  }

  private static WriteValue writeValue(ReportingNamespace namespace, String name) {
    return new WriteValue(
        namespace.nodeId(name), AttributeId.Value.uid(), null, new DataValue(new Variant(1)));
  }

  private static HistoryReadValueId historyReadValueId(ReportingNamespace namespace, String name) {
    return new HistoryReadValueId(namespace.nodeId(name), null, QualifiedName.NULL_VALUE, null);
  }

  private static AddNodesItem addNodesItem(ReportingNamespace namespace, String name) {
    return new AddNodesItem(
        NodeIds.ObjectsFolder.expanded(),
        NodeIds.Organizes,
        namespace.nodeId(name).expanded(),
        new QualifiedName(0, name),
        NodeClass.Object,
        null,
        NodeIds.BaseObjectType.expanded());
  }

  private static AddReferencesItem addReferencesItem(ReportingNamespace namespace, String name) {
    return new AddReferencesItem(
        namespace.nodeId(name),
        NodeIds.Organizes,
        true,
        null,
        namespace.nodeId(OK).expanded(),
        NodeClass.Variable);
  }

  private static DeleteReferencesItem deleteReferencesItem(
      ReportingNamespace namespace, String name) {

    return new DeleteReferencesItem(
        namespace.nodeId(name), NodeIds.Organizes, true, namespace.nodeId(OK).expanded(), false);
  }

  private UaResponseMessageType send(int mask, RequestFactory factory, ReportingNamespace namespace)
      throws Exception {

    UaRequestMessageType request =
        factory.create(header(mask), namespace, client.getStaticEncodingContext());

    return client.sendRequestAsync(request).get(10, TimeUnit.SECONDS);
  }

  private RequestHeader header(int mask) throws Exception {
    OpcUaSession session = client.getSessionAsync().get(10, TimeUnit.SECONDS);
    RequestHeader base = client.newRequestHeader(session.getAuthenticationToken());

    return new RequestHeader(
        base.getAuthenticationToken(),
        base.getTimestamp(),
        base.getRequestHandle(),
        uint(mask),
        base.getAuditEntryId(),
        base.getTimeoutHint(),
        base.getAdditionalHeader());
  }

  private static int lengthOf(Object @Nullable [] array) {
    return array == null ? 0 : array.length;
  }
}

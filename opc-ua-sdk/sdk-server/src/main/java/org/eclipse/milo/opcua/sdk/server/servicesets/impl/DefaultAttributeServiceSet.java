/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.sdk.core.util.GroupMapCollate.groupMapCollate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.HistoryUpdateContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.WriteContext;
import org.eclipse.milo.opcua.sdk.server.HistoryUpdateOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.OperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultAttributeServiceSet extends AbstractServiceSet implements AttributeServiceSet {

  private final OpcUaServer server;

  public DefaultAttributeServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public ReadResponse onRead(ServiceRequestContext context, ReadRequest request)
      throws UaException {
    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return read(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getReadCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw e;
    } finally {
      session.getSessionDiagnostics().getReadCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public HistoryReadResponse onHistoryRead(
      ServiceRequestContext context, HistoryReadRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return historyRead(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getHistoryReadCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw e;
    } finally {
      session.getSessionDiagnostics().getHistoryReadCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public WriteResponse onWrite(ServiceRequestContext context, WriteRequest request)
      throws UaException {
    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return write(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getWriteCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw e;
    } finally {
      session.getSessionDiagnostics().getWriteCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public HistoryUpdateResponse onHistoryUpdate(
      ServiceRequestContext context, HistoryUpdateRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return historyUpdate(request, session);
    } catch (UaException e) {
      session.getSessionDiagnostics().getHistoryUpdateCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw e;
    } finally {
      session.getSessionDiagnostics().getHistoryUpdateCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  private ReadResponse read(ReadRequest request, Session session) throws UaException {
    List<ReadValueId> nodesToRead = Lists.ofNullable(request.getNodesToRead());

    if (nodesToRead.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (nodesToRead.size() > server.getConfig().getLimits().getMaxNodesPerRead().longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    if (request.getMaxAge() < 0d) {
      throw new UaException(StatusCodes.Bad_MaxAgeInvalid);
    }

    if (request.getTimestampsToReturn() == null) {
      throw new UaException(StatusCodes.Bad_TimestampsToReturnInvalid);
    }

    Map<ReadValueId, AccessResult> accessResults =
        server.getAccessController().checkReadAccess(session, nodesToRead);

    ServiceDiagnostics diagnostics =
        ServiceDiagnostics.create(server, session, Service.ATTRIBUTE_READ, request);

    OperationDiagnostics<ReadValueId> operationDiagnostics = diagnostics.collect(nodesToRead);

    List<DataValue> values =
        groupMapCollate(
            nodesToRead,
            accessResults::get,
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    return Collections.nCopies(group.size(), new DataValue(denied.statusCode()));
                  } else {
                    var readContext =
                        new ReadContext(
                            server,
                            session,
                            operationDiagnostics,
                            request.getRequestHeader().getAuditEntryId(),
                            request.getRequestHeader().getTimeoutHint(),
                            request.getRequestHeader().getAdditionalHeader());

                    return server
                        .getAddressSpaceManager()
                        .read(
                            readContext,
                            request.getMaxAge(),
                            request.getTimestampsToReturn(),
                            group);
                  }
                });

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(operationDiagnostics, values, DataValue::statusCode);

    ResponseHeader header = diagnostics.responseHeader(request);

    return new ReadResponse(header, values.toArray(DataValue[]::new), diagnosticInfos);
  }

  private HistoryReadResponse historyRead(HistoryReadRequest request, Session session)
      throws UaException {
    List<HistoryReadValueId> nodesToRead = Lists.ofNullable(request.getNodesToRead());

    if (nodesToRead.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    ExtensionObject xo = request.getHistoryReadDetails();
    HistoryReadDetails details = (HistoryReadDetails) xo.decode(server.getStaticEncodingContext());

    if (nodesToRead.size() > maxNodesPerHistoryRead(details).longValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    if (request.getTimestampsToReturn() == null) {
      throw new UaException(StatusCodes.Bad_TimestampsToReturnInvalid);
    }

    ServiceDiagnostics diagnostics =
        ServiceDiagnostics.create(server, session, Service.ATTRIBUTE_HISTORY_READ, request);

    OperationDiagnostics<HistoryReadValueId> operationDiagnostics =
        diagnostics.collect(nodesToRead);

    var historyReadContext =
        new HistoryReadContext(
            server,
            session,
            operationDiagnostics,
            request.getRequestHeader().getAuditEntryId(),
            request.getRequestHeader().getTimeoutHint(),
            request.getRequestHeader().getAdditionalHeader());

    List<HistoryReadResult> results =
        server
            .getAddressSpaceManager()
            .historyRead(historyReadContext, details, request.getTimestampsToReturn(), nodesToRead);

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(
            operationDiagnostics, results, HistoryReadResult::getStatusCode);

    ResponseHeader header = diagnostics.responseHeader(request);

    return new HistoryReadResponse(
        header, results.toArray(HistoryReadResult[]::new), diagnosticInfos);
  }

  private WriteResponse write(WriteRequest request, Session session) throws UaException {
    List<WriteValue> nodesToWrite = Lists.ofNullable(request.getNodesToWrite());

    if (nodesToWrite.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (nodesToWrite.size() > server.getConfig().getLimits().getMaxNodesPerWrite().intValue()) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    Map<WriteValue, AccessResult> accessResults =
        server.getAccessController().checkWriteAccess(session, nodesToWrite);

    ServiceDiagnostics diagnostics =
        ServiceDiagnostics.create(server, session, Service.ATTRIBUTE_WRITE, request);

    OperationDiagnostics<WriteValue> operationDiagnostics = diagnostics.collect(nodesToWrite);

    List<StatusCode> results =
        groupMapCollate(
            nodesToWrite,
            accessResults::get,
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    return Collections.nCopies(group.size(), denied.statusCode());
                  } else {
                    var writeContext =
                        new WriteContext(
                            server,
                            session,
                            operationDiagnostics,
                            request.getRequestHeader().getAuditEntryId(),
                            request.getRequestHeader().getTimeoutHint(),
                            request.getRequestHeader().getAdditionalHeader());

                    return server.getAddressSpaceManager().write(writeContext, group);
                  }
                });

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(operationDiagnostics, results, Function.identity());

    ResponseHeader header = diagnostics.responseHeader(request);

    return new WriteResponse(header, results.toArray(StatusCode[]::new), diagnosticInfos);
  }

  private HistoryUpdateResponse historyUpdate(HistoryUpdateRequest request, Session session)
      throws UaException {
    var historyUpdateDetails =
        requireNonNullElse(request.getHistoryUpdateDetails(), new ExtensionObject[0]);

    if (historyUpdateDetails.length == 0) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    OpcUaServerConfigLimits limits = server.getConfig().getLimits();
    long maxDataEntries = limits.getMaxNodesPerHistoryUpdateData().longValue();
    long maxEventEntries = limits.getMaxNodesPerHistoryUpdateEvents().longValue();

    // No valid request is longer than the larger limit, so reject those before decoding anything.
    if (historyUpdateDetails.length > Math.max(maxDataEntries, maxEventEntries)) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    List<HistoryUpdateDetails> historyUpdateDetailsList =
        Stream.of(historyUpdateDetails)
            .map(xo -> (HistoryUpdateDetails) xo.decode(server.getStaticEncodingContext()))
            .collect(Collectors.toList());

    long eventEntries =
        historyUpdateDetailsList.stream()
            .filter(DefaultAttributeServiceSet::isEventHistoryUpdate)
            .count();
    long dataEntries = historyUpdateDetailsList.size() - eventEntries;

    if (dataEntries > maxDataEntries || eventEntries > maxEventEntries) {
      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    ServiceDiagnostics diagnostics =
        ServiceDiagnostics.create(server, session, Service.ATTRIBUTE_HISTORY_UPDATE, request);

    HistoryUpdateOperationDiagnostics operationDiagnostics =
        diagnostics.collectHistoryUpdate(historyUpdateDetailsList);

    var historyUpdateContext =
        new HistoryUpdateContext(
            server,
            session,
            operationDiagnostics,
            request.getRequestHeader().getAuditEntryId(),
            request.getRequestHeader().getTimeoutHint(),
            request.getRequestHeader().getAdditionalHeader());

    List<HistoryUpdateResult> results =
        server
            .getAddressSpaceManager()
            .historyUpdate(historyUpdateContext, historyUpdateDetailsList);

    HistoryUpdateResult[] normalized =
        NestedDiagnostics.normalizeHistoryUpdateResults(
            diagnostics,
            operationDiagnostics,
            results,
            server.getConfig().getEncodingLimits().getMaxRecursionDepth());

    DiagnosticInfo[] diagnosticInfos =
        diagnostics.encodeOperations(
            operationDiagnostics, Arrays.asList(normalized), HistoryUpdateResult::getStatusCode);

    ResponseHeader header = diagnostics.responseHeader(request);

    return new HistoryUpdateResponse(header, normalized, diagnosticInfos);
  }

  /**
   * Get the operation limit that applies to a HistoryRead request with the given details.
   *
   * <p>OPC 10000-5 6.3.11: MaxNodesPerHistoryReadEvents limits requests that read events, and
   * MaxNodesPerHistoryReadData limits all other requests.
   */
  private UInteger maxNodesPerHistoryRead(HistoryReadDetails details) {
    OpcUaServerConfigLimits limits = server.getConfig().getLimits();

    if (details instanceof ReadEventDetails) {
      return limits.getMaxNodesPerHistoryReadEvents();
    } else {
      return limits.getMaxNodesPerHistoryReadData();
    }
  }

  /**
   * Check whether a HistoryUpdate entry counts against MaxNodesPerHistoryUpdateEvents instead of
   * MaxNodesPerHistoryUpdateData.
   */
  private static boolean isEventHistoryUpdate(HistoryUpdateDetails details) {
    return details instanceof UpdateEventDetails || details instanceof DeleteEventDetails;
  }
}

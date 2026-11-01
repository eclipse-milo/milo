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

import static java.util.Objects.requireNonNullElse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.CallOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsPolicy;
import org.eclipse.milo.opcua.sdk.server.DiagnosticsRequest;
import org.eclipse.milo.opcua.sdk.server.HistoryUpdateOperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OperationDiagnostics;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.DiagnosticEncoder;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The diagnostics of one service response: the request's {@link ReturnDiagnostics}, the server's
 * {@link DiagnosticsPolicy}, and the one {@link DiagnosticEncoder} every list in the response
 * shares.
 *
 * <p>A service set creates one per request, before it hands operations to the address spaces, and
 * uses it on the request thread after the results are collated. When the request asks for no
 * operation-level diagnostics the collectors are {@link OperationDiagnostics#none()}, no encoder is
 * created, and the response carries empty lists and no string table, as it did before diagnostics
 * were supported.
 */
@NullMarked
final class ServiceDiagnostics {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiagnostics.class);

  private static final DiagnosticInfo[] NONE = new DiagnosticInfo[0];

  private final ReturnDiagnostics returnDiagnostics;
  private final DiagnosticsPolicy policy;
  private final DiagnosticsRequest operationRequest;
  private final int byteBudget;

  private @Nullable DiagnosticEncoder encoder;

  ServiceDiagnostics(
      ReturnDiagnostics returnDiagnostics,
      DiagnosticsPolicy policy,
      @Nullable Session session,
      Service service) {

    this(returnDiagnostics, policy, session, service, DiagnosticEncoder.DEFAULT_BYTE_BUDGET);
  }

  ServiceDiagnostics(
      ReturnDiagnostics returnDiagnostics,
      DiagnosticsPolicy policy,
      @Nullable Session session,
      Service service,
      int byteBudget) {

    this.returnDiagnostics = returnDiagnostics;
    this.policy = policy;
    this.operationRequest = new DiagnosticsRequest(session, service, Level.OPERATION);
    this.byteBudget = byteBudget;
  }

  /**
   * Create the diagnostics for a request to {@code service}.
   *
   * @param server the server handling the request.
   * @param session the activated Session the request belongs to.
   * @param service the service handling the request.
   * @param request the request.
   * @return a new {@link ServiceDiagnostics}.
   */
  static ServiceDiagnostics create(
      OpcUaServer server, Session session, Service service, UaRequestMessageType request) {

    return new ServiceDiagnostics(
        ReturnDiagnostics.decode(request.getRequestHeader().getReturnDiagnostics()),
        server.getConfig().getDiagnosticsPolicy(),
        session,
        service,
        byteBudget(server, session));
  }

  /**
   * The diagnostics budget for a response: an eighth of the smaller of the server's maximum message
   * size and the Session's MaxResponseMessageSize, and at most the encoder's default.
   */
  static int byteBudget(OpcUaServer server, Session session) {
    long limit = server.getConfig().getEncodingLimits().getMaxMessageSize();

    UInteger sessionLimit = session.getMaxResponseMessageSize();
    if (sessionLimit != null && sessionLimit.longValue() > 0) {
      limit = limit > 0 ? Math.min(limit, sessionLimit.longValue()) : sessionLimit.longValue();
    }

    if (limit > 0) {
      return (int) Math.min(DiagnosticEncoder.DEFAULT_BYTE_BUDGET, limit / 8);
    } else {
      return DiagnosticEncoder.DEFAULT_BYTE_BUDGET;
    }
  }

  ReturnDiagnostics getReturnDiagnostics() {
    return returnDiagnostics;
  }

  /**
   * Create the collector for {@code operations}, or {@link OperationDiagnostics#none()} if the
   * request asks for no operation-level diagnostics.
   */
  <T> OperationDiagnostics<T> collect(List<T> operations) {
    if (returnDiagnostics.isRequested(Level.OPERATION)) {
      return new OperationDiagnosticsCollector<>(operations);
    } else {
      return OperationDiagnostics.none();
    }
  }

  CallOperationDiagnostics collectCall(List<CallMethodRequest> operations) {
    if (returnDiagnostics.isRequested(Level.OPERATION)) {
      return new OperationDiagnosticsCollector.Call(operations);
    } else {
      return CallOperationDiagnostics.none();
    }
  }

  HistoryUpdateOperationDiagnostics collectHistoryUpdate(List<HistoryUpdateDetails> operations) {
    if (returnDiagnostics.isRequested(Level.OPERATION)) {
      return new OperationDiagnosticsCollector.HistoryUpdate(operations);
    } else {
      return HistoryUpdateOperationDiagnostics.none();
    }
  }

  /**
   * Encode the per-operation diagnostics in {@code collected}.
   *
   * @param collected the collector returned for this request.
   * @param results the result of each operation, in request order.
   * @param status the status of a non-null result.
   * @param <R> the type of the results.
   * @return the response's {@code diagnosticInfos}: empty, or one entry per operation.
   */
  <R> DiagnosticInfo[] encodeOperations(
      OperationDiagnostics<?> collected,
      List<? extends @Nullable R> results,
      Function<R, @Nullable StatusCode> status) {

    if (!(collected instanceof OperationDiagnosticsCollector<?> collector)) {
      return NONE;
    }

    var diagnostics = new ArrayList<@Nullable Diagnostic>(collector.size());
    var statuses = new ArrayList<@Nullable StatusCode>(collector.size());

    for (int i = 0; i < collector.size(); i++) {
      R result = i < results.size() ? results.get(i) : null;

      diagnostics.add(collector.get(i));
      statuses.add(result != null ? status.apply(result) : null);
    }

    return encodeAll(diagnostics, statuses);
  }

  /**
   * Get the nested diagnostics reported for the operation at {@code position}.
   *
   * @return the reported list, or {@code null} if none was reported.
   */
  @Nullable List<@Nullable Diagnostic> nested(OperationDiagnostics<?> collected, int position) {
    if (collected instanceof OperationDiagnosticsCollector<?> collector) {
      return collector.getNested(position);
    } else {
      return null;
    }
  }

  /**
   * Encode a list of operation-level diagnostics, each explaining the status at the same position.
   *
   * @param diagnostics the diagnostics, one per element.
   * @param statuses the status each diagnostic explains, one per element.
   * @return empty, or one entry per element.
   */
  DiagnosticInfo[] encodeAll(
      List<@Nullable Diagnostic> diagnostics, List<@Nullable StatusCode> statuses) {

    var filtered = new ArrayList<@Nullable Diagnostic>(diagnostics.size());
    boolean any = false;

    for (int i = 0; i < diagnostics.size(); i++) {
      StatusCode status = i < statuses.size() ? statuses.get(i) : null;
      Diagnostic diagnostic = filter(status, diagnostics.get(i));

      filtered.add(diagnostic);
      any |= diagnostic != null;
    }

    return any ? encoder().encodeAll(Level.OPERATION, filtered) : NONE;
  }

  /**
   * Create the response header, carrying the string table of everything encoded so far.
   *
   * @param request the request being answered.
   * @return the response header.
   */
  ResponseHeader responseHeader(UaRequestMessageType request) {
    String[] stringTable = encoder != null ? encoder.stringTable() : null;

    return new ResponseHeader(
        DateTime.now(),
        request.getRequestHeader().getRequestHandle(),
        StatusCode.GOOD,
        null,
        stringTable,
        null);
  }

  private @Nullable Diagnostic filter(
      @Nullable StatusCode status, @Nullable Diagnostic diagnostic) {
    if (diagnostic == null) {
      return null;
    }

    try {
      return policy.filter(
          operationRequest, requireNonNullElse(status, StatusCode.GOOD), diagnostic);
    } catch (RuntimeException e) {
      LOGGER.warn("DiagnosticsPolicy failed; sending no diagnostic", e);
      return null;
    }
  }

  private DiagnosticEncoder encoder() {
    if (encoder == null) {
      encoder = new DiagnosticEncoder(returnDiagnostics, byteBudget);
    }
    return encoder;
  }
}

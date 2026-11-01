/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves the {@link DiagnosticInfo}s of one response into {@link Diagnostic}s.
 *
 * <p>Every DiagnosticInfo in a response refers to strings by index into the string table of that
 * response's header, including those nested in operation results, such as the per-argument
 * diagnostics of a Method call. Create one {@code ResponseDiagnostics} from the response header and
 * use it for every list in the response.
 *
 * <pre>{@code
 * WriteResponse response = client.write(writeValues);
 *
 * ResponseDiagnostics diagnostics = ResponseDiagnostics.of(response.getResponseHeader());
 * List<@Nullable Diagnostic> perWrite = diagnostics.resolve(response.getDiagnosticInfos());
 * }</pre>
 *
 * <p>A server returns diagnostics only when the request asked for them; see {@link
 * ReturnDiagnostics}. Resolution tolerates malformed data from a server: an index outside the
 * string table resolves that field to {@code null} and is logged at debug level.
 */
@NullMarked
public final class ResponseDiagnostics {

  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseDiagnostics.class);

  private final String[] stringTable;
  private final @Nullable DiagnosticInfo serviceDiagnostics;

  private ResponseDiagnostics(
      String @Nullable [] stringTable, @Nullable DiagnosticInfo serviceDiagnostics) {

    this.stringTable = stringTable != null ? stringTable : new String[0];
    this.serviceDiagnostics = serviceDiagnostics;
  }

  /**
   * Create a resolver for the response with {@code responseHeader}.
   *
   * @param responseHeader the header of the response whose diagnostics to resolve.
   * @return a new {@link ResponseDiagnostics}.
   */
  public static ResponseDiagnostics of(ResponseHeader responseHeader) {
    requireNonNull(responseHeader, "responseHeader");

    return new ResponseDiagnostics(
        responseHeader.getStringTable(), responseHeader.getServiceDiagnostics());
  }

  /**
   * Resolve the service-level diagnostic of the response.
   *
   * @return the service-level diagnostic, or empty if the response has none.
   */
  public Optional<Diagnostic> service() {
    return Optional.ofNullable(resolve(serviceDiagnostics));
  }

  /**
   * Resolve a list of diagnostics, such as the per-operation {@code diagnosticInfos} of a response
   * or a list nested in an operation result.
   *
   * @param diagnosticInfos the list to resolve, or {@code null}.
   * @return one entry per element of {@code diagnosticInfos}, in the same order, with {@code null}
   *     for each element that has nothing to report; an empty list if {@code diagnosticInfos} is
   *     null or empty.
   */
  public List<@Nullable Diagnostic> resolve(DiagnosticInfo @Nullable [] diagnosticInfos) {
    if (diagnosticInfos == null || diagnosticInfos.length == 0) {
      return List.of();
    }

    var diagnostics = new ArrayList<@Nullable Diagnostic>(diagnosticInfos.length);
    for (DiagnosticInfo diagnosticInfo : diagnosticInfos) {
      diagnostics.add(resolve(diagnosticInfo));
    }

    return Collections.unmodifiableList(diagnostics);
  }

  /**
   * Resolve one diagnostic.
   *
   * @param diagnosticInfo the diagnostic to resolve, or {@code null}.
   * @return the resolved diagnostic, or {@code null} if {@code diagnosticInfo} is null or none of
   *     its fields resolve to a value.
   */
  public @Nullable Diagnostic resolve(@Nullable DiagnosticInfo diagnosticInfo) {
    if (diagnosticInfo == null) {
      return null;
    }

    String namespaceUri = lookup(diagnosticInfo.namespaceUri(), "namespaceUri");
    String symbolicId = lookup(diagnosticInfo.symbolicId(), "symbolicId");
    String locale = lookup(diagnosticInfo.locale(), "locale");
    String text = lookup(diagnosticInfo.localizedText(), "localizedText");

    LocalizedText localizedText = text != null ? new LocalizedText(locale, text) : null;

    Diagnostic innerDiagnostic = resolve(diagnosticInfo.innerDiagnosticInfo());

    if (namespaceUri == null
        && symbolicId == null
        && localizedText == null
        && diagnosticInfo.additionalInfo() == null
        && diagnosticInfo.innerStatusCode() == null
        && innerDiagnostic == null) {

      return null;
    }

    return new Diagnostic(
        namespaceUri,
        symbolicId,
        localizedText,
        diagnosticInfo.additionalInfo(),
        diagnosticInfo.innerStatusCode(),
        innerDiagnostic);
  }

  private @Nullable String lookup(int index, String field) {
    if (index == -1) {
      return null;
    }

    if (index < 0 || index >= stringTable.length) {
      LOGGER.debug(
          "DiagnosticInfo {} index {} is outside the string table of {} entries",
          field,
          index,
          stringTable.length);

      return null;
    }

    return stringTable[index];
  }
}

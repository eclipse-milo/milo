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

import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An explanation of a {@link StatusCode}, with every string resolved.
 *
 * <p>This is the application-facing form of a {@link DiagnosticInfo} (OPC 10000-4, 7.12). A {@link
 * DiagnosticInfo} holds indexes into the string table of the response that carries it, so it means
 * nothing on its own. A {@code Diagnostic} holds the strings themselves. Servers create one and
 * attach it to the failure it explains, usually by throwing a {@link UaException}; the SDK encodes
 * it into the response when the client asked for diagnostics. Clients receive one from {@link
 * UaException#getDiagnostic()} or resolve one from a raw response with {@link ResponseDiagnostics}.
 *
 * <pre>{@code
 * Diagnostic diagnostic =
 *     Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE")
 *         .withText("Writes are blocked while the controller is in RUN mode");
 *
 * throw new UaException(StatusCodes.Bad_NotWritable, "write blocked: RUN mode", diagnostic);
 * }</pre>
 *
 * <p>The factories enforce the rules of OPC 10000-4, 7.12. The canonical constructor accepts any
 * values, because a diagnostic decoded from another server may break them. Text longer than 256
 * UTF-8 bytes is truncated when it is encoded, not rejected.
 *
 * <p>A diagnostic must not contain security-related information (OPC 10000-4, 7.12), and an
 * exception's message never becomes one. Write the text for the client, not for the server log.
 *
 * @param namespaceUri the namespace that qualifies {@code symbolicId}, or {@code null}.
 * @param symbolicId a vendor-specific error or condition, at most 32 characters, or {@code null}.
 * @param localizedText text that describes {@code symbolicId}, or the status when there is no
 *     symbolic id, or {@code null}.
 * @param additionalInfo vendor-specific information, or {@code null}.
 * @param innerStatusCode the status of an inner operation, typically in an underlying system, or
 *     {@code null}.
 * @param innerDiagnostic the diagnostic that explains {@code innerStatusCode}, or {@code null}.
 */
@NullMarked
public record Diagnostic(
    @Nullable String namespaceUri,
    @Nullable String symbolicId,
    @Nullable LocalizedText localizedText,
    @Nullable String additionalInfo,
    @Nullable StatusCode innerStatusCode,
    @Nullable Diagnostic innerDiagnostic) {

  /** The longest symbolic id OPC 10000-4, 7.12 allows, in characters. */
  public static final int MAX_SYMBOLIC_ID_LENGTH = 32;

  /**
   * Create a diagnostic that carries only text, without a locale.
   *
   * @param text the text that explains the status.
   * @return a new {@link Diagnostic}.
   */
  public static Diagnostic text(String text) {
    return text(new LocalizedText(null, requireNonNull(text, "text")));
  }

  /**
   * Create a diagnostic that carries only text.
   *
   * @param text the text that explains the status.
   * @return a new {@link Diagnostic}.
   */
  public static Diagnostic text(LocalizedText text) {
    return new Diagnostic(null, null, requireNonNull(text, "text"), null, null, null);
  }

  /**
   * Create a diagnostic that identifies a vendor-specific error or condition.
   *
   * <p>Add text that describes it with {@link #withText(String)}.
   *
   * @param namespaceUri the namespace the symbolic id is defined in. It must not be the OPC UA
   *     namespace, which defines no symbolic ids.
   * @param symbolicId the error or condition, at most 32 characters. A numeric code is converted to
   *     a string such as {@code "0xC0040007"}.
   * @return a new {@link Diagnostic}.
   * @throws IllegalArgumentException if {@code namespaceUri} is empty or the OPC UA namespace, or
   *     {@code symbolicId} is empty or longer than 32 characters.
   */
  public static Diagnostic symbolic(String namespaceUri, String symbolicId) {
    requireNonNull(namespaceUri, "namespaceUri");
    requireNonNull(symbolicId, "symbolicId");

    if (namespaceUri.isEmpty()) {
      throw new IllegalArgumentException("namespaceUri must not be empty");
    }
    if (Namespaces.OPC_UA.equals(namespaceUri)) {
      throw new IllegalArgumentException(
          "namespaceUri must not be the OPC UA namespace: " + namespaceUri);
    }
    if (symbolicId.isEmpty()) {
      throw new IllegalArgumentException("symbolicId must not be empty");
    }

    int length = symbolicId.codePointCount(0, symbolicId.length());
    if (length > MAX_SYMBOLIC_ID_LENGTH) {
      throw new IllegalArgumentException(
          "symbolicId is "
              + length
              + " characters, longer than "
              + MAX_SYMBOLIC_ID_LENGTH
              + ": "
              + symbolicId);
    }

    return new Diagnostic(namespaceUri, symbolicId, null, null, null, null);
  }

  /**
   * Return a copy of this diagnostic with {@code text}, without a locale.
   *
   * @param text the text that explains the status or symbolic id.
   * @return a new {@link Diagnostic}.
   */
  public Diagnostic withText(String text) {
    return withText(new LocalizedText(null, requireNonNull(text, "text")));
  }

  /**
   * Return a copy of this diagnostic with {@code text}.
   *
   * @param text the text that explains the status or symbolic id.
   * @return a new {@link Diagnostic}.
   */
  public Diagnostic withText(LocalizedText text) {
    return new Diagnostic(
        namespaceUri,
        symbolicId,
        requireNonNull(text, "text"),
        additionalInfo,
        innerStatusCode,
        innerDiagnostic);
  }

  /**
   * Return a copy of this diagnostic with {@code additionalInfo}.
   *
   * @param additionalInfo vendor-specific information, or {@code null} to remove it.
   * @return a new {@link Diagnostic}.
   */
  public Diagnostic withAdditionalInfo(@Nullable String additionalInfo) {
    return new Diagnostic(
        namespaceUri, symbolicId, localizedText, additionalInfo, innerStatusCode, innerDiagnostic);
  }

  /**
   * Return a copy of this diagnostic with the status of an inner operation and, optionally, the
   * diagnostic that explains it.
   *
   * <p>A gateway uses this to forward the status and diagnostic of the server it called:
   *
   * <pre>{@code
   * Diagnostic forwarded =
   *     Diagnostic.text("The upstream server rejected the write")
   *         .withInner(upstreamStatus, upstreamDiagnostic);
   * }</pre>
   *
   * @param innerStatusCode the status of the inner operation.
   * @param innerDiagnostic the diagnostic that explains {@code innerStatusCode}, or {@code null}.
   * @return a new {@link Diagnostic}.
   */
  public Diagnostic withInner(StatusCode innerStatusCode, @Nullable Diagnostic innerDiagnostic) {
    return new Diagnostic(
        namespaceUri,
        symbolicId,
        localizedText,
        additionalInfo,
        requireNonNull(innerStatusCode, "innerStatusCode"),
        innerDiagnostic);
  }
}

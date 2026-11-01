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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Field;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encodes {@link Diagnostic}s into the {@link DiagnosticInfo}s and string table of one response.
 *
 * <p>This is SDK-facing: the server SDK creates one encoder per response from the request's {@link
 * ReturnDiagnostics} and passes every diagnostic of that response through it, the service-level
 * diagnostic and every per-operation or nested list alike, so that all of their indexes refer to
 * the one string table it builds. Applications report {@link Diagnostic}s and do not use this class
 * directly.
 *
 * <pre>{@code
 * var encoder = new DiagnosticEncoder(ReturnDiagnostics.decode(header.getReturnDiagnostics()));
 *
 * DiagnosticInfo[] diagnosticInfos = encoder.encodeAll(Level.OPERATION, perOperation);
 *
 * var responseHeader =
 *     new ResponseHeader(now, handle, StatusCode.GOOD, null, encoder.stringTable(), null);
 * }</pre>
 *
 * <p>The encoder enforces the limits of OPC 10000-4 and 10000-6 on what it returns:
 *
 * <ul>
 *   <li>It keeps only the fields the mask requests at the given level. A symbolic id travels with
 *       its namespace, and a localized text with its locale. Inner diagnostics are filtered by the
 *       same level's fields.
 *   <li>It adds each distinct string to the string table once, in the order it first encounters
 *       them.
 *   <li>It nests at most {@value #MAX_DEPTH} levels deep, the depth every decoder must support (OPC
 *       10000-6, 5.2.2.12), and drops anything deeper.
 *   <li>It truncates localized text to {@value #MAX_TEXT_BYTES} bytes of UTF-8 (OPC 10000-4, 7.12
 *       and 7.32).
 *   <li>It stops adding diagnostics to the response once their encoded size, including the string
 *       table and the null entries of each list, would exceed its byte budget, {@value
 *       #DEFAULT_BYTE_BUDGET} bytes by default. Every later diagnostic is dropped, and the encoder
 *       logs this once at debug level. The budget bounds what diagnostics add to a response; choose
 *       one well below the largest response the client accepts, so that diagnostics do not make a
 *       good response too large.
 * </ul>
 *
 * <p>An encoder is not thread-safe. Use it from the thread that assembles the response.
 */
@NullMarked
public final class DiagnosticEncoder {

  /** The deepest a {@link DiagnosticInfo} nests, counting the outermost entry as level 1. */
  public static final int MAX_DEPTH = 4;

  /** The longest localized text sent, in bytes of UTF-8. */
  public static final int MAX_TEXT_BYTES = 256;

  /** The default limit on the encoded size of the diagnostics in one response, in bytes. */
  public static final int DEFAULT_BYTE_BUDGET = 64 * 1024;

  private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticEncoder.class);

  private final Map<String, Integer> indexes = new HashMap<>();
  private final List<String> strings = new ArrayList<>();

  private final ReturnDiagnostics returnDiagnostics;
  private final int byteBudget;

  private int bytesUsed = 0;
  private boolean exhausted = false;

  /**
   * Create an encoder with the default byte budget.
   *
   * @param returnDiagnostics the diagnostics the request asked for.
   */
  public DiagnosticEncoder(ReturnDiagnostics returnDiagnostics) {
    this(returnDiagnostics, DEFAULT_BYTE_BUDGET);
  }

  /**
   * Create an encoder.
   *
   * @param returnDiagnostics the diagnostics the request asked for.
   * @param byteBudget the limit on the encoded size of the diagnostics in this response, in bytes.
   */
  public DiagnosticEncoder(ReturnDiagnostics returnDiagnostics, int byteBudget) {
    if (byteBudget < 0) {
      throw new IllegalArgumentException("byteBudget must not be negative: " + byteBudget);
    }

    this.returnDiagnostics = requireNonNull(returnDiagnostics, "returnDiagnostics");
    this.byteBudget = byteBudget;
  }

  /**
   * Get the diagnostics the request asked for.
   *
   * @return the {@link ReturnDiagnostics} this encoder filters by.
   */
  public ReturnDiagnostics getReturnDiagnostics() {
    return returnDiagnostics;
  }

  /**
   * Encode one diagnostic at {@code level}.
   *
   * @param level the level the diagnostic belongs to.
   * @param diagnostic the diagnostic to encode, or {@code null}.
   * @return the encoded diagnostic, or {@code null} if {@code diagnostic} is null, none of its
   *     requested fields are set, or the byte budget is spent.
   */
  public @Nullable DiagnosticInfo encode(Level level, @Nullable Diagnostic diagnostic) {
    if (diagnostic == null || exhausted) {
      return null;
    }

    Set<Field> fields = returnDiagnostics.fields(level);
    if (fields.isEmpty()) {
      return null;
    }

    Diagnostic reduced = reduce(diagnostic, fields, 1);
    if (reduced == null) {
      return null;
    }

    if (!reserve(cost(reduced, new HashSet<>()))) {
      return null;
    }

    return toDiagnosticInfo(reduced);
  }

  /**
   * Encode a list of diagnostics at {@code level}, one per operation or nested element.
   *
   * <p>Following OPC 10000-4, the result is empty if no entry has anything to return. Otherwise it
   * has one entry per element of {@code diagnostics}, in the same order, with {@code null} for each
   * element that has nothing to return.
   *
   * @param level the level the diagnostics belong to.
   * @param diagnostics the diagnostics to encode; an element may be {@code null}.
   * @return the encoded list.
   */
  public DiagnosticInfo[] encodeAll(Level level, List<? extends @Nullable Diagnostic> diagnostics) {
    if (exhausted
        || returnDiagnostics.fields(level).isEmpty()
        || diagnostics.stream().allMatch(Objects::isNull)) {

      return new DiagnosticInfo[0];
    }

    // A list that is sent costs its length and a byte for every entry, including the null ones.
    int overhead = 4 + diagnostics.size();
    if (!reserve(overhead)) {
      return new DiagnosticInfo[0];
    }

    var diagnosticInfos = new DiagnosticInfo[diagnostics.size()];
    boolean any = false;

    for (int i = 0; i < diagnosticInfos.length; i++) {
      diagnosticInfos[i] = encode(level, diagnostics.get(i));
      any |= diagnosticInfos[i] != null;
    }

    if (any) {
      return diagnosticInfos;
    } else {
      bytesUsed -= overhead;
      return new DiagnosticInfo[0];
    }
  }

  /**
   * Get the string table for the {@link ResponseHeader}, holding every string the encoded
   * diagnostics refer to.
   *
   * @return the string table, or {@code null} if no string was encoded.
   */
  public String @Nullable [] stringTable() {
    return strings.isEmpty() ? null : strings.toArray(String[]::new);
  }

  /** Charge {@code cost} bytes to the budget, or mark it spent if they do not fit. */
  private boolean reserve(int cost) {
    if (cost > byteBudget - bytesUsed) {
      exhausted = true;

      LOGGER.debug(
          "Diagnostics in this response reached the budget of {} bytes; dropping the rest",
          byteBudget);

      return false;
    }

    bytesUsed += cost;
    return true;
  }

  private static @Nullable Diagnostic reduce(Diagnostic diagnostic, Set<Field> fields, int depth) {
    String namespaceUri = null;
    String symbolicId = null;
    if (fields.contains(Field.SYMBOLIC_ID)) {
      namespaceUri = diagnostic.namespaceUri();
      symbolicId = diagnostic.symbolicId();
    }

    LocalizedText localizedText = null;
    if (fields.contains(Field.LOCALIZED_TEXT)) {
      localizedText = reduce(diagnostic.localizedText());
    }

    String additionalInfo = null;
    if (fields.contains(Field.ADDITIONAL_INFO)) {
      additionalInfo = emptyToNull(diagnostic.additionalInfo());
    }

    StatusCode innerStatusCode = null;
    if (fields.contains(Field.INNER_STATUS_CODE)) {
      innerStatusCode = diagnostic.innerStatusCode();
    }

    Diagnostic innerDiagnostic = null;
    if (fields.contains(Field.INNER_DIAGNOSTICS)
        && depth < MAX_DEPTH
        && diagnostic.innerDiagnostic() != null) {

      innerDiagnostic = reduce(diagnostic.innerDiagnostic(), fields, depth + 1);
    }

    if (namespaceUri == null
        && symbolicId == null
        && localizedText == null
        && additionalInfo == null
        && innerStatusCode == null
        && innerDiagnostic == null) {

      return null;
    }

    return new Diagnostic(
        namespaceUri, symbolicId, localizedText, additionalInfo, innerStatusCode, innerDiagnostic);
  }

  /** The text to send, truncated, with its locale; or null if there is no text to send. */
  private static @Nullable LocalizedText reduce(@Nullable LocalizedText localizedText) {
    if (localizedText == null) {
      return null;
    }

    String text = emptyToNull(localizedText.text());
    if (text == null) {
      return null;
    }

    return new LocalizedText(emptyToNull(localizedText.locale()), truncate(text, MAX_TEXT_BYTES));
  }

  /** The encoded size of {@code diagnostic}, including table entries for strings not yet added. */
  private int cost(Diagnostic diagnostic, Set<String> added) {
    int cost = 1;

    cost += stringCost(diagnostic.namespaceUri(), added);
    cost += stringCost(diagnostic.symbolicId(), added);

    LocalizedText localizedText = diagnostic.localizedText();
    if (localizedText != null) {
      cost += stringCost(localizedText.locale(), added);
      cost += stringCost(localizedText.text(), added);
    }

    if (diagnostic.additionalInfo() != null) {
      cost += 4 + utf8Length(diagnostic.additionalInfo());
    }
    if (diagnostic.innerStatusCode() != null) {
      cost += 4;
    }
    if (diagnostic.innerDiagnostic() != null) {
      cost += cost(diagnostic.innerDiagnostic(), added);
    }

    return cost;
  }

  private int stringCost(@Nullable String s, Set<String> added) {
    if (s == null) {
      return 0;
    } else if (indexes.containsKey(s) || !added.add(s)) {
      return 4;
    } else {
      return 4 + 4 + utf8Length(s);
    }
  }

  private DiagnosticInfo toDiagnosticInfo(Diagnostic diagnostic) {
    int namespaceUri = indexOf(diagnostic.namespaceUri());
    int symbolicId = indexOf(diagnostic.symbolicId());

    LocalizedText localizedText = diagnostic.localizedText();
    int locale = localizedText != null ? indexOf(localizedText.locale()) : -1;
    int text = localizedText != null ? indexOf(localizedText.text()) : -1;

    Diagnostic innerDiagnostic = diagnostic.innerDiagnostic();

    return new DiagnosticInfo(
        namespaceUri,
        symbolicId,
        locale,
        text,
        diagnostic.additionalInfo(),
        diagnostic.innerStatusCode(),
        innerDiagnostic != null ? toDiagnosticInfo(innerDiagnostic) : null);
  }

  private int indexOf(@Nullable String s) {
    if (s == null) {
      return -1;
    }

    return indexes.computeIfAbsent(
        s,
        k -> {
          strings.add(k);
          return strings.size() - 1;
        });
  }

  private static @Nullable String emptyToNull(@Nullable String s) {
    return s == null || s.isEmpty() ? null : s;
  }

  /** {@code s}, cut at a code point boundary to at most {@code maxBytes} bytes of UTF-8. */
  static String truncate(String s, int maxBytes) {
    int bytes = 0;
    int i = 0;

    while (i < s.length()) {
      int codePoint = s.codePointAt(i);
      int length = utf8Length(codePoint);

      if (bytes + length > maxBytes) {
        return s.substring(0, i);
      }

      bytes += length;
      i += Character.charCount(codePoint);
    }

    return s;
  }

  private static int utf8Length(String s) {
    int bytes = 0;
    int i = 0;

    while (i < s.length()) {
      int codePoint = s.codePointAt(i);
      bytes += utf8Length(codePoint);
      i += Character.charCount(codePoint);
    }

    return bytes;
  }

  private static int utf8Length(int codePoint) {
    if (codePoint < 0x80) {
      return 1;
    } else if (codePoint < 0x800) {
      return 2;
    } else if (codePoint < 0x10000) {
      return 3;
    } else {
      return 4;
    }
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.events.ValidationException;
import org.eclipse.milo.opcua.sdk.server.events.conversions.ImplicitConversions;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.jspecify.annotations.Nullable;

public class Like implements Operator<Boolean> {

  /**
   * Upper bound on the number of distinct parsed patterns retained, so a client that sends many
   * distinct patterns cannot grow the cache without bound.
   */
  private static final int MAX_CACHED_PATTERNS = 256;

  /**
   * Token sentinel for a {@code %} wildcard, which matches any run of characters (including none).
   */
  private static final Object STAR = new Object();

  /**
   * LRU cache of parsed patterns. Parsing is comparatively cheap, but the LIKE pattern is usually a
   * constant {@code LiteralOperand}, so caching avoids reparsing it for every event. Unlike a
   * grow-then-stop cache, an LRU keeps caching new patterns by evicting the least-recently-used
   * entry once full, so a workload with more than {@link #MAX_CACHED_PATTERNS} live patterns does
   * not fall back to parsing on every event.
   */
  private final Map<String, Object[]> patternCache =
      Collections.synchronizedMap(
          new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object[]> eldest) {
              return size() > MAX_CACHED_PATTERNS;
            }
          });

  Like() {}

  @Override
  public void validate(FilterContext context, FilterOperand[] operands) throws ValidationException {
    OperatorUtil.validateMinOperandCount(operands, 2);
  }

  @Nullable
  @Override
  public Boolean apply(
      OperatorContext context, BaseEventTypeNode eventNode, FilterOperand[] operands)
      throws UaException {

    validate(context, operands);

    Object lhs = OperatorUtil.resolve(context, eventNode, operands[0]);
    Object rhs = OperatorUtil.resolve(context, eventNode, operands[1]);

    // Three-valued logic: a null operand is indeterminate, consistent with the comparison
    // operators.
    if (lhs == null || rhs == null) {
      return null;
    }

    String value = asString(lhs);
    String pattern = asString(rhs);

    // An operand that is present but not convertible to a String cannot match; FALSE rather than
    // indeterminate, mirroring a type mismatch in the comparison operators.
    if (value == null || pattern == null) {
      return false;
    }

    try {
      return matches(value, getTokens(pattern));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private Object[] getTokens(String pattern) {
    Object[] tokens = patternCache.get(pattern);

    if (tokens == null) {
      // parse() throws IllegalArgumentException for malformed patterns; that propagates to apply().
      tokens = parse(pattern);
      patternCache.put(pattern, tokens);
    }

    return tokens;
  }

  @Nullable
  private static String asString(@Nullable Object value) {
    value = OperatorUtil.toScalarIfSingleElementArray(value);

    if (value instanceof String s) {
      return s;
    } else if (value != null) {
      Object converted = ImplicitConversions.convert(value, OpcUaDataType.String);

      return converted instanceof String s ? s : null;
    } else {
      return null;
    }
  }

  /**
   * Matches {@code text} against a parsed LIKE {@code pattern} using the classic iterative wildcard
   * algorithm. It runs in O(text.length * pattern.length) time with O(1) extra state, so unlike a
   * regex translation it cannot be driven into catastrophic backtracking (ReDoS) by a
   * client-supplied pattern such as {@code %a%a%a...}.
   */
  private static boolean matches(String text, Object[] tokens) {
    int n = text.length();
    int m = tokens.length;

    int s = 0; // index into text
    int t = 0; // index into tokens
    int starToken = -1; // most recent STAR token index, or -1 if none seen
    int starText = -1; // text index captured when that STAR was entered

    while (s < n) {
      if (t < m && tokens[t] instanceof CharMatcher matcher && matcher.matches(text.charAt(s))) {
        s++;
        t++;
      } else if (t < m && tokens[t] == STAR) {
        starToken = t;
        starText = s;
        t++;
      } else if (starToken == -1) {
        return false;
      } else {
        // Backtrack: let the most recent STAR consume one more character and retry.
        t = starToken + 1;
        starText++;
        s = starText;
      }
    }

    while (t < m && tokens[t] == STAR) {
      t++;
    }

    return t == m;
  }

  /**
   * Parses an OPC UA LIKE pattern (Part 4 Table 120) into a token array of {@link #STAR} markers
   * and {@link CharMatcher}s. Throws {@link IllegalArgumentException} for malformed patterns.
   */
  private static Object[] parse(String pattern) {
    List<Object> tokens = new ArrayList<>();

    for (int i = 0; i < pattern.length(); i++) {
      char c = pattern.charAt(i);

      switch (c) {
        case '%' -> {
          // Consecutive '%' are semantically identical to a single one; collapse them.
          if (tokens.isEmpty() || tokens.get(tokens.size() - 1) != STAR) {
            tokens.add(STAR);
          }
        }
        case '_' -> tokens.add((CharMatcher) ch -> true);
        case '\\' -> {
          if (++i >= pattern.length()) {
            throw new IllegalArgumentException("trailing escape");
          }
          char literal = pattern.charAt(i);
          tokens.add((CharMatcher) ch -> ch == literal);
        }
        case '[' -> i = parseCharacterClass(pattern, i, tokens);
        default -> tokens.add((CharMatcher) ch -> ch == c);
      }
    }

    return tokens.toArray();
  }

  /**
   * Parses a {@code [...]} character class starting at {@code start} (the {@code '['}), appends a
   * {@link CharMatcher} to {@code tokens}, and returns the index of the closing {@code ']'}.
   */
  private static int parseCharacterClass(String pattern, int start, List<Object> tokens) {
    int i = start + 1;
    boolean negated = false;

    if (i < pattern.length() && pattern.charAt(i) == '^') {
      negated = true;
      i++;
    }

    List<char[]> ranges = new ArrayList<>();
    boolean sawContent = false;

    while (i < pattern.length()) {
      char c = pattern.charAt(i);

      if (c == ']') {
        if (!sawContent) {
          throw new IllegalArgumentException("empty character class");
        }

        boolean finalNegated = negated;
        tokens.add((CharMatcher) ch -> inRanges(ranges, ch) != finalNegated);

        return i;
      }

      // Resolve the low end of a potential range, honoring '\' as a literal escape.
      char lo;
      if (c == '\\') {
        if (++i >= pattern.length()) {
          throw new IllegalArgumentException("trailing character class escape");
        }
        lo = pattern.charAt(i);
      } else {
        lo = c;
      }

      // A range is "x-y" where '-' is not the last character before ']'. A '-' adjacent to ']' is a
      // literal '-'.
      if (i + 2 < pattern.length()
          && pattern.charAt(i + 1) == '-'
          && pattern.charAt(i + 2) != ']') {

        i += 2;
        char hi;
        if (pattern.charAt(i) == '\\') {
          if (++i >= pattern.length()) {
            throw new IllegalArgumentException("trailing character class escape");
          }
          hi = pattern.charAt(i);
        } else {
          hi = pattern.charAt(i);
        }

        if (hi < lo) {
          throw new IllegalArgumentException("invalid character range: " + lo + '-' + hi);
        }

        ranges.add(new char[] {lo, hi});
      } else {
        ranges.add(new char[] {lo, lo});
      }

      sawContent = true;
      i++;
    }

    throw new IllegalArgumentException("unclosed character class");
  }

  private static boolean inRanges(List<char[]> ranges, char ch) {
    for (char[] range : ranges) {
      if (ch >= range[0] && ch <= range[1]) {
        return true;
      }
    }

    return false;
  }

  @FunctionalInterface
  private interface CharMatcher {
    boolean matches(char c);
  }
}

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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
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
   * Upper bound on the number of distinct compiled patterns retained, so a client that sends many
   * distinct patterns cannot grow the cache without bound.
   */
  private static final int MAX_CACHED_PATTERNS = 256;

  private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();

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

    String value = asString(OperatorUtil.resolve(context, eventNode, operands[0]));
    String pattern = asString(OperatorUtil.resolve(context, eventNode, operands[1]));

    if (value != null && pattern != null) {
      try {
        return getPattern(pattern).matcher(value).matches();
      } catch (IllegalArgumentException e) {
        return false;
      }
    } else {
      return false;
    }
  }

  private Pattern getPattern(String pattern) {
    Pattern compiled = patternCache.get(pattern);

    if (compiled == null) {
      // Recompiling per event is wasteful when the LIKE pattern is constant, which is the common
      // case (a LiteralOperand). toRegex throws for malformed patterns; that propagates to apply().
      compiled = Pattern.compile(toRegex(pattern), Pattern.DOTALL);

      if (patternCache.size() < MAX_CACHED_PATTERNS) {
        patternCache.putIfAbsent(pattern, compiled);
      }
    }

    return compiled;
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

  private static String toRegex(String pattern) {
    StringBuilder regex = new StringBuilder("^");

    for (int i = 0; i < pattern.length(); i++) {
      char c = pattern.charAt(i);

      switch (c) {
        case '%' -> {
          // Collapse a run of consecutive '%' into a single '.*'. Adjacent unbounded quantifiers
          // (".*.*...") are a catastrophic-backtracking (ReDoS) hazard for client-supplied
          // patterns, and consecutive '%' are semantically identical to a single one.
          regex.append(".*");
          while (i + 1 < pattern.length() && pattern.charAt(i + 1) == '%') {
            i++;
          }
        }
        case '_' -> regex.append('.');
        case '\\' -> {
          if (++i >= pattern.length()) {
            throw new IllegalArgumentException("trailing escape");
          }
          appendLiteral(regex, pattern.charAt(i));
        }
        case '[' -> i = appendCharacterClass(pattern, i, regex);
        default -> appendLiteral(regex, c);
      }
    }

    return regex.append('$').toString();
  }

  private static int appendCharacterClass(String pattern, int start, StringBuilder regex) {
    int i = start + 1;
    boolean negated = false;

    if (i < pattern.length() && pattern.charAt(i) == '^') {
      negated = true;
      i++;
    }

    StringBuilder characterClass = new StringBuilder();
    boolean sawContent = false;

    for (; i < pattern.length(); i++) {
      char c = pattern.charAt(i);

      if (c == '\\') {
        if (++i >= pattern.length()) {
          throw new IllegalArgumentException("trailing character class escape");
        }
        // Part 4 Table 120 treats '\' as literal interpretation, so escaped regex class
        // metacharacters must not become Java regex syntax.
        appendEscapedClassLiteral(characterClass, pattern.charAt(i));
        sawContent = true;
      } else if (c == ']') {
        if (!sawContent) {
          throw new IllegalArgumentException("empty character class");
        }

        regex.append('[');
        if (negated) {
          regex.append('^');
        }
        regex.append(characterClass).append(']');

        return i;
      } else {
        appendClassPatternCharacter(characterClass, c);
        sawContent = true;
      }
    }

    throw new IllegalArgumentException("unclosed character class");
  }

  private static void appendLiteral(StringBuilder regex, char c) {
    if ("\\.[]{}()*+-?^$|".indexOf(c) >= 0) {
      regex.append('\\');
    }

    regex.append(c);
  }

  private static void appendClassPatternCharacter(StringBuilder regex, char c) {
    // '&' must be escaped so an unescaped "&&" is not interpreted as Java's character-class
    // intersection operator; LIKE has no such operator (matching appendEscapedClassLiteral).
    if (c == '\\' || c == '[' || c == ']' || c == '&') {
      regex.append('\\');
    }

    regex.append(c);
  }

  private static void appendEscapedClassLiteral(StringBuilder regex, char c) {
    if (c == '\\' || c == '[' || c == ']' || c == '-' || c == '^' || c == '&') {
      regex.append('\\');
    }

    regex.append(c);
  }
}

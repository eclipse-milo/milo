/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

/**
 * Matches strings against OPC UA {@code Like} patterns (Part 4 §7.7.3, Table 120).
 *
 * <p>The pattern grammar:
 *
 * <ul>
 *   <li>{@code %} matches any run of characters, including none.
 *   <li>{@code _} matches exactly one character.
 *   <li>{@code \} escapes the next character, making it a literal.
 *   <li>{@code [...]} matches one character from a list and/or ranges, e.g. {@code [abc]} or {@code
 *       [a-z0-9]}; {@code [^...]} negates the class. Within a class, {@code \} escapes the next
 *       character.
 *   <li>Any other character matches itself.
 * </ul>
 *
 * <p>Matching is case-sensitive and compares {@code char} values directly, with no Unicode
 * normalization or locale-specific folding.
 *
 * <p>Instances are thread-safe: parsed patterns are held in an internal synchronized LRU cache, and
 * matching itself is stateless. Each instance has its own cache, so independent subsystems can
 * isolate their pattern working sets.
 *
 * <p>Malformed patterns are rejected with {@link IllegalArgumentException}; callers that need
 * lenient behavior must catch it themselves.
 */
@NullMarked
public final class LikeMatcher {

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
   * LRU cache of parsed patterns. Parsing is comparatively cheap, but the same pattern is usually
   * matched repeatedly, so caching avoids reparsing it for every match. Unlike a grow-then-stop
   * cache, an LRU keeps caching new patterns by evicting the least-recently-used entry once full,
   * so a workload with more than {@link #MAX_CACHED_PATTERNS} live patterns does not fall back to
   * parsing on every match.
   */
  private final Map<String, Object[]> patternCache =
      Collections.synchronizedMap(
          new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object[]> eldest) {
              return size() > MAX_CACHED_PATTERNS;
            }
          });

  /** Creates a matcher with its own (initially empty) pattern cache. */
  public LikeMatcher() {}

  /**
   * Matches {@code value} against {@code pattern}.
   *
   * @param value the string to test.
   * @param pattern the {@code Like} pattern to test against.
   * @return {@code true} if {@code value} matches {@code pattern}.
   * @throws IllegalArgumentException if {@code pattern} is malformed, e.g. it ends with a dangling
   *     escape, contains an empty or unclosed character class, or contains an inverted character
   *     range.
   */
  public boolean matches(String value, String pattern) {
    return matches(value, getTokens(pattern));
  }

  /**
   * Parses {@code pattern} once and returns a compiled form for repeated matching.
   *
   * <p>Prefer this over {@link #matches(String, String)} when one pattern is matched against many
   * values: the compiled form matches without touching the shared pattern cache, so a tight
   * matching loop involves no synchronization.
   *
   * @param pattern the {@code Like} pattern to compile.
   * @return the compiled form of {@code pattern}.
   * @throws IllegalArgumentException if {@code pattern} is malformed; see {@link #matches(String,
   *     String)}.
   */
  public CompiledPattern compile(String pattern) {
    return new CompiledPattern(getTokens(pattern));
  }

  /** A parsed {@code Like} pattern; immutable and safe for concurrent matching. */
  public static final class CompiledPattern {

    private final Object[] tokens;

    private CompiledPattern(Object[] tokens) {
      this.tokens = tokens;
    }

    /**
     * Matches {@code value} against this pattern.
     *
     * @param value the string to test.
     * @return {@code true} if {@code value} matches this pattern.
     */
    public boolean matches(String value) {
      return LikeMatcher.matches(value, tokens);
    }
  }

  private Object[] getTokens(String pattern) {
    Object[] tokens = patternCache.get(pattern);

    if (tokens == null) {
      // parse() throws IllegalArgumentException for malformed patterns; that propagates to the
      // caller.
      tokens = parse(pattern);
      patternCache.put(pattern, tokens);
    }

    return tokens;
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

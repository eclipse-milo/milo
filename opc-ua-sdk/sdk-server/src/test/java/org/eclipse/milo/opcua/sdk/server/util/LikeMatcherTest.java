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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LikeMatcherTest {

  private final LikeMatcher matcher = new LikeMatcher();

  @Nested
  class Wildcards {

    @Test
    void percentMatchesAnyRunOfCharactersIncludingNone() {
      // Part 4 §7.7.3 Table 120: '%' matches any string of zero or more characters.
      assertTrue(matcher.matches("event message", "event%"));
      assertTrue(matcher.matches("event", "event%"));
      assertFalse(matcher.matches("message event", "event%"));
    }

    @Test
    void underscoreMatchesExactlyOneCharacter() {
      // Part 4 §7.7.3 Table 120: '_' matches any single character; it never matches zero or two.
      assertTrue(matcher.matches("ab", "a_"));
      assertFalse(matcher.matches("a", "a_"));
      assertFalse(matcher.matches("abc", "a_"));
    }

    @Test
    void consecutivePercentsBehaveLikeASinglePercent() {
      // Part 4 §7.7.3: runs of '%' are semantically one wildcard; "a%%b" must match "ab".
      assertTrue(matcher.matches("ab", "a%%b"));
      assertTrue(matcher.matches("aXYb", "a%%b"));
    }
  }

  @Nested
  class Escapes {

    @Test
    void escapedWildcardsMatchLiterally() {
      // Part 4 §7.7.3 Table 120: '\' escapes the following character, so "\%" and "\_" are the
      // literal characters '%' and '_' rather than wildcards.
      assertTrue(matcher.matches("event%", "event\\%"));
      assertTrue(matcher.matches("event_", "event\\_"));
      assertFalse(matcher.matches("eventX", "event\\_"));
    }

    @Test
    void escapedBackslashMatchesLiteralBackslash() {
      // Part 4 §7.7.3: the escape character itself must be escapable, so "\\" is one literal '\'.
      assertTrue(matcher.matches("a\\b", "a\\\\b"));
      assertFalse(matcher.matches("ab", "a\\\\b"));
    }
  }

  @Nested
  class CharacterClasses {

    @Test
    void listMatchesAnySingleListedCharacter() {
      // Part 4 §7.7.3 Table 120: "[abc]" matches any single character in the list.
      assertTrue(matcher.matches("cat", "c[ao]t"));
      assertTrue(matcher.matches("cot", "c[ao]t"));
      assertFalse(matcher.matches("cut", "c[ao]t"));
    }

    @Test
    void rangeMatchesAnySingleCharacterWithinTheRange() {
      // Part 4 §7.7.3 Table 120: "[a-f]" matches any single character in the inclusive range.
      assertTrue(matcher.matches("cat", "c[a-f]t"));
      assertTrue(matcher.matches("cft", "c[a-f]t"));
      assertFalse(matcher.matches("czt", "c[a-f]t"));
    }

    @Test
    void negatedClassMatchesAnySingleCharacterOutsideTheClass() {
      // Part 4 §7.7.3 Table 120: "[^...]" matches any single character NOT in the list/range.
      assertTrue(matcher.matches("cot", "c[^a]t"));
      assertFalse(matcher.matches("cat", "c[^a]t"));
    }

    @Test
    void escapedClassMetacharactersMatchLiterally() {
      // Part 4 §7.7.3: inside a class, '\' escapes '-', ']', '^', and '\' so they lose their
      // special meaning.
      assertTrue(matcher.matches("-", "[a\\-c]"));
      assertFalse(matcher.matches("b", "[a\\-c]"));

      assertTrue(matcher.matches("]", "[\\]]"));
      assertFalse(matcher.matches("[", "[\\]]"));

      assertTrue(matcher.matches("^", "[\\^a]"));
      assertFalse(matcher.matches("b", "[\\^a]"));

      assertTrue(matcher.matches("\\", "[\\\\]"));
      assertFalse(matcher.matches("a", "[\\\\]"));
    }

    @Test
    void hyphenAdjacentToClosingBracketIsLiteral() {
      // Part 4 §7.7.3: '-' only forms a range between two characters; before ']' it is a literal.
      assertTrue(matcher.matches("-", "[a-]"));
      assertTrue(matcher.matches("a", "[a-]"));
      assertFalse(matcher.matches("b", "[a-]"));
    }
  }

  @Nested
  class CaseSensitivity {

    @Test
    void matchingIsCaseSensitive() {
      // Part 4 §7.7.3 defines Like over character values with no case folding, so "event%" must
      // not match a value that differs only in case.
      assertFalse(matcher.matches("Event message", "event%"));
      assertTrue(matcher.matches("Event message", "Event%"));
    }
  }

  @Nested
  class MalformedPatterns {

    @Test
    void unclosedCharacterClassThrows() {
      // A '[' with no matching ']' is not valid Part 4 §7.7.3 grammar; the matcher must reject it
      // rather than guess at intent.
      assertThrows(IllegalArgumentException.class, () -> matcher.matches("abc", "a[b"));
    }

    @Test
    void trailingEscapeThrows() {
      // A '\' with nothing to escape is not valid Part 4 §7.7.3 grammar.
      assertThrows(IllegalArgumentException.class, () -> matcher.matches("abc", "abc\\"));
    }

    @Test
    void emptyCharacterClassThrows() {
      // "[]" has no members; there is no character it could match, so it is rejected as malformed.
      assertThrows(IllegalArgumentException.class, () -> matcher.matches("abc", "a[]c"));
    }

    @Test
    void trailingEscapeInsideCharacterClassThrows() {
      // A '\' at the end of the pattern inside a class has nothing to escape.
      assertThrows(IllegalArgumentException.class, () -> matcher.matches("abc", "a[b\\"));
    }

    @Test
    void invertedCharacterRangeThrows() {
      // A range whose high end is below its low end matches nothing; it is rejected as malformed.
      assertThrows(IllegalArgumentException.class, () -> matcher.matches("abc", "a[z-a]c"));
    }
  }

  @Nested
  class Performance {

    @Test
    void pathologicalPatternMatchesInLinearTime() {
      // A regex translation of this pattern (many "%" separated by literals) would backtrack
      // catastrophically against a long non-matching input; the iterative matcher must return
      // promptly because Like patterns are client-supplied (ReDoS surface).
      String value = "a".repeat(100_000);

      assertTimeoutPreemptively(
          Duration.ofSeconds(5),
          () -> assertFalse(matcher.matches(value, "%a%a%a%a%a%a%a%a%a%aZ")));
    }
  }
}

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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Field;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DiagnosticEncoderTest {

  /** A diagnostic with every field set, nested three deep. */
  private static final Diagnostic FULL =
      new Diagnostic(
          "urn:acme:plc",
          "E_RUN_MODE",
          new LocalizedText("en", "Blocked in RUN mode"),
          "slot 3",
          new StatusCode(StatusCodes.Bad_DeviceFailure),
          new Diagnostic(
              "urn:acme:io",
              "E_LINK",
              new LocalizedText("de", "Verbindung unterbrochen"),
              "rack 1",
              new StatusCode(StatusCodes.Bad_CommunicationError),
              new Diagnostic(
                  "urn:acme:fieldbus",
                  "E_CRC",
                  new LocalizedText("en", "CRC error"),
                  "frame 42",
                  new StatusCode(StatusCodes.Bad_Timeout),
                  null)));

  @Nested
  class RoundTrip {

    // Part 4 §7.32 and §7.33: the server returns exactly the requested fields at each level, and
    // every string resolves through the response's one string table. Exhaustive over the mask
    // because stacks get individual bit combinations wrong.
    @Test
    void everyMaskReturnsExactlyTheRequestedFieldsAtEachLevel() {
      for (int mask = 0; mask < 1024; mask++) {
        ReturnDiagnostics returnDiagnostics = ReturnDiagnostics.decode(uint(mask));
        var encoder = new DiagnosticEncoder(returnDiagnostics);

        DiagnosticInfo service = encoder.encode(Level.SERVICE, FULL);
        DiagnosticInfo[] operations = encoder.encodeAll(Level.OPERATION, List.of(FULL));

        ResponseDiagnostics resolver = ResponseDiagnostics.of(header(service, encoder));

        Diagnostic expectedService = expected(FULL, returnDiagnostics.serviceLevel());
        Diagnostic expectedOperation = expected(FULL, returnDiagnostics.operationLevel());

        assertEquals(
            expectedService,
            resolver.service().orElse(null),
            "service level, mask 0x%03X".formatted(mask));
        assertEquals(
            expectedOperation == null ? List.of() : Collections.singletonList(expectedOperation),
            resolver.resolve(operations),
            "operation level, mask 0x%03X".formatted(mask));
      }
    }
  }

  @Nested
  class Mask {

    static Stream<Arguments> singleFields() {
      return Stream.of(Level.values())
          .flatMap(level -> Stream.of(Field.values()).map(field -> Arguments.of(level, field)));
    }

    // Each bit on its own returns only its own field, at its own level and nowhere else.
    @ParameterizedTest
    @MethodSource("singleFields")
    void eachFieldOnItsOwnReturnsOnlyThatField(Level level, Field field) {
      ReturnDiagnostics returnDiagnostics =
          level == Level.SERVICE
              ? new ReturnDiagnostics(Set.of(field), Set.of())
              : new ReturnDiagnostics(Set.of(), Set.of(field));

      var encoder = new DiagnosticEncoder(returnDiagnostics);
      Level other = level == Level.SERVICE ? Level.OPERATION : Level.SERVICE;

      DiagnosticInfo encoded = encoder.encode(level, FULL);
      DiagnosticInfo atOtherLevel = encoder.encode(other, FULL);

      Diagnostic resolved = ResponseDiagnostics.of(header(null, encoder)).resolve(encoded);

      assertEquals(expected(FULL, Set.of(field)), resolved);
      assertNull(atOtherLevel, "a field requested at one level must not appear at the other");
    }

    // Part 4 §7.32: inner diagnostics' content "is determined by other bits in the mask". With no
    // other bit set there is nothing to return. node-opcua inverts this bit and returns the inner
    // entry unfiltered.
    @Test
    void innerDiagnosticsAloneReturnsNothing() {
      var encoder =
          new DiagnosticEncoder(new ReturnDiagnostics(Set.of(Field.INNER_DIAGNOSTICS), Set.of()));

      assertNull(encoder.encode(Level.SERVICE, FULL));
      assertNull(encoder.stringTable());
    }

    @Test
    void innerDiagnosticsAreFilteredByTheOtherFieldsAtTheSameLevel() {
      var encoder =
          new DiagnosticEncoder(
              new ReturnDiagnostics(
                  Set.of(Field.INNER_DIAGNOSTICS, Field.SYMBOLIC_ID),
                  Set.of(Field.ADDITIONAL_INFO)));

      DiagnosticInfo encoded = encoder.encode(Level.SERVICE, FULL);

      assertNotNull(encoded);
      assertNotNull(encoded.innerDiagnosticInfo());
      assertNull(
          encoded.innerDiagnosticInfo().additionalInfo(),
          "an operation-level field must not leak into a service-level inner entry");
      assertTrue(encoded.innerDiagnosticInfo().symbolicId() >= 0);
    }

    // The inner entry is returned only when Inner Diagnostics is requested; requesting the inner
    // status code alone returns the status without nesting.
    @Test
    void innerStatusCodeWithoutInnerDiagnosticsDoesNotNest() {
      var encoder =
          new DiagnosticEncoder(new ReturnDiagnostics(Set.of(Field.INNER_STATUS_CODE), Set.of()));

      DiagnosticInfo encoded = encoder.encode(Level.SERVICE, FULL);

      assertNotNull(encoded);
      assertEquals(FULL.innerStatusCode(), encoded.innerStatusCode());
      assertNull(encoded.innerDiagnosticInfo());
    }

    // Part 4 §7.32: "If none are requested, as indicated by a 0 value ... then diagnostics
    // information is not returned." Both UA-.NETStandard and node-opcua mishandle this case.
    @Test
    void nothingRequestedReturnsNothing() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.NONE);

      assertNull(encoder.encode(Level.SERVICE, FULL));
      assertArrayEquals(new DiagnosticInfo[0], encoder.encodeAll(Level.OPERATION, List.of(FULL)));
      assertNull(encoder.stringTable(), "no string table when nothing is returned");
    }

    // A diagnostic whose requested fields are all unset has nothing to return.
    @Test
    void diagnosticWithoutRequestedFieldsReturnsNothing() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.of(Field.ADDITIONAL_INFO));

      assertNull(encoder.encode(Level.OPERATION, Diagnostic.text("only text")));
    }
  }

  @Nested
  class StringTable {

    // Part 4 §7.33: "one string in this list for each unique namespace, symbolic identifier, and
    // localized text string contained in all of the diagnostics information parameters".
    @Test
    void stringsSharedAcrossDiagnosticsAppearOnce() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);

      Diagnostic first = Diagnostic.symbolic("urn:acme:plc", "E_ONE").withText("same text");
      Diagnostic second = Diagnostic.symbolic("urn:acme:plc", "E_TWO").withText("same text");

      DiagnosticInfo[] encoded = encoder.encodeAll(Level.OPERATION, List.of(first, second));

      assertArrayEquals(
          new String[] {"urn:acme:plc", "E_ONE", "same text", "E_TWO"}, encoder.stringTable());
      assertEquals(encoded[0].namespaceUri(), encoded[1].namespaceUri());
      assertEquals(encoded[0].localizedText(), encoded[1].localizedText());
    }

    // The service-level entry and every list in a response index the same table.
    @Test
    void serviceAndOperationLevelsShareOneTable() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);

      DiagnosticInfo[] operations =
          encoder.encodeAll(Level.OPERATION, List.of(Diagnostic.text("shared")));
      DiagnosticInfo service = encoder.encode(Level.SERVICE, Diagnostic.text("shared"));

      assertNotNull(service);
      assertEquals(operations[0].localizedText(), service.localizedText());
      assertArrayEquals(new String[] {"shared"}, encoder.stringTable());
    }

    // A localized text with no text carries nothing; its locale must not reach the table.
    @Test
    void localeWithoutTextIsNotSent() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);

      Diagnostic diagnostic =
          new Diagnostic(null, null, new LocalizedText("en", null), null, null, null);

      assertNull(encoder.encode(Level.OPERATION, diagnostic));
      assertNull(encoder.stringTable());
    }
  }

  @Nested
  class ListShape {

    // Part 4 §5.11.2.2: a diagnostics list has "the size and order of" the request list, with
    // null entries for operations that have nothing to report.
    @Test
    void operationsWithoutDiagnosticsAreNullEntries() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);

      DiagnosticInfo[] encoded =
          encoder.encodeAll(Level.OPERATION, Arrays.asList(null, Diagnostic.text("second"), null));

      assertEquals(3, encoded.length);
      assertNull(encoded[0]);
      assertNotNull(encoded[1]);
      assertNull(encoded[2]);
    }

    // Part 4 §5.11.2.2: the list is "empty if ... no diagnostic information was encountered".
    @Test
    void listIsEmptyWhenNoOperationHasAnything() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);

      assertArrayEquals(
          new DiagnosticInfo[0], encoder.encodeAll(Level.OPERATION, Arrays.asList(null, null)));
    }
  }

  @Nested
  class Limits {

    // Part 6 §5.2.2.12: "Decoders shall support at least 4 recursion levels". Deeper nesting
    // risks a decode failure at a conforming client.
    @Test
    void nestingIsCappedAtFourLevels() {
      Diagnostic chain = Diagnostic.text("level 6");
      for (int level = 5; level >= 1; level--) {
        chain = Diagnostic.text("level " + level).withInner(StatusCode.BAD, chain);
      }

      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);
      DiagnosticInfo encoded = encoder.encode(Level.SERVICE, chain);

      int depth = 0;
      for (DiagnosticInfo d = encoded; d != null; d = d.innerDiagnosticInfo()) {
        depth++;
      }

      assertEquals(4, depth);
      assertEquals(
          List.of("level 1", "level 2", "level 3", "level 4"),
          Arrays.asList(encoder.stringTable()));
    }

    // Part 4 §7.12 limits localized text to 256 characters and §7.32 to 256 bytes; 256 bytes of
    // UTF-8 satisfies both. Truncation must not split a multi-byte character.
    @Test
    void localizedTextIsTruncatedTo256BytesAtACharacterBoundary() {
      String text = "a" + "€".repeat(100);

      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);
      encoder.encode(Level.SERVICE, Diagnostic.text(text));

      String sent = encoder.stringTable()[0];

      assertEquals("a" + "€".repeat(85), sent);
      assertTrue(sent.getBytes(UTF_8).length <= 256);
    }

    @Test
    void textWithin256BytesIsSentUnchanged() {
      String text = "x".repeat(256);

      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL);
      encoder.encode(Level.SERVICE, Diagnostic.text(text));

      assertEquals(text, encoder.stringTable()[0]);
    }

    // Diagnostics must never turn a good response into Bad_ResponseTooLarge. Once the budget is
    // spent every further diagnostic is dropped, even a small one, and no string of a dropped
    // diagnostic reaches the table.
    @Test
    void diagnosticsBeyondTheByteBudgetAreDropped() {
      var encoder = new DiagnosticEncoder(ReturnDiagnostics.ALL, 100);

      DiagnosticInfo first = encoder.encode(Level.OPERATION, Diagnostic.text("x".repeat(40)));
      DiagnosticInfo second = encoder.encode(Level.OPERATION, Diagnostic.text("y".repeat(80)));
      DiagnosticInfo third = encoder.encode(Level.OPERATION, Diagnostic.text("z"));

      assertNotNull(first, "control: a diagnostic within the budget is sent");
      assertNull(second);
      assertNull(third, "every diagnostic after the budget is spent is dropped");
      assertArrayEquals(new String[] {"x".repeat(40)}, encoder.stringTable());
    }

    // Each entry of a list costs its mask byte once: two diagnostics holding only an inner status
    // code need the array length (4) plus 1 + 4 bytes each, 14 bytes in all.
    @Test
    void listEntriesAreChargedExactlyOnce() {
      var statusOnly = new Diagnostic(null, null, null, null, StatusCode.BAD, null);
      List<Diagnostic> diagnostics = List.of(statusOnly, statusOnly);

      var exact = new DiagnosticEncoder(ReturnDiagnostics.ALL, 14);
      var oneShort = new DiagnosticEncoder(ReturnDiagnostics.ALL, 13);

      DiagnosticInfo[] fits = exact.encodeAll(Level.OPERATION, diagnostics);
      DiagnosticInfo[] cut = oneShort.encodeAll(Level.OPERATION, diagnostics);

      assertNotNull(fits[1], "both entries fit a budget of exactly 14 bytes");
      assertNotNull(cut[0], "control: the first entry fits 13 bytes");
      assertNull(cut[1], "the second entry does not");
    }

    // Requested fields decide whether a list is sent. A list whose entries carry none of them is
    // empty and must not spend budget that a later diagnostic, such as the service-level one,
    // needs.
    @Test
    void listWithNothingToSendLeavesTheBudgetForLaterDiagnostics() {
      var encoder =
          new DiagnosticEncoder(
              new ReturnDiagnostics(Set.of(Field.LOCALIZED_TEXT), Set.of(Field.SYMBOLIC_ID)), 20);

      DiagnosticInfo[] operations =
          encoder.encodeAll(
              Level.OPERATION, Collections.nCopies(50, Diagnostic.text("no symbolic id")));
      DiagnosticInfo service = encoder.encode(Level.SERVICE, Diagnostic.text("ok"));

      assertArrayEquals(new DiagnosticInfo[0], operations);
      assertNotNull(service);
    }

    // A list that is sent carries a null entry for every other operation. In a large request
    // those entries can outweigh the diagnostic itself, so they count against the budget.
    @Test
    void nullEntriesOfAListCountAgainstTheByteBudget() {
      var diagnostics = new ArrayList<@Nullable Diagnostic>(Collections.nCopies(100, null));
      diagnostics.set(0, Diagnostic.text("x"));

      var withinBudget = new DiagnosticEncoder(ReturnDiagnostics.ALL, 200);
      var overBudget = new DiagnosticEncoder(ReturnDiagnostics.ALL, 50);

      assertEquals(
          100,
          withinBudget.encodeAll(Level.OPERATION, diagnostics).length,
          "control: the list fits a larger budget");
      assertArrayEquals(new DiagnosticInfo[0], overBudget.encodeAll(Level.OPERATION, diagnostics));
      assertNull(overBudget.stringTable());
    }
  }

  /** The fields of {@code diagnostic} a client gets when it requests {@code fields}. */
  private static @Nullable Diagnostic expected(@Nullable Diagnostic diagnostic, Set<Field> fields) {
    if (diagnostic == null) {
      return null;
    }

    boolean symbolicId = fields.contains(Field.SYMBOLIC_ID);
    boolean localizedText = fields.contains(Field.LOCALIZED_TEXT);
    boolean additionalInfo = fields.contains(Field.ADDITIONAL_INFO);
    boolean innerStatusCode = fields.contains(Field.INNER_STATUS_CODE);
    boolean innerDiagnostics = fields.contains(Field.INNER_DIAGNOSTICS);

    var reduced =
        new Diagnostic(
            symbolicId ? diagnostic.namespaceUri() : null,
            symbolicId ? diagnostic.symbolicId() : null,
            localizedText ? diagnostic.localizedText() : null,
            additionalInfo ? diagnostic.additionalInfo() : null,
            innerStatusCode ? diagnostic.innerStatusCode() : null,
            innerDiagnostics ? expected(diagnostic.innerDiagnostic(), fields) : null);

    return reduced.equals(new Diagnostic(null, null, null, null, null, null)) ? null : reduced;
  }

  private static ResponseHeader header(
      @Nullable DiagnosticInfo serviceDiagnostics, DiagnosticEncoder encoder) {

    return new ResponseHeader(
        DateTime.now(), uint(1), StatusCode.GOOD, serviceDiagnostics, encoder.stringTable(), null);
  }
}

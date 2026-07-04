/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubCounter;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubDiagnosticsExposure.RegisteredComponent;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.junit.jupiter.api.Test;

/**
 * The pure §9.1.11 value-computation layer of {@link PubSubDiagnosticsExposure}: the UInt32
 * saturation clamp with SourceTimestamp-advances-at-cap (§9.1.11.5, the S17 test seam), the UInt16
 * count clamp, the 16-bit DataSetMessage sequence narrowing (D12), the per-subtype Total*
 * classification sums over spec-exposed counters only (R14: vendor counters never participate), the
 * D37 earliest-contributor TimeFirstChange, and the SubError direct-child rule (§9.1.11.2 "the next
 * PubSub layer" — a grandchild never sets the parent's SubError).
 */
class PubSubDiagnosticsComputationTest {

  @Test
  void uint32ClampSaturatesAtTheMaximum() {
    assertEquals(UInteger.MIN, PubSubDiagnosticsExposure.clampUInt32(0L));
    assertEquals(uint(1234), PubSubDiagnosticsExposure.clampUInt32(1234L));
    assertEquals(UInteger.MAX, PubSubDiagnosticsExposure.clampUInt32(0xFFFFFFFFL));
    assertEquals(UInteger.MAX, PubSubDiagnosticsExposure.clampUInt32(0xFFFFFFFFL + 17L));
    assertEquals(UInteger.MAX, PubSubDiagnosticsExposure.clampUInt32(Long.MAX_VALUE));
    assertEquals(UInteger.MIN, PubSubDiagnosticsExposure.clampUInt32(-1L));
  }

  @Test
  void counterValueKeepsTheSourceTimestampAdvancingAtTheCap() throws InterruptedException {
    DataValue first = PubSubDiagnosticsExposure.counterValue(Long.MAX_VALUE);
    Thread.sleep(10);
    DataValue second = PubSubDiagnosticsExposure.counterValue(Long.MAX_VALUE);

    // the value sits pinned at 0xFFFFFFFF while the SourceTimestamp keeps updating (§9.1.11.5)
    assertEquals(UInteger.MAX, first.getValue().getValue());
    assertEquals(UInteger.MAX, second.getValue().getValue());
    assertTrue(
        second.getSourceTime().getUtcTime() > first.getSourceTime().getUtcTime(),
        "SourceTimestamp must advance at the cap");
  }

  @Test
  void countValueClampsAtTheUInt16Maximum() {
    assertEquals(ushort(3), PubSubDiagnosticsExposure.countValue(3).getValue().getValue());
    assertEquals(
        ushort(0xFFFF), PubSubDiagnosticsExposure.countValue(70_000L).getValue().getValue());
  }

  @Test
  void sequenceNumberNarrowsToSixteenBits() {
    // JSON mappings count 32-bit on the wire; the UInt16 LiveValue serves the low 16 bits (D12)
    assertEquals(
        ushort(7), PubSubDiagnosticsExposure.sequenceNumberValue(uint(7)).getValue().getValue());
    assertEquals(
        ushort(0xFFFF),
        PubSubDiagnosticsExposure.sequenceNumberValue(uint(0x1FFFF)).getValue().getValue());
    assertEquals(
        ushort(1),
        PubSubDiagnosticsExposure.sequenceNumberValue(uint(0x10001)).getValue().getValue());
  }

  @Test
  void classificationSumsCoverOnlySpecExposedCounters() {
    ComponentDiagnostics diagnostics = diagnostics();

    // Information: the five Information State* counters plus the per-subtype traffic counter
    long stateInformation = 3 + 5 + 7 + 11 + 13;

    assertEquals(
        stateInformation,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.CONNECTION,
            diagnostics,
            PubSubDiagnosticsCounterClassification.Information));
    assertEquals(
        stateInformation + 100,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.WRITER_GROUP,
            diagnostics,
            PubSubDiagnosticsCounterClassification.Information));
    assertEquals(
        stateInformation + 200,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.READER_GROUP,
            diagnostics,
            PubSubDiagnosticsCounterClassification.Information));

    // Error: StateError plus the per-subtype error counters; vendor counters (sourceErrors,
    // sequence/security drops, dataSetMessages*) never participate (R14)
    assertEquals(
        2,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.CONNECTION, diagnostics, PubSubDiagnosticsCounterClassification.Error));
    assertEquals(
        2 + 40 + 50,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.WRITER_GROUP, diagnostics, PubSubDiagnosticsCounterClassification.Error));
    assertEquals(
        2 + 60,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.READER_GROUP, diagnostics, PubSubDiagnosticsCounterClassification.Error));
    assertEquals(
        2 + 70,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.DATA_SET_WRITER,
            diagnostics,
            PubSubDiagnosticsCounterClassification.Error));
    // the DSR FailedDataSetMessages row maps to the per-reader decodeErrors substance
    assertEquals(
        2 + 30,
        PubSubDiagnosticsExposure.classificationSum(
            ComponentType.DATA_SET_READER,
            diagnostics,
            PubSubDiagnosticsCounterClassification.Error));
  }

  @Test
  void earliestTimeFirstChangeIsTheEarliestContributorAndNullWhileAllZero() {
    var early = new DateTime(1_000_000L);
    var late = new DateTime(2_000_000L);

    ComponentDiagnostics diagnostics =
        diagnostics(
            Map.of(
                PubSubCounter.NETWORK_MESSAGES_SENT, late,
                PubSubCounter.STATE_OPERATIONAL_BY_METHOD, early,
                PubSubCounter.STATE_ERROR, late));

    assertEquals(
        early,
        PubSubDiagnosticsExposure.earliestTimeFirstChange(
            ComponentType.WRITER_GROUP,
            diagnostics,
            PubSubDiagnosticsCounterClassification.Information));
    assertEquals(
        late,
        PubSubDiagnosticsExposure.earliestTimeFirstChange(
            ComponentType.WRITER_GROUP, diagnostics, PubSubDiagnosticsCounterClassification.Error));

    assertNull(
        PubSubDiagnosticsExposure.earliestTimeFirstChange(
            ComponentType.WRITER_GROUP,
            diagnostics(Map.of()),
            PubSubDiagnosticsCounterClassification.Information));
  }

  @Test
  void subErrorChildRelationIsTheDirectChildLayerOnly() {
    var connection = new RegisteredComponent(ComponentType.CONNECTION, "conn", "conn", null, null);
    var group =
        new RegisteredComponent(ComponentType.WRITER_GROUP, "conn/grp", "conn", "grp", null);
    var otherGroup =
        new RegisteredComponent(ComponentType.WRITER_GROUP, "other/grp", "other", "grp", null);
    var writer =
        new RegisteredComponent(
            ComponentType.DATA_SET_WRITER, "conn/grp/writer", "conn", "grp", "writer");
    var otherWriter =
        new RegisteredComponent(
            ComponentType.DATA_SET_WRITER, "conn/grp2/writer", "conn", "grp2", "writer");
    var readerGroup =
        new RegisteredComponent(ComponentType.READER_GROUP, "conn/rgrp", "conn", "rgrp", null);
    var reader =
        new RegisteredComponent(
            ComponentType.DATA_SET_READER, "conn/rgrp/reader", "conn", "rgrp", "reader");

    assertTrue(PubSubDiagnosticsExposure.isDirectChildOf(group, connection));
    assertTrue(PubSubDiagnosticsExposure.isDirectChildOf(readerGroup, connection));
    assertTrue(PubSubDiagnosticsExposure.isDirectChildOf(writer, group));
    assertTrue(PubSubDiagnosticsExposure.isDirectChildOf(reader, readerGroup));

    // a grandchild never sets the parent's SubError (§9.1.11.2: "the next PubSub layer")
    assertFalse(PubSubDiagnosticsExposure.isDirectChildOf(writer, connection));
    assertFalse(PubSubDiagnosticsExposure.isDirectChildOf(reader, connection));

    // sibling scopes and foreign connections never match
    assertFalse(PubSubDiagnosticsExposure.isDirectChildOf(otherGroup, connection));
    assertFalse(PubSubDiagnosticsExposure.isDirectChildOf(otherWriter, group));
    assertFalse(PubSubDiagnosticsExposure.isDirectChildOf(writer, readerGroup));
    assertFalse(PubSubDiagnosticsExposure.isDirectChildOf(connection, group));
  }

  // region fixtures

  private static ComponentDiagnostics diagnostics() {
    return diagnostics(Map.of());
  }

  /**
   * A snapshot with distinct per-counter values: traffic counters 100/200, decodeErrors 30,
   * failedTransmissions 40, encryptionErrors 50, decryptionErrors 60, failedDataSetMessages 70,
   * vendor counters 9xx (must never contribute), StateError 2, and the five Information State*
   * counters 3/5/7/11/13.
   */
  private static ComponentDiagnostics diagnostics(Map<PubSubCounter, DateTime> timeFirstChange) {
    return new ComponentDiagnostics(
        "conn/grp",
        100, // networkMessagesSent
        200, // networkMessagesReceived
        900, // dataSetMessagesSent (vendor)
        901, // dataSetMessagesReceived (vendor)
        30, // decodeErrors
        902, // sourceErrors (vendor)
        903, // staleSequenceMessages (vendor)
        904, // invalidSequenceMessages (vendor)
        50, // encryptionErrors
        60, // decryptionErrors
        905, // invalidSignatureMessages (vendor)
        906, // unknownTokenMessages (vendor)
        907, // staleKeyMessages (vendor)
        40, // failedTransmissions
        70, // failedDataSetMessages
        2, // stateError
        3, // stateOperationalByMethod
        5, // stateOperationalByParent
        7, // stateOperationalFromError
        11, // statePausedByParent
        13, // stateDisabledByMethod
        timeFirstChange,
        null);
  }

  // endregion
}

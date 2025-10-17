/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.junit.jupiter.api.Test;

public class DataChangeMonitoringFilterTest {

  // Test percent deadband filtering

  @Test
  public void testPercentDeadband_WithinDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(0.0, 100.0); // Range of 100

    var lastValue = new DataValue(new Variant(50.0));
    var currentValue = new DataValue(new Variant(59.0)); // 9% change

    // 9% change is within 10% deadband, should NOT pass filter
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertFalse(result, "Value within deadband should not pass filter");
  }

  @Test
  public void testPercentDeadband_ExceedsDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(0.0, 100.0); // Range of 100

    var lastValue = new DataValue(new Variant(50.0));
    var currentValue = new DataValue(new Variant(61.0)); // 11% change

    // 11% change exceeds 10% deadband, should pass filter
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertTrue(result, "Value exceeding deadband should pass filter");
  }

  @Test
  public void testPercentDeadband_ExactDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(0.0, 100.0); // Range of 100

    var lastValue = new DataValue(new Variant(50.0));
    var currentValue = new DataValue(new Variant(60.0)); // Exactly 10% change

    // Exact 10% change should NOT pass filter (must exceed, not equal)
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertFalse(result, "Value at exact deadband should not pass filter");
  }

  @Test
  public void testPercentDeadband_DifferentRange() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 5.0);

    var euRange = new Range(0.0, 1000.0); // Range of 1000

    var lastValue = new DataValue(new Variant(500.0));
    var currentValue = new DataValue(new Variant(549.0)); // 49 units = 4.9% of range

    // 4.9% change is within 5% deadband
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertFalse(result);
  }

  @Test
  public void testPercentDeadband_NegativeRange() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(-100.0, 100.0); // Range of 200

    var lastValue = new DataValue(new Variant(0.0));
    var currentValue = new DataValue(new Variant(19.0)); // 19 units = 9.5% of range

    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertFalse(result);

    currentValue = new DataValue(new Variant(21.0)); // 21 units = 10.5% of range
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertTrue(result);
  }

  @Test
  public void testPercentDeadband_NullEURange() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var lastValue = new DataValue(new Variant(50.0));
    var currentValue = new DataValue(new Variant(60.0));

    // Null EURange should fall back to value comparison
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, null);
    assertTrue(result, "Without EURange, should fall back to valueChanged");
  }

  @Test
  public void testPercentDeadband_ZeroRange() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(50.0, 50.0); // Zero range

    var lastValue = new DataValue(new Variant(50.0));
    var currentValue = new DataValue(new Variant(50.0));

    // Zero range should return true (cannot calculate percentage)
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertTrue(result);
  }

  // Test NaN handling

  @Test
  public void testNaN_InitialValue() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

    var lastValue = new DataValue(new Variant(Double.NaN));
    var currentValue = new DataValue(new Variant(10.0));

    // Transition from NaN to valid value should pass filter
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result, "Transition from NaN to valid value should pass");
  }

  @Test
  public void testNaN_BothNaN() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

    var lastValue = new DataValue(new Variant(Double.NaN));
    var currentValue = new DataValue(new Variant(Double.NaN));

    // Both NaN should not pass filter (no change)
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertFalse(result, "Both NaN should not pass filter");
  }

  @Test
  public void testNaN_WithAbsoluteDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Absolute.getValue()), 5.0);

    var lastValue = new DataValue(new Variant(Double.NaN));
    var currentValue = new DataValue(new Variant(10.0));

    // Transition from NaN with absolute deadband should pass
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);
  }

  @Test
  public void testNaN_WithPercentDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(0.0, 100.0);
    var lastValue = new DataValue(new Variant(Double.NaN));
    var currentValue = new DataValue(new Variant(50.0));

    // Transition from NaN with percent deadband should pass
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertTrue(result);
  }

  // Test absolute deadband

  @Test
  public void testAbsoluteDeadband_WithinDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Absolute.getValue()), 5.0);

    var lastValue = new DataValue(new Variant(100.0));
    var currentValue = new DataValue(new Variant(104.0)); // 4.0 change

    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertFalse(result, "Change within absolute deadband should not pass");
  }

  @Test
  public void testAbsoluteDeadband_ExceedsDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Absolute.getValue()), 5.0);

    var lastValue = new DataValue(new Variant(100.0));
    var currentValue = new DataValue(new Variant(106.0)); // 6.0 change

    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result, "Change exceeding absolute deadband should pass");
  }

  // Test trigger types

  @Test
  public void testTrigger_Status() {
    var filter =
        new DataChangeFilter(DataChangeTrigger.Status, uint(DeadbandType.None.getValue()), 0.0);

    var lastValue = new DataValue(new Variant(100.0), StatusCode.GOOD, null, null);
    var currentValue = new DataValue(new Variant(200.0), StatusCode.GOOD, null, null);

    // Status trigger should NOT pass on value change alone
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertFalse(result, "Status trigger should not fire on value change");

    currentValue = new DataValue(new Variant(200.0), StatusCode.BAD, null, null);
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result, "Status trigger should fire on status change");
  }

  @Test
  public void testTrigger_StatusValue() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

    var lastValue = new DataValue(new Variant(100.0), StatusCode.GOOD, null, null);

    // Value change should trigger
    var currentValue = new DataValue(new Variant(200.0), StatusCode.GOOD, null, null);
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);

    // Status change should trigger
    currentValue = new DataValue(new Variant(100.0), StatusCode.BAD, null, null);
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);
  }

  @Test
  public void testTrigger_StatusValueTimestamp() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValueTimestamp, uint(DeadbandType.None.getValue()), 0.0);

    var lastValue = new DataValue(new Variant(100.0), StatusCode.GOOD, DateTime.now(), null);

    // Value change should trigger
    var currentValue = new DataValue(new Variant(200.0), StatusCode.GOOD, DateTime.now(), null);
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);

    // Timestamp change should trigger
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      // ignore
    }
    currentValue = new DataValue(new Variant(200.0), StatusCode.GOOD, DateTime.now(), null);
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);
  }

  // Test first value (null lastValue)

  @Test
  public void testFirstValue_AlwaysPasses() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

    var currentValue = new DataValue(new Variant(100.0));

    // First value (null lastValue) should always pass
    boolean result = DataChangeMonitoringFilter.filter(null, currentValue, filter);
    assertTrue(result, "First value should always pass filter");
  }

  @Test
  public void testFirstValue_WithDeadband() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Absolute.getValue()), 100.0);

    var currentValue = new DataValue(new Variant(1.0));

    // First value should pass even with large deadband
    boolean result = DataChangeMonitoringFilter.filter(null, currentValue, filter);
    assertTrue(result);
  }

  // Test array values

  @Test
  public void testPercentDeadband_ArrayValue() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Percent.getValue()), 10.0);

    var euRange = new Range(0.0, 100.0);

    var lastValue = new DataValue(new Variant(new Double[] {10.0, 20.0, 30.0}));
    var currentValue =
        new DataValue(new Variant(new Double[] {10.0, 20.0, 40.0})); // 10 change = 10%

    // Exactly 10% change in one element should not pass
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertFalse(result);

    currentValue = new DataValue(new Variant(new Double[] {10.0, 20.0, 41.0})); // 11 change = 11%
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter, euRange);
    assertTrue(result);
  }

  @Test
  public void testAbsoluteDeadband_ArrayValue() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Absolute.getValue()), 5.0);

    var lastValue = new DataValue(new Variant(new Double[] {100.0, 200.0, 300.0}));
    var currentValue = new DataValue(new Variant(new Double[] {100.0, 200.0, 304.0})); // 4.0 change

    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertFalse(result);

    currentValue = new DataValue(new Variant(new Double[] {100.0, 200.0, 306.0})); // 6.0 change
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);
  }

  // Test edge cases

  @Test
  public void testNullValues() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.None.getValue()), 0.0);

    var lastValue = new DataValue(Variant.NULL_VALUE);
    var currentValue = new DataValue(Variant.NULL_VALUE);

    // Null to null should not trigger
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertFalse(result);

    currentValue = new DataValue(new Variant(100.0));
    // Null to value should trigger
    result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);
  }

  @Test
  public void testInfinity() {
    var filter =
        new DataChangeFilter(
            DataChangeTrigger.StatusValue, uint(DeadbandType.Absolute.getValue()), 10.0);

    var lastValue = new DataValue(new Variant(Double.POSITIVE_INFINITY));
    var currentValue = new DataValue(new Variant(1000.0));

    // Infinity changes should always pass
    boolean result = DataChangeMonitoringFilter.filter(lastValue, currentValue, filter);
    assertTrue(result);
  }
}

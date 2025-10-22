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

import java.lang.reflect.Array;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Evaluates OPC UA DataChangeFilter to determine if value changes should trigger notifications to
 * monitoring clients.
 *
 * <p>Supports all DataChangeTrigger types (Status, StatusValue, StatusValueTimestamp) and
 * DeadbandType options (None, Absolute, Percent).
 *
 * <p>Percent deadband filtering (OPC UA Part 8 - Data Access) requires the EURange parameter to
 * calculate the percentage of the engineering unit range.
 *
 * <p>The filter methods return {@code true} if a value change should trigger a notification to the
 * client, and {@code false} if the notification should be suppressed.
 *
 * @see DataChangeFilter
 * @see DataChangeTrigger
 * @see DeadbandType
 */
@NullMarked
public class DataChangeMonitoringFilter {

  /**
   * Evaluates a data change filter to determine if the value change should trigger a notification.
   *
   * <p>This overload does not support Percent Deadband filtering. For Percent Deadband support, use
   * {@link #filter(DataValue, DataValue, DataChangeFilter, Range)}.
   *
   * @param lastValue the previous value, or null for the first notification.
   * @param currentValue the new value to evaluate.
   * @param filter the data change filter configuration.
   * @return true if the value change should trigger a notification, false otherwise.
   */
  public static boolean filter(
      @Nullable DataValue lastValue, DataValue currentValue, DataChangeFilter filter) {

    return triggerFilter(lastValue, currentValue, filter, null);
  }

  /**
   * Evaluates a data change filter with EURange support for Percent Deadband.
   *
   * <p>The EURange parameter is required when the filter uses {@link DeadbandType#Percent}. If
   * euRange is null and Percent Deadband is configured, the filter falls back to value comparison.
   *
   * @param lastValue the previous value, or null for the first notification.
   * @param currentValue the new value to evaluate.
   * @param filter the data change filter configuration.
   * @param euRange the engineering unit range, required for Percent Deadband filtering.
   * @return true if the value change should trigger a notification, false otherwise.
   */
  public static boolean filter(
      @Nullable DataValue lastValue,
      DataValue currentValue,
      DataChangeFilter filter,
      @Nullable Range euRange) {

    return triggerFilter(lastValue, currentValue, filter, euRange);
  }

  private static boolean triggerFilter(
      @Nullable DataValue lastValue,
      DataValue currentValue,
      DataChangeFilter filter,
      @Nullable Range euRange) {

    if (lastValue == null) {
      return true;
    }

    DataChangeTrigger trigger = filter.getTrigger();

    return switch (trigger) {
      case Status -> filterByStatus(lastValue, currentValue);
      case StatusValue -> filterByStatusValue(lastValue, currentValue, filter, euRange);
      case StatusValueTimestamp ->
          filterByStatusValueTimestamp(lastValue, currentValue, filter, euRange);
    };
  }

  /**
   * Filters by status changes only.
   *
   * @param lastValue the previous value.
   * @param currentValue the new value to evaluate.
   * @return true if the status has changed.
   */
  private static boolean filterByStatus(DataValue lastValue, DataValue currentValue) {
    return statusChanged(lastValue, currentValue);
  }

  /**
   * Filters by value changes (with deadband if configured) or status changes.
   *
   * @param lastValue the previous value.
   * @param currentValue the new value to evaluate.
   * @param filter the data change filter configuration.
   * @param euRange the engineering unit range for Percent Deadband; may be null.
   * @return true if the value exceeds deadband or status has changed.
   */
  private static boolean filterByStatusValue(
      DataValue lastValue,
      DataValue currentValue,
      DataChangeFilter filter,
      @Nullable Range euRange) {

    return deadbandFilter(lastValue, currentValue, filter, euRange)
        || statusChanged(lastValue, currentValue);
  }

  /**
   * Filters by timestamp, value (with deadband if configured), or status changes.
   *
   * <p>When no deadband is configured, timestamp changes are checked first. When a deadband is
   * configured, deadband filtering takes precedence.
   *
   * @param lastValue the previous value.
   * @param currentValue the new value to evaluate.
   * @param filter the data change filter configuration.
   * @param euRange the engineering unit range for Percent Deadband; may be null.
   * @return true if timestamp, value, or status has changed.
   */
  private static boolean filterByStatusValueTimestamp(
      DataValue lastValue,
      DataValue currentValue,
      DataChangeFilter filter,
      @Nullable Range euRange) {

    if (DeadbandType.from(filter.getDeadbandType().intValue()) != DeadbandType.None) {
      return deadbandFilter(lastValue, currentValue, filter, euRange)
          || statusChanged(lastValue, currentValue);
    }

    // DataChangeTrigger.StatusValueTimestamp with no deadband
    return timestampChanged(lastValue, currentValue)
        || deadbandFilter(lastValue, currentValue, filter, euRange)
        || statusChanged(lastValue, currentValue);
  }

  /**
   * Applies deadband filtering to determine if the value change is significant.
   *
   * <p>Deadband types:
   *
   * <ul>
   *   <li>None: Any value change triggers notification
   *   <li>Absolute: Triggers when |current - last| &gt; deadbandValue
   *   <li>Percent: Triggers when |current - last| &gt; (deadbandPercent/100 * euRange)
   * </ul>
   *
   * @param lastValue the previous value, or null.
   * @param currentValue the new value to evaluate.
   * @param filter the data change filter configuration.
   * @param euRange the engineering unit range for Percent Deadband; may be null.
   * @return true if the value change exceeds the configured deadband.
   */
  private static boolean deadbandFilter(
      @Nullable DataValue lastValue,
      DataValue currentValue,
      DataChangeFilter filter,
      @Nullable Range euRange) {

    if (lastValue == null) {
      return true;
    }

    int index = filter.getDeadbandType().intValue();
    if (index < 0 || index >= DeadbandType.values().length) {
      return true;
    }
    DeadbandType deadbandType = DeadbandType.values()[index];

    if (deadbandType == DeadbandType.None) {
      return valueChanged(lastValue, currentValue);
    }

    Object last = lastValue.value().value();
    Object current = currentValue.value().value();

    if (last == null || current == null) {
      return valueChanged(lastValue, currentValue);
    } else if (last instanceof Matrix lastMatrix && current instanceof Matrix currentMatrix) {
      Object lastElements = lastMatrix.getElements();
      Object currentElements = currentMatrix.getElements();

      if (lastElements == null || currentElements == null) {
        return valueChanged(lastValue, currentValue);
      }

      if (deadbandType == DeadbandType.Absolute) {
        return compareArrayAbsoluteDeadband(
            lastElements, currentElements, filter.getDeadbandValue());
      } else if (deadbandType == DeadbandType.Percent && euRange != null) {
        return compareArrayPercentDeadband(
            lastElements, currentElements, filter.getDeadbandValue(), euRange);
      }
      return valueChanged(lastValue, currentValue);
    } else if (last.getClass().isArray() && current.getClass().isArray()) {
      if (deadbandType == DeadbandType.Absolute) {
        return compareArrayAbsoluteDeadband(last, current, filter.getDeadbandValue());
      } else if (deadbandType == DeadbandType.Percent && euRange != null) {
        return compareArrayPercentDeadband(last, current, filter.getDeadbandValue(), euRange);
      }
      return valueChanged(lastValue, currentValue);
    } else {
      if (deadbandType == DeadbandType.Absolute) {
        return compareScalarAbsoluteDeadband(last, current, filter.getDeadbandValue());
      } else if (deadbandType == DeadbandType.Percent && euRange != null) {
        return compareScalarPercentDeadband(last, current, filter.getDeadbandValue(), euRange);
      }
      return valueChanged(lastValue, currentValue);
    }
  }

  private static boolean compareArrayAbsoluteDeadband(
      Object last, Object current, double deadband) {

    // Use Array API to handle both primitive arrays (double[], float[], int[], etc.)
    // and boxed arrays (Double[], Float[], Integer[], etc.).
    int lastLength = Array.getLength(last);
    int currentLength = Array.getLength(current);

    if (lastLength != currentLength) {
      return true;
    } else {
      for (int i = 0; i < lastLength; i++) {
        Object lastElement = Array.get(last, i);
        Object currentElement = Array.get(current, i);
        if (exceedsAbsoluteDeadband(lastElement, currentElement, deadband)) {
          return true;
        }
      }

      return false;
    }
  }

  private static boolean compareArrayPercentDeadband(
      Object last, Object current, double deadbandPercent, Range euRange) {

    // Use Array API to handle both primitive arrays (double[], float[], int[], etc.)
    // and boxed arrays (Double[], Float[], Integer[], etc.).
    int lastLength = Array.getLength(last);
    int currentLength = Array.getLength(current);

    if (lastLength != currentLength) {
      return true;
    } else {
      for (int i = 0; i < lastLength; i++) {
        Object lastElement = Array.get(last, i);
        Object currentElement = Array.get(current, i);
        if (exceedsPercentDeadband(lastElement, currentElement, deadbandPercent, euRange)) {
          return true;
        }
      }

      return false;
    }
  }

  private static boolean compareScalarAbsoluteDeadband(
      Object last, Object current, double deadband) {

    return exceedsAbsoluteDeadband(last, current, deadband);
  }

  private static boolean compareScalarPercentDeadband(
      Object last, Object current, double deadbandPercent, Range euRange) {

    return exceedsPercentDeadband(last, current, deadbandPercent, euRange);
  }

  /**
   * Returns true if the absolute deadband is exceeded.
   *
   * <p>Returns true if type conversion fails, ensuring clients receive the notification rather than
   * silently suppressing errors.
   *
   * @param last the previous value.
   * @param current the new value.
   * @param deadband the absolute deadband value.
   * @return true if the change exceeds the deadband or conversion fails.
   */
  private static boolean exceedsAbsoluteDeadband(Object last, Object current, double deadband) {
    try {
      double lastD = ((Number) last).doubleValue();
      double currentD = ((Number) current).doubleValue();
      // NaN cannot be compared with Math.abs(). Treat transition from NaN to
      // any valid number as exceeding the deadband to ensure the valid value
      // is reported to clients.
      if (Double.isNaN(lastD) && !Double.isNaN(currentD)) {
        return true;
      }
      return Math.abs(lastD - currentD) > deadband;
    } catch (Exception e) {
      return true;
    }
  }

  /**
   * Returns true if the Percent Deadband is exceeded.
   *
   * <p>Returns true if type conversion or calculation fails, ensuring clients receive a
   * notification rather than silently suppressing errors.
   *
   * @param last the previous value.
   * @param current the new value.
   * @param deadbandPercent the deadband as a percentage (0-100).
   * @param euRange the engineering unit range for calculating absolute deadband.
   * @return true if the change exceeds the deadband or conversion fails.
   */
  private static boolean exceedsPercentDeadband(
      Object last, Object current, double deadbandPercent, Range euRange) {

    double lastD = ((Number) last).doubleValue();
    double currentD = ((Number) current).doubleValue();
    double range = euRange.getHigh() - euRange.getLow();

    // Avoid division by zero when calculating Percent Deadband.
    // Also treat negative range as invalid (misconfigured EURange property).
    // NaN transition must be reported (cannot calculate the percentage of NaN).
    if (range <= 0.0 || (Double.isNaN(lastD) && !Double.isNaN(currentD))) {
      return true;
    }
    try {
      double absoluteDeadband = (deadbandPercent / 100.0) * range;
      return Math.abs(lastD - currentD) > absoluteDeadband;
    } catch (ClassCastException | NumberFormatException | NullPointerException e) {
      return true;
    }
  }

  private static boolean statusChanged(DataValue lastValue, DataValue currentValue) {
    return !Objects.equals(lastValue.statusCode(), currentValue.statusCode());
  }

  private static boolean valueChanged(DataValue lastValue, DataValue currentValue) {
    return !Objects.equals(lastValue.value(), currentValue.value());
  }

  private static boolean timestampChanged(DataValue lastValue, DataValue currentValue) {
    return !Objects.equals(lastValue.sourceTime(), currentValue.sourceTime());
  }
}

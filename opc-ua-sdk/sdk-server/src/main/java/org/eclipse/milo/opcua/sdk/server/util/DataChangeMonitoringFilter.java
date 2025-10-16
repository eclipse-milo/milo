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

import java.util.Objects;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;

@SuppressWarnings({"checkstyle:LineLength", "checkstyle:Indentation", "checkstyle:MissingJavadocType"})
public class DataChangeMonitoringFilter {

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation", "checkstyle:MissingJavadocMethod"})
	public static boolean filter(DataValue lastValue, DataValue currentValue, DataChangeFilter filter) {
		return triggerFilter(lastValue, currentValue, filter, null);
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation", "checkstyle:MissingJavadocMethod"})
	public static boolean filter(DataValue lastValue, DataValue currentValue, DataChangeFilter filter, Range euRange) {
		return triggerFilter(lastValue, currentValue, filter, euRange);
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation", "checkstyle:LineLength"})
	private static boolean triggerFilter(DataValue lastValue, DataValue currentValue, DataChangeFilter filter, Range euRange) {
		if (lastValue == null) {
			return true;
		}

		DataChangeTrigger trigger = filter.getTrigger();

		if (trigger == DataChangeTrigger.Status) {
			return statusChanged(lastValue, currentValue);
		} else if (trigger == DataChangeTrigger.StatusValue) {
			return deadbandFilter(lastValue, currentValue, filter, euRange) || statusChanged(lastValue, currentValue);
		} else {
			if (DeadbandType.from(filter.getDeadbandType().intValue()) != DeadbandType.None) {
				return deadbandFilter(lastValue, currentValue, filter, euRange) || statusChanged(lastValue, currentValue);
			}
			// DataChangeTrigger.StatusValueTimestamp
			return timestampChanged(lastValue, currentValue) || deadbandFilter(lastValue, currentValue, filter, euRange) || statusChanged(lastValue, currentValue);
		}
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation", "checkstyle:LineLength"})
	private static boolean deadbandFilter(DataValue lastValue, DataValue currentValue, DataChangeFilter filter, Range euRange) {
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

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation", "checkstyle:LineLength"})
	private static boolean compareArrayAbsoluteDeadband(Object last, Object current, double deadband) {
		Object[] lastA = (Object[]) last;
		Object[] currentA = (Object[]) current;

		if (lastA.length != currentA.length) {
			return true;
		} else {
			boolean exceeds = false;

			for (int i = 0; i < lastA.length; i++) {
				exceeds = exceeds || exceedsAbsoluteDeadband(lastA[i], currentA[i], deadband);
			}

			return exceeds;
		}
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation", "checkstyle:LineLength"})
	private static boolean compareArrayPercentDeadband(Object last, Object current, double deadbandPercent, Range euRange) {
		Object[] lastA = (Object[]) last;
		Object[] currentA = (Object[]) current;

		if (lastA.length != currentA.length) {
			return true;
		} else {
			boolean exceeds = false;

			for (int i = 0; i < lastA.length; i++) {
				exceeds = exceeds || exceedsPercentDeadband(lastA[i], currentA[i], deadbandPercent, euRange);
			}

			return exceeds;
		}
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean compareScalarAbsoluteDeadband(Object last, Object current, double deadband) {
		return exceedsAbsoluteDeadband(last, current, deadband);
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean compareScalarPercentDeadband(Object last, Object current, double deadbandPercent, Range euRange) {
		return exceedsPercentDeadband(last, current, deadbandPercent, euRange);
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean exceedsAbsoluteDeadband(Object last, Object current, double deadband) {
		try {
			double lastD = ((Number) last).doubleValue();
			double currentD = ((Number) current).doubleValue();
			// in some cases inital Value is NaN but abs from NaN is not possible
			if (lastD == Double.NaN && currentD != Double.NaN) {
				return true;
			}
			return Math.abs(lastD - currentD) > deadband;
		} catch (Exception e) {
			return true;
		}
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean exceedsPercentDeadband(Object last, Object current, double deadbandPercent, Range euRange) {

		double lastD = ((Number) last).doubleValue();
		double currentD = ((Number) current).doubleValue();
		double range = euRange.getHigh() - euRange.getLow();

		// Avoid division by zero
		// in some cases inital Value is NaN but abs from NaN is not possible
		if (range <= 0.0 || (lastD == Double.NaN && currentD != Double.NaN)) {
			return true;
		}
		try {
			double absoluteDeadband = (deadbandPercent / 100.0) * range;
			return Math.abs(lastD - currentD) > absoluteDeadband;
		} catch (ClassCastException | NumberFormatException | NullPointerException e) {
			return true;
		}
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean statusChanged(DataValue lastValue, DataValue currentValue) {
		return !Objects.equals(lastValue.statusCode(), currentValue.statusCode());
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean valueChanged(DataValue lastValue, DataValue currentValue) {
		return !Objects.equals(lastValue.value(), currentValue.value());
	}

	@SuppressWarnings({"checkstyle:FileTabCharacter", "checkstyle:Indentation"})
	private static boolean timestampChanged(DataValue lastValue, DataValue currentValue) {
		return !Objects.equals(lastValue.sourceTime(), currentValue.sourceTime());
	}
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.eclipse.milo.opcua.sdk.server.events.conversions.AbstractConversionTest.ConversionType.EXPLICIT;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.AbstractConversionTest.ConversionType.NONE;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class DoubleConversionsTest extends AbstractConversionTest<Double> {

  @Override
  protected Class<Double> getSourceClass() {
    return Double.class;
  }

  @Override
  public Conversion[] getConversions(OpcUaDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c(0.0, false), c(0.1, true), c(1.0, true)};
        }

      case Byte:
        {
          return new Conversion[] {
            c(0.0, ubyte(0)),
            c(UByte.MIN.doubleValue(), UByte.MIN),
            c(UByte.MAX.doubleValue(), UByte.MAX),
            c(UByte.MIN.doubleValue() - 1, null, targetType),
            c(UByte.MAX.doubleValue() + 1, null, targetType)
          };
        }

      case Float:
        {
          return new Conversion[] {
            c(0.0, 0.0f),
            c((double) Float.MIN_VALUE, Float.MIN_VALUE),
            c((double) Float.MAX_VALUE, Float.MAX_VALUE),
            c((double) -Float.MIN_VALUE, -Float.MIN_VALUE),
            c((double) -Float.MAX_VALUE, -Float.MAX_VALUE),
            c(Double.MAX_VALUE, null, targetType),
            c(-Double.MAX_VALUE, null, targetType)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c(0.0, (short) 0),
            c((double) Short.MIN_VALUE, Short.MIN_VALUE),
            c((double) Short.MAX_VALUE, Short.MAX_VALUE),
            c(Short.MIN_VALUE - 1.0, null, targetType),
            c(Short.MAX_VALUE + 1.0, null, targetType)
          };
        }

      case Int32:
        {
          return new Conversion[] {
            c(0.0, 0),
            c((double) Integer.MIN_VALUE, Integer.MIN_VALUE),
            c((double) Integer.MAX_VALUE, Integer.MAX_VALUE),
            c(Integer.MIN_VALUE - 1.0, null, targetType),
            c(Integer.MAX_VALUE + 1.0, null, targetType)
          };
        }

      case Int64:
        {
          return new Conversion[] {
            c(0.0, 0L),
            c(Long.MIN_VALUE + 0.0, Long.MIN_VALUE),
            c(Long.MAX_VALUE + 0.0, Long.MAX_VALUE),
            c(-Double.MAX_VALUE, null, targetType),
            c(Double.MAX_VALUE, null, targetType)
          };
        }

      case SByte:
        {
          return new Conversion[] {
            c(0.0, (byte) 0),
            c(Byte.MIN_VALUE + 0.0, Byte.MIN_VALUE),
            c(Byte.MAX_VALUE + 0.0, Byte.MAX_VALUE),
            c(Byte.MIN_VALUE - 1.0, null, targetType),
            c(Byte.MAX_VALUE + 1.0, null, targetType)
          };
        }

      case String:
        {
          return new Conversion[] {c(0.0, "0.0"), c(1.5, "1.5")};
        }

      case UInt16:
        {
          return new Conversion[] {
            c(0.0, ushort(0)),
            c(UShort.MAX_VALUE + 0.0, UShort.MAX),
            c(UShort.MIN_VALUE - 1.0, null, targetType),
            c(UShort.MAX_VALUE + 1.0, null, targetType)
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c(0.0, uint(0)),
            c(UInteger.MAX_VALUE + 0.0, UInteger.MAX),
            c(UInteger.MIN_VALUE - 1.0, null, targetType),
            c(UInteger.MAX_VALUE + 1.0, null, targetType)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(0.0, ulong(0)),
            c(-1.0, null, targetType),
            c(Double.MAX_VALUE, ulong(Math.round(Double.MAX_VALUE)))
          };
        }

      default:
        return new Conversion[0];
    }
  }

  @Override
  public ConversionType getConversionType(OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case Boolean:
        return EXPLICIT;
      case Byte:
        return EXPLICIT;
      case Float:
        return EXPLICIT;
      case Int16:
        return EXPLICIT;
      case Int32:
        return EXPLICIT;
      case Int64:
        return EXPLICIT;
      case SByte:
        return EXPLICIT;
      case String:
        return EXPLICIT;
      case UInt16:
        return EXPLICIT;
      case UInt32:
        return EXPLICIT;
      case UInt64:
        return EXPLICIT;
      default:
        return NONE;
    }
    // @formatter:on
  }

  @Override
  protected Object convert(Object fromValue, OpcUaDataType targetType, boolean implicit) {
    return DoubleConversions.convert(fromValue, targetType, implicit);
  }
}

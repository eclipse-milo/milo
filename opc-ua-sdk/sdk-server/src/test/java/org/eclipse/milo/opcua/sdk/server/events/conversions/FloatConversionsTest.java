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
import static org.eclipse.milo.opcua.sdk.server.events.conversions.AbstractConversionTest.ConversionType.IMPLICIT;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.AbstractConversionTest.ConversionType.NONE;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class FloatConversionsTest extends AbstractConversionTest<Float> {

  @Override
  protected Class<Float> getSourceClass() {
    return Float.class;
  }

  @Override
  public Conversion[] getConversions(OpcUaDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c(0.0f, Boolean.FALSE), c(1.0f, Boolean.TRUE)};
        }

      case Byte:
        {
          return new Conversion[] {
            c(0.0f, ubyte(0)),
            c(UByte.MIN.floatValue(), UByte.MIN),
            c(UByte.MAX.floatValue(), UByte.MAX),
            c(UByte.MIN.floatValue() - 1, null, targetType),
            c(UByte.MAX.floatValue() + 1, null, targetType)
          };
        }

      case Double:
        {
          return new Conversion[] {
            c(0.0f, 0.0d),
            c(Float.MIN_VALUE, (double) Float.MIN_VALUE),
            c(Float.MAX_VALUE, (double) Float.MAX_VALUE)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c(0.0f, (short) 0),
            c(0.4f, (short) 0),
            c(0.5f, (short) 1),
            c((float) Short.MIN_VALUE, Short.MIN_VALUE),
            c((float) Short.MAX_VALUE, Short.MAX_VALUE),
            c((float) (Short.MIN_VALUE - 1), null, targetType),
            c((float) (Short.MAX_VALUE + 1), null, targetType)
          };
        }

      case Int32:
        {
          // 3e9f, 1e15f and 1e30f are outside the Int32 range and must convert to null rather
          // than saturate at Integer.MAX_VALUE. Note that (float) Integer.MAX_VALUE itself rounds
          // up to 2^31 and is therefore out of range too; 2147483520f is the largest float that
          // still fits.
          return new Conversion[] {
            c(0.0f, 0),
            c(0.4f, 0),
            c(0.5f, 1),
            c((float) Integer.MIN_VALUE, Integer.MIN_VALUE),
            c(2147483520f, 2147483520),
            c((float) Integer.MAX_VALUE, null, targetType),
            c(3e9f, null, targetType),
            c(1e15f, null, targetType),
            c(1e30f, null, targetType)
          };
        }

      case Int64:
        {
          // Int64 holds every finite float below 2^63, so 1e15f must keep its exact value
          // (999999986991104) instead of saturating at Integer.MAX_VALUE. (float) Long.MAX_VALUE is
          // 2^63, one above the largest long, so it must fail like (float) Integer.MAX_VALUE does
          // for Int32; 9223371487098961920f (2^63 - 2^39) is the largest float that still fits.
          return new Conversion[] {
            c(0.0f, 0L),
            c(0.4f, 0L),
            c(0.5f, 1L),
            c(3e9f, 3_000_000_000L),
            c(1e15f, 999_999_986_991_104L),
            c((float) Long.MIN_VALUE, Long.MIN_VALUE),
            c(9223371487098961920f, 9223371487098961920L),
            c((float) Long.MAX_VALUE, null, targetType),
            c(1e30f, null, targetType),
            c(Float.NaN, null, targetType),
            c(Float.POSITIVE_INFINITY, null, targetType),
            c(Float.NEGATIVE_INFINITY, null, targetType)
          };
        }

      case SByte:
        {
          return new Conversion[] {
            c(0.0f, (byte) 0),
            c(0.4f, (byte) 0),
            c(0.5f, (byte) 1),
            c((float) Byte.MIN_VALUE, Byte.MIN_VALUE),
            c((float) Byte.MAX_VALUE, Byte.MAX_VALUE),
            c((float) (Byte.MIN_VALUE - 1), null, targetType),
            c((float) (Byte.MAX_VALUE + 1), null, targetType)
          };
        }

      case String:
        {
          return new Conversion[] {c(0.0f, "0.0"), c(0.4f, "0.4"), c(0.5f, "0.5"), c(1.0f, "1.0")};
        }

      case UInt16:
        {
          return new Conversion[] {
            c(0.0f, ushort(0)),
            c(0.4f, ushort(0)),
            c(0.5f, ushort(1)),
            c(UShort.MIN.floatValue(), UShort.MIN),
            c(UShort.MAX.floatValue(), UShort.MAX),
            c(UShort.MIN.floatValue() - 1, null, targetType),
            c(UShort.MAX.floatValue() + 1, null, targetType)
          };
        }

      case UInt32:
        {
          // UInt32 reaches 4294967295, so 3e9f is in range; 1e15f is not.
          return new Conversion[] {
            c(0.0f, uint(0)),
            c(0.4f, uint(0)),
            c(0.5f, uint(1)),
            c(3e9f, uint(3_000_000_000L)),
            c(-1.0f, null, targetType),
            c(1e15f, null, targetType)
          };
        }

      case UInt64:
        {
          // UInt64 reaches 2^64 - 1, so the floats at and above 2^63 must convert instead of
          // saturating at Long.MAX_VALUE. ulong(long) masks, so 2^63 is the bit pattern
          // Long.MIN_VALUE and 2^64 - 2^40 (the largest float below 2^64) is -2^40. At the bottom,
          // -0.5f rounds to 0 and fits, as it does for the other unsigned types, but anything below
          // it rounds to -1.
          return new Conversion[] {
            c(0.0f, ulong(0)),
            c(0.4f, ulong(0)),
            c(0.5f, ulong(1)),
            c(-0.4f, ulong(0)),
            c(-0.5f, ulong(0)),
            c(Math.nextDown(-0.5f), null, targetType),
            c(3e9f, ulong(3_000_000_000L)),
            c(1e15f, ulong(999_999_986_991_104L)),
            c((float) Long.MAX_VALUE, ulong(Long.MIN_VALUE)),
            c(18446742974197923840f, ulong(-1_099_511_627_776L)),
            c(18446744073709551616f, null, targetType),
            c(Float.MAX_VALUE, null, targetType),
            c(-1.0f, null, targetType),
            c(Float.NaN, null, targetType),
            c(Float.POSITIVE_INFINITY, null, targetType),
            c(Float.NEGATIVE_INFINITY, null, targetType)
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
      case Double:
        return IMPLICIT;
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
    return FloatConversions.convert(fromValue, targetType, implicit);
  }
}

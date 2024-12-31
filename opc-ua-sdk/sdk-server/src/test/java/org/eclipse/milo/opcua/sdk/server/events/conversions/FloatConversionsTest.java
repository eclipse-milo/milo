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

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class FloatConversionsTest extends AbstractConversionTest<Float> {

  @Override
  protected Class<Float> getSourceClass() {
    return Float.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
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
          return new Conversion[] {
            c(0.0f, 0),
            c(0.4f, 0),
            c(0.5f, 1),
            c((float) Integer.MIN_VALUE, Integer.MIN_VALUE),
            c((float) Integer.MAX_VALUE, Integer.MAX_VALUE),
          };
        }

      case Int64:
        {
          return new Conversion[] {c(0.0f, 0L), c(0.4f, 0L), c(0.5f, 1L)};
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
          return new Conversion[] {
            c(0.0f, uint(0)), c(0.4f, uint(0)), c(0.5f, uint(1)), c(-1.0f, null, targetType)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(0.0f, ulong(0)), c(0.4f, ulong(0)), c(0.5f, ulong(1)), c(-1.0f, null, targetType)
          };
        }

      default:
        return new Conversion[0];
    }
  }

  @Override
  public ConversionType getConversionType(BuiltinDataType targetType) {
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
  protected Object convert(Object fromValue, BuiltinDataType targetType, boolean implicit) {
    return FloatConversions.convert(fromValue, targetType, implicit);
  }
}

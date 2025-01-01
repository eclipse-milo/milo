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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class Int64ConversionsTest extends AbstractConversionTest<Long> {

  @Override
  protected Class<Long> getSourceClass() {
    return Long.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c(0L, Boolean.FALSE), c(1L, Boolean.TRUE)};
        }

      case Byte:
        {
          return new Conversion[] {
            c(UByte.MIN.longValue(), UByte.MIN),
            c(UByte.MAX.longValue(), UByte.MAX),
            c(UByte.MIN.longValue() - 1, null, targetType),
            c(UByte.MAX.longValue() + 1, null, targetType)
          };
        }

      case Double:
        {
          return new Conversion[] {
            c(0L, 0.0d),
            c(Long.MIN_VALUE, (double) Long.MIN_VALUE),
            c(Long.MAX_VALUE, (double) Long.MAX_VALUE)
          };
        }

      case Float:
        {
          return new Conversion[] {
            c(0L, 0.0f),
            c(Long.MIN_VALUE, (float) Long.MIN_VALUE),
            c(Long.MAX_VALUE, (float) Long.MAX_VALUE)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c(0L, (short) 0),
            c((long) Short.MIN_VALUE, Short.MIN_VALUE),
            c((long) Short.MAX_VALUE, Short.MAX_VALUE),
            c((long) (Short.MIN_VALUE - 1), null, targetType),
            c((long) (Short.MAX_VALUE + 1), null, targetType)
          };
        }

      case Int32:
        {
          return new Conversion[] {
            c(0L, 0),
            c((long) Integer.MIN_VALUE, Integer.MIN_VALUE),
            c((long) Integer.MAX_VALUE, Integer.MAX_VALUE),
            c(Integer.MIN_VALUE - 1L, null, targetType),
            c(Integer.MAX_VALUE + 1L, null, targetType)
          };
        }

      case SByte:
        {
          return new Conversion[] {
            c(0L, (byte) 0),
            c((long) Byte.MIN_VALUE, Byte.MIN_VALUE),
            c((long) Byte.MAX_VALUE, Byte.MAX_VALUE),
            c((long) (Byte.MIN_VALUE - 1), null, targetType),
            c((long) (Byte.MAX_VALUE + 1), null, targetType)
          };
        }

      case StatusCode:
        {
          return new Conversion[] {
            c(0L, new StatusCode(0)),
            c(Long.MIN_VALUE, new StatusCode(Long.MIN_VALUE)),
            c(Long.MAX_VALUE, new StatusCode(Long.MAX_VALUE))
          };
        }

      case String:
        {
          return new Conversion[] {c(0L, "0"), c(1L, "1")};
        }

      case UInt16:
        {
          return new Conversion[] {
            c(0L, ushort(0)),
            c(UShort.MAX.longValue(), UShort.MAX),
            c(UShort.MIN.longValue() - 1, null, targetType),
            c(UShort.MAX.longValue() + 1, null, targetType)
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c(0L, uint(0)),
            c(UInteger.MAX.longValue(), UInteger.MAX),
            c(-1L, null, targetType),
            c(UInteger.MAX.longValue() + 1L, null, targetType)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(0L, ulong(0)), c(Long.MAX_VALUE, ulong(Long.MAX_VALUE)), c(-1L, null, targetType)
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
      case Float:
        return IMPLICIT;
      case Int16:
        return EXPLICIT;
      case Int32:
        return EXPLICIT;
      case SByte:
        return EXPLICIT;
      case StatusCode:
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
    return Int64Conversions.convert(fromValue, targetType, implicit);
  }
}

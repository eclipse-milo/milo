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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;

public class Int16ConversionsTest extends AbstractConversionTest<Short> {

  @Override
  protected Class<Short> getSourceClass() {
    return Short.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c((short) 0, Boolean.FALSE), c((short) 1, Boolean.TRUE)};
        }

      case Byte:
        {
          return new Conversion[] {
            c(UByte.MIN_VALUE, UByte.MIN, BuiltinDataType.Byte),
            c(UByte.MAX_VALUE, UByte.MAX, BuiltinDataType.Byte),
            c((short) (UByte.MIN_VALUE - 1), null, BuiltinDataType.Byte),
            c((short) (UByte.MAX_VALUE + 1), null, BuiltinDataType.Byte)
          };
        }

      case Double:
        {
          return new Conversion[] {
            c((short) 0, 0.0d),
            c(Short.MIN_VALUE, (double) Short.MIN_VALUE),
            c(Short.MAX_VALUE, (double) Short.MAX_VALUE),
          };
        }

      case Float:
        {
          return new Conversion[] {
            c((short) 0, 0.0f),
            c(Short.MIN_VALUE, (float) Short.MIN_VALUE),
            c(Short.MAX_VALUE, (float) Short.MAX_VALUE),
          };
        }

      case Int32:
        {
          return new Conversion[] {
            c((short) 0, 0),
            c(Short.MIN_VALUE, (int) Short.MIN_VALUE),
            c(Short.MAX_VALUE, (int) Short.MAX_VALUE),
          };
        }

      case Int64:
        {
          return new Conversion[] {
            c((short) 0, 0L),
            c(Short.MIN_VALUE, (long) Short.MIN_VALUE),
            c(Short.MAX_VALUE, (long) Short.MAX_VALUE),
          };
        }

      case SByte:
        {
          return new Conversion[] {
            c((short) 0, (byte) 0),
            c((short) Byte.MIN_VALUE, Byte.MIN_VALUE),
            c((short) Byte.MAX_VALUE, Byte.MAX_VALUE),
            c((short) (Byte.MIN_VALUE - 1), null, BuiltinDataType.SByte),
            c((short) (Byte.MAX_VALUE + 1), null, BuiltinDataType.SByte)
          };
        }

      case String:
        {
          return new Conversion[] {c((short) 0, "0")};
        }

      case UInt16:
        {
          return new Conversion[] {
            c((short) 0, ushort(0)),
            c(Short.MAX_VALUE, ushort(Short.MAX_VALUE)),
            c((short) -1, null, BuiltinDataType.UInt16)
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c((short) 0, uint(0)),
            c(Short.MAX_VALUE, uint(Short.MAX_VALUE)),
            c((short) -1, null, BuiltinDataType.UInt32)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c((short) 0, ulong(0)),
            c(Short.MAX_VALUE, ulong(Short.MAX_VALUE)),
            c((short) -1, null, BuiltinDataType.UInt64)
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
        return ConversionType.EXPLICIT;
      case Byte:
        return ConversionType.EXPLICIT;
      case Double:
        return ConversionType.IMPLICIT;
      case Float:
        return ConversionType.IMPLICIT;
      case Int32:
        return ConversionType.IMPLICIT;
      case Int64:
        return ConversionType.IMPLICIT;
      case SByte:
        return ConversionType.EXPLICIT;
      case String:
        return ConversionType.EXPLICIT;
      case UInt16:
        return ConversionType.EXPLICIT;
      case UInt32:
        return ConversionType.IMPLICIT;
      case UInt64:
        return ConversionType.IMPLICIT;
      default:
        return ConversionType.NONE;
    }
    // @formatter:on
  }

  @Override
  protected Object convert(Object fromValue, BuiltinDataType targetType, boolean implicit) {
    return Int16Conversions.convert(fromValue, targetType, implicit);
  }
}

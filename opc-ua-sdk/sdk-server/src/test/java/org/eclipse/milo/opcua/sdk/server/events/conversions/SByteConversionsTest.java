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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import org.eclipse.milo.opcua.stack.core.BuiltinDataType;

public class SByteConversionsTest extends AbstractConversionTest<Byte> {

  @Override
  protected Class<Byte> getSourceClass() {
    return Byte.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
    switch (targetType) {
      case Boolean:
        return new Conversion[] {c((byte) 0, Boolean.FALSE), c((byte) 1, Boolean.TRUE)};

      case Byte:
        return new Conversion[] {
          c((byte) 0, ubyte(0)),
          c(Byte.MAX_VALUE, ubyte(Byte.MAX_VALUE)),
          c((byte) -1, null, BuiltinDataType.Byte)
        };

      case Double:
        {
          return new Conversion[] {
            c((byte) 0, 0.0),
            c(Byte.MIN_VALUE, (double) Byte.MIN_VALUE),
            c(Byte.MAX_VALUE, (double) Byte.MAX_VALUE)
          };
        }

      case Float:
        {
          return new Conversion[] {
            c((byte) 0, 0f),
            c(Byte.MIN_VALUE, (float) Byte.MIN_VALUE),
            c(Byte.MAX_VALUE, (float) Byte.MAX_VALUE)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c((byte) 0, (short) 0),
            c(Byte.MIN_VALUE, (short) Byte.MIN_VALUE),
            c(Byte.MAX_VALUE, (short) Byte.MAX_VALUE)
          };
        }

      case Int32:
        {
          return new Conversion[] {
            c((byte) 0, 0),
            c(Byte.MIN_VALUE, (int) Byte.MIN_VALUE),
            c(Byte.MAX_VALUE, (int) Byte.MAX_VALUE)
          };
        }

      case Int64:
        {
          return new Conversion[] {
            c((byte) 0, 0L),
            c(Byte.MIN_VALUE, (long) Byte.MIN_VALUE),
            c(Byte.MAX_VALUE, (long) Byte.MAX_VALUE)
          };
        }

      case String:
        {
          return new Conversion[] {
            c((byte) 0, "0"),
            c(Byte.MIN_VALUE, Byte.toString(Byte.MIN_VALUE)),
            c(Byte.MAX_VALUE, Byte.toString(Byte.MAX_VALUE))
          };
        }

      case UInt16:
        {
          return new Conversion[] {
            c((byte) 0, ushort(0)),
            c(Byte.MAX_VALUE, ushort(Byte.MAX_VALUE)),
            c((byte) -1, null, targetType)
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c((byte) 0, uint(0)),
            c(Byte.MAX_VALUE, uint(Byte.MAX_VALUE)),
            c((byte) -1, null, targetType)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c((byte) 0, ulong(0)),
            c(Byte.MAX_VALUE, ulong(Byte.MAX_VALUE)),
            c((byte) -1, null, targetType)
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
      case Int16:
        return ConversionType.IMPLICIT;
      case Int32:
        return ConversionType.IMPLICIT;
      case Int64:
        return ConversionType.IMPLICIT;
      case String:
        return ConversionType.EXPLICIT;
      case UInt16:
        return ConversionType.IMPLICIT;
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
    return SByteConversions.convert(fromValue, targetType, implicit);
  }
}

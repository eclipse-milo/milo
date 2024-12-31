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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class UInt32ConversionsTest extends AbstractConversionTest<UInteger> {

  @Override
  protected Class<UInteger> getSourceClass() {
    return UInteger.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c(uint(0), Boolean.FALSE), c(uint(1), Boolean.TRUE)};
        }

      case Byte:
        {
          return new Conversion[] {
            c(uint(0), ubyte(0)),
            c(uint(UByte.MAX_VALUE), UByte.MAX),
            c(uint(UByte.MAX_VALUE + 1), null, targetType)
          };
        }

      case Double:
        {
          return new Conversion[] {
            c(UInteger.MIN, (double) UInteger.MIN_VALUE),
            c(UInteger.MAX, (double) UInteger.MAX_VALUE)
          };
        }

      case Float:
        {
          return new Conversion[] {
            c(UInteger.MIN, (float) UInteger.MIN_VALUE), c(UInteger.MAX, (float) UInteger.MAX_VALUE)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c(uint(0), (short) 0),
            c(uint(Short.MAX_VALUE), Short.MAX_VALUE),
            c(uint(Short.MAX_VALUE + 1), null, targetType)
          };
        }

      case Int32:
        {
          return new Conversion[] {
            c(uint(0), 0),
            c(uint(Integer.MAX_VALUE), Integer.MAX_VALUE),
            c(uint(Integer.MAX_VALUE + 1L), null, targetType)
          };
        }

      case Int64:
        {
          return new Conversion[] {c(uint(0), 0L), c(UInteger.MAX, UInteger.MAX_VALUE)};
        }

      case SByte:
        {
          return new Conversion[] {
            c(uint(0), (byte) 0),
            c(uint(Byte.MAX_VALUE), Byte.MAX_VALUE),
            c(uint(Byte.MAX_VALUE + 1), null, targetType)
          };
        }

      case StatusCode:
        {
          return new Conversion[] {
            c(uint(0), new StatusCode(0x00000000)),
            c(uint(0xABCD), new StatusCode(0x0000ABCD)),
            c(uint(0xBADCABCD), new StatusCode(0xBADCABCD)),
            c(UInteger.MAX, new StatusCode(0xFFFFFFFF))
          };
        }

      case String:
        {
          return new Conversion[] {
            c(UInteger.MIN, UInteger.MIN.toString()), c(UInteger.MAX, UInteger.MAX.toString())
          };
        }

      case UInt16:
        {
          return new Conversion[] {
            c(uint(0), ushort(0)),
            c(uint(UShort.MAX_VALUE), UShort.MAX),
            c(uint(UShort.MAX_VALUE + 1), null, targetType)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(uint(0), ulong(0)), c(UInteger.MAX, ulong(UInteger.MAX_VALUE))
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
        return ConversionType.EXPLICIT;
      case Int32:
        return ConversionType.IMPLICIT;
      case Int64:
        return ConversionType.IMPLICIT;
      case SByte:
        return ConversionType.EXPLICIT;
      case StatusCode:
        return ConversionType.EXPLICIT;
      case String:
        return ConversionType.EXPLICIT;
      case UInt16:
        return ConversionType.EXPLICIT;
      case UInt64:
        return ConversionType.IMPLICIT;
      default:
        return ConversionType.NONE;
    }
    // @formatter:on
  }

  @Override
  protected Object convert(Object fromValue, BuiltinDataType targetType, boolean implicit) {
    return UInt32Conversions.convert(fromValue, targetType, implicit);
  }
}

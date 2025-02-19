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

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class UInt16ConversionsTest extends AbstractConversionTest<UShort> {

  @Override
  protected Class<UShort> getSourceClass() {
    return UShort.class;
  }

  @Override
  public Conversion[] getConversions(OpcUaDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c(ushort(0), Boolean.FALSE), c(ushort(1), Boolean.TRUE)};
        }

      case Byte:
        {
          return new Conversion[] {
            c(ushort(0), ubyte(0)),
            c(ushort(UByte.MAX_VALUE), UByte.MAX),
            c(ushort(UByte.MAX_VALUE + 1), null, targetType)
          };
        }

      case Double:
        {
          return new Conversion[] {
            c(UShort.MIN, (double) UShort.MIN_VALUE), c(UShort.MAX, (double) UShort.MAX_VALUE)
          };
        }

      case Float:
        {
          return new Conversion[] {
            c(UShort.MIN, (float) UShort.MIN_VALUE), c(UShort.MAX, (float) UShort.MAX_VALUE)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c(ushort(0), (short) 0),
            c(ushort(Short.MAX_VALUE), Short.MAX_VALUE),
            c(ushort(Short.MAX_VALUE + 1), null, targetType)
          };
        }

      case Int32:
        {
          return new Conversion[] {c(ushort(0), 0), c(UShort.MAX, UShort.MAX_VALUE)};
        }

      case Int64:
        {
          return new Conversion[] {c(ushort(0), 0L), c(UShort.MAX, (long) UShort.MAX_VALUE)};
        }

      case SByte:
        {
          return new Conversion[] {
            c(ushort(0), (byte) 0),
            c(ushort(Byte.MAX_VALUE), Byte.MAX_VALUE),
            c(ushort(Byte.MAX_VALUE + 1), null, targetType)
          };
        }

      case StatusCode:
        {
          return new Conversion[] {
            c(ushort(0), new StatusCode(0x00000000)),
            c(ushort(0xABCD), new StatusCode(0xABCD0000)),
            c(UShort.MAX, new StatusCode(0xFFFF0000))
          };
        }

      case String:
        {
          return new Conversion[] {
            c(UShort.MIN, UShort.MIN.toString()), c(UShort.MAX, UShort.MAX.toString())
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c(UShort.MIN, uint(UShort.MIN_VALUE)), c(UShort.MAX, uint(UShort.MAX_VALUE))
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(UShort.MIN, ulong(UShort.MIN_VALUE)), c(UShort.MAX, ulong(UShort.MAX_VALUE))
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
      case SByte:
        return ConversionType.EXPLICIT;
      case StatusCode:
        return ConversionType.IMPLICIT;
      case String:
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
  protected Object convert(Object fromValue, OpcUaDataType targetType, boolean implicit) {
    return UInt16Conversions.convert(fromValue, targetType, implicit);
  }
}

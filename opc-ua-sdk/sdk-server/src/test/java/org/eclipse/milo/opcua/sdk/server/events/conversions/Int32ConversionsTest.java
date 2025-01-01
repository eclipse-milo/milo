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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class Int32ConversionsTest extends AbstractConversionTest<Integer> {

  @Override
  protected Class<Integer> getSourceClass() {
    return Integer.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {c(0, Boolean.FALSE), c(1, Boolean.TRUE)};
        }

      case Byte:
        {
          return new Conversion[] {
            c((int) UByte.MIN_VALUE, UByte.MIN),
            c((int) UByte.MAX_VALUE, UByte.MAX),
            c(UByte.MIN_VALUE - 1, null, targetType),
            c(UByte.MAX_VALUE + 1, null, targetType)
          };
        }

      case Double:
        {
          return new Conversion[] {
            c(0, 0d),
            c(Integer.MIN_VALUE, (double) Integer.MIN_VALUE),
            c(Integer.MAX_VALUE, (double) Integer.MAX_VALUE)
          };
        }

      case Float:
        {
          return new Conversion[] {
            c(0, 0f),
            c(Integer.MIN_VALUE, (float) Integer.MIN_VALUE),
            c(Integer.MAX_VALUE, (float) Integer.MAX_VALUE)
          };
        }

      case Int16:
        {
          return new Conversion[] {
            c(0, (short) 0),
            c((int) Short.MIN_VALUE, Short.MIN_VALUE),
            c((int) Short.MAX_VALUE, Short.MAX_VALUE),
            c(Short.MIN_VALUE - 1, null, targetType),
            c(Short.MAX_VALUE + 1, null, targetType)
          };
        }

      case Int64:
        {
          return new Conversion[] {
            c(0, 0L),
            c(Integer.MIN_VALUE, (long) Integer.MIN_VALUE),
            c(Integer.MAX_VALUE, (long) Integer.MAX_VALUE)
          };
        }

      case SByte:
        {
          return new Conversion[] {
            c(0, (byte) 0),
            c((int) Byte.MIN_VALUE, Byte.MIN_VALUE),
            c((int) Byte.MAX_VALUE, Byte.MAX_VALUE),
            c(Byte.MIN_VALUE - 1, null, targetType),
            c(Byte.MAX_VALUE + 1, null, targetType)
          };
        }

      case StatusCode:
        {
          return new Conversion[] {
            c(0, new StatusCode(0)),
            c(Integer.MIN_VALUE, new StatusCode(Integer.MIN_VALUE)),
            c(Integer.MAX_VALUE, new StatusCode(Integer.MAX_VALUE))
          };
        }

      case String:
        {
          return new Conversion[] {c(0, "0"), c(1, "1")};
        }

      case UInt16:
        {
          return new Conversion[] {
            c(0, ushort(0)),
            c(UShort.MAX_VALUE, UShort.MAX),
            c(UShort.MIN_VALUE - 1, null, targetType),
            c(UShort.MAX_VALUE + 1, null, targetType)
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c(0, uint(0)), c(Integer.MAX_VALUE, uint(Integer.MAX_VALUE)), c(-1, null, targetType)
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(0, ulong(0)), c(Integer.MAX_VALUE, ulong(Integer.MAX_VALUE)), c(-1, null, targetType)
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
      case Int64:
        return IMPLICIT;
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
        return IMPLICIT;
      default:
        return NONE;
    }
    // @formatter:on
  }

  @Override
  protected Object convert(Object fromValue, BuiltinDataType targetType, boolean implicit) {
    return Int32Conversions.convert(fromValue, targetType, implicit);
  }
}

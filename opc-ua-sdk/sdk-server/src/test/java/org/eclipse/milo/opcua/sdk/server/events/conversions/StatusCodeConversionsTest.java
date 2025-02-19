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

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

public class StatusCodeConversionsTest extends AbstractConversionTest<StatusCode> {

  @Override
  protected Class<StatusCode> getSourceClass() {
    return StatusCode.class;
  }

  @Override
  public Conversion[] getConversions(OpcUaDataType targetType) {
    switch (targetType) {
      case Int16:
        {
          return new Conversion[] {
            c(new StatusCode(0), (short) 0), c(new StatusCode(0x7FFF0000), Short.MAX_VALUE)
          };
        }

      case Int32:
        {
          return new Conversion[] {
            c(new StatusCode(0), 0), c(new StatusCode(Integer.MAX_VALUE), Integer.MAX_VALUE)
          };
        }

      case Int64:
        {
          return new Conversion[] {
            c(new StatusCode(0), 0L), c(new StatusCode(0xFFFFFFFF), 0xFFFFFFFFL)
          };
        }

      case UInt16:
        {
          return new Conversion[] {
            c(new StatusCode(0), ushort(0)),
            c(new StatusCode(0x7FFF0000), ushort(Short.MAX_VALUE)),
            c(new StatusCode(0xFFFF0000), UShort.MAX)
          };
        }

      case UInt32:
        {
          return new Conversion[] {
            c(new StatusCode(0), uint(0)),
            c(new StatusCode(0x7FFF0000), uint(0x7FFF0000)),
            c(new StatusCode(0xFFFF0000), uint(0xFFFF0000)),
            c(new StatusCode(0xFFFFFFFF), uint(0xFFFFFFFF))
          };
        }

      case UInt64:
        {
          return new Conversion[] {
            c(new StatusCode(0), ulong(0)), c(new StatusCode(0xFFFFFFFF), ulong(0xFFFFFFFFL))
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
      case Int16:
        return ConversionType.EXPLICIT;
      case Int32:
        return ConversionType.IMPLICIT;
      case Int64:
        return ConversionType.IMPLICIT;
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
  protected Object convert(Object fromValue, OpcUaDataType targetType, boolean implicit) {
    return StatusCodeConversions.convert(fromValue, targetType, implicit);
  }
}

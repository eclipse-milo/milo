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

import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

public class StringConversionsTest extends AbstractConversionTest<String> {

  @Override
  protected Class<String> getSourceClass() {
    return String.class;
  }

  @Override
  public Conversion[] getConversions(BuiltinDataType targetType) {
    switch (targetType) {
      case Boolean:
        {
          return new Conversion[] {
            c("true", true),
            c("1", true),
            c("false", false),
            c("0", false),
            c("foo", null, targetType)
          };
        }

      case Byte:
        {
          return new Conversion[] {c("0", ubyte(0)), c("255", ubyte(255))};
        }

      case DateTime:
        {
          // Arbitrary DateTime without any nano precision because
          // they are lost going to ISO 8601 format.
          DateTime dt = new DateTime(131801928020000000L);
          String dts = (String) DateTimeConversions.convert(dt, BuiltinDataType.String, false);
          return new Conversion[] {c(dts, dt)};
        }

      case Double:
        {
          return new Conversion[] {c("0.0", 0.0d)};
        }

      case ExpandedNodeId:
        {
          ExpandedNodeId eni = new ExpandedNodeId(ushort(0), Namespaces.OPC_UA, "foo", uint(0));

          String s = eni.toParseableString();

          return new Conversion[] {c(s, eni)};
        }

      case Float:
        {
          return new Conversion[] {c("0.0", 0.0f), c("0f", 0f), c("0e0", 0f)};
        }

      case Guid:
        {
          UUID uuid = UUID.randomUUID();
          String s = uuid.toString();
          return new Conversion[] {c(s, uuid)};
        }

      case Int16:
        {
          return new Conversion[] {c("0", (short) 0)};
        }

      case Int32:
        {
          return new Conversion[] {c("0", 0)};
        }

      case Int64:
        {
          return new Conversion[] {c("0", 0L)};
        }

      case NodeId:
        {
          NodeId nodeId = new NodeId(0, "foo");
          return new Conversion[] {c(nodeId.toParseableString(), nodeId)};
        }

      case SByte:
        {
          return new Conversion[] {c("0", (byte) 0)};
        }

      case LocalizedText:
        {
          return new Conversion[] {c("foo", new LocalizedText("", "foo"))};
        }

      case QualifiedName:
        {
          return new Conversion[] {c("foo", new QualifiedName(0, "foo"))};
        }

      case UInt16:
        {
          return new Conversion[] {c("0", ushort(0))};
        }

      case UInt32:
        {
          return new Conversion[] {c("0", uint(0))};
        }

      case UInt64:
        {
          return new Conversion[] {c("0", ulong(0))};
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
        return ConversionType.IMPLICIT;
      case Byte:
        return ConversionType.IMPLICIT;
      case DateTime:
        return ConversionType.EXPLICIT;
      case Double:
        return ConversionType.IMPLICIT;
      case ExpandedNodeId:
        return ConversionType.EXPLICIT;
      case Float:
        return ConversionType.IMPLICIT;
      case Guid:
        return ConversionType.IMPLICIT;
      case Int16:
        return ConversionType.IMPLICIT;
      case Int32:
        return ConversionType.IMPLICIT;
      case Int64:
        return ConversionType.IMPLICIT;
      case NodeId:
        return ConversionType.EXPLICIT;
      case SByte:
        return ConversionType.IMPLICIT;
      case LocalizedText:
        return ConversionType.EXPLICIT;
      case QualifiedName:
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
    return StringConversions.convert(fromValue, targetType, implicit);
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server.types;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.examples.server.ExampleNamespace;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Union;

public class CustomUnionType extends Union implements UaStructuredType {

  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse(
          String.format("nsu=%s;s=%s", ExampleNamespace.NAMESPACE_URI, "DataType.CustomUnionType"));

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse(
          String.format(
              "nsu=%s;s=%s",
              ExampleNamespace.NAMESPACE_URI, "DataType.CustomUnionType.BinaryEncoding"));

  private final Type type;
  private final Object value;

  private CustomUnionType(Type type, Object value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    // XML encoding not supported
    return ExpandedNodeId.NULL_VALUE;
  }

  public UInteger asFoo() {
    return (UInteger) value;
  }

  public String asBar() {
    return (String) value;
  }

  public boolean isNull() {
    return type == Type.Null;
  }

  public boolean isFoo() {
    return type == Type.Foo;
  }

  public boolean isBar() {
    return type == Type.Bar;
  }

  public static CustomUnionType ofNull() {
    return new CustomUnionType(Type.Null, null);
  }

  public static CustomUnionType ofFoo(UInteger value) {
    return new CustomUnionType(Type.Foo, value);
  }

  public static CustomUnionType ofBar(String value) {
    return new CustomUnionType(Type.Bar, value);
  }

  enum Type {
    Null,
    Foo,
    Bar
  }

  public static class Codec extends GenericDataTypeCodec<CustomUnionType> {
    @Override
    public Class<CustomUnionType> getType() {
      return CustomUnionType.class;
    }

    @Override
    public CustomUnionType decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger switchValue = decoder.decodeUInt32("SwitchValue");
      switch (switchValue.intValue()) {
        case 0:
          return CustomUnionType.ofNull();
        case 1:
          {
            UInteger foo = decoder.decodeUInt32("foo");
            return CustomUnionType.ofFoo(foo);
          }
        case 2:
          {
            String bar = decoder.decodeString("bar");
            return CustomUnionType.ofBar(bar);
          }
        default:
          throw new UaSerializationException(
              StatusCodes.Bad_DecodingError,
              "unknown field in Union CustomUnionType: " + switchValue);
      }
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, CustomUnionType value) {
      encoder.encodeUInt32("SwitchValue", uint(value.type.ordinal()));
      switch (value.type) {
        case Null:
          break;
        case Foo:
          {
            encoder.encodeUInt32("foo", value.asFoo());
            break;
          }
        case Bar:
          {
            encoder.encodeString("bar", value.asBar());
            break;
          }
        default:
          throw new IllegalArgumentException("unhandled type: " + value.type);
      }
    }
  }
}

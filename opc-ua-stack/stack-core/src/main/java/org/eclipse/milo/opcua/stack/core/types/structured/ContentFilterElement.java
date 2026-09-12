/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.1</a>
 */
public class ContentFilterElement extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=583");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=585");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=584");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15204");

  private final FilterOperator filterOperator;

  private final ExtensionObject @Nullable [] filterOperands;

  public ContentFilterElement(
      FilterOperator filterOperator, ExtensionObject @Nullable [] filterOperands) {
    this.filterOperator = filterOperator;
    this.filterOperands = filterOperands;
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
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public FilterOperator getFilterOperator() {
    return filterOperator;
  }

  public ExtensionObject @Nullable [] getFilterOperands() {
    return filterOperands;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ContentFilterElement that = (ContentFilterElement) object;
    var eqb = new EqualsBuilder();
    eqb.append(getFilterOperator(), that.getFilterOperator());
    eqb.append(getFilterOperands(), that.getFilterOperands());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getFilterOperator());
    hcb.append(getFilterOperands());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ContentFilterElement.class.getSimpleName() + "[", "]");
    joiner.add("filterOperator=" + getFilterOperator());
    joiner.add("filterOperands=" + java.util.Arrays.toString(getFilterOperands()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        NodeId.parse("i=585"),
        NodeId.parse("i=22"),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "FilterOperator",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=576"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "FilterOperands",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=22"),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ContentFilterElement> {
    @Override
    public Class<ContentFilterElement> getType() {
      return ContentFilterElement.class;
    }

    @Override
    public ContentFilterElement decodeType(EncodingContext context, UaDecoder decoder) {
      final FilterOperator filterOperator;
      final ExtensionObject[] filterOperands;
      {
        Integer enumValue = decoder.decodeEnum("FilterOperator");
        if (enumValue != null && !((Object) enumValue instanceof FilterOperator)) {
          if (!(enumValue instanceof Integer)) {
            throw new UaSerializationException(
                StatusCodes.Bad_TypeMismatch,
                "FilterOperator: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator or Int32,"
                    + " got "
                    + enumValue);
          }
          if (FilterOperator.from((Integer) enumValue) == null) {
            throw new UaSerializationException(
                StatusCodes.Bad_OutOfRange,
                "FilterOperator: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator value "
                    + enumValue);
          }
        }
        filterOperator = enumValue == null ? null : FilterOperator.from(enumValue);
      }
      filterOperands = decoder.decodeExtensionObjectArray("FilterOperands");
      return new ContentFilterElement(filterOperator, filterOperands);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ContentFilterElement value) {
      encoder.encodeEnum("FilterOperator", value.getFilterOperator());
      encoder.encodeExtensionObjectArray("FilterOperands", value.getFilterOperands());
    }
  }
}

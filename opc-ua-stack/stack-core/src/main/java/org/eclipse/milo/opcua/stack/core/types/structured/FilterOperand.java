/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4</a>
 */
public class FilterOperand extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=589");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=591");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=590");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15206");

  public FilterOperand() {}

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

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", FilterOperand.class.getSimpleName() + "[", "]");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 591), new NodeId(0, 22), StructureType.Structure, new StructureField[] {});
  }

  public static final class Codec extends GenericDataTypeCodec<FilterOperand> {
    @Override
    public Class<FilterOperand> getType() {
      return FilterOperand.class;
    }

    @Override
    public FilterOperand decodeType(EncodingContext context, UaDecoder decoder) {
      return new FilterOperand();
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, FilterOperand value) {}
  }
}

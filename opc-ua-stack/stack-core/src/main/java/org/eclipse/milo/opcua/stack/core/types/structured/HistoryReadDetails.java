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
 *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.1">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.1</a>
 */
public class HistoryReadDetails extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=641");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=643");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=642");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15261");

  public HistoryReadDetails() {}

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
    var joiner = new StringJoiner(", ", HistoryReadDetails.class.getSimpleName() + "[", "]");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 643), new NodeId(0, 22), StructureType.Structure, new StructureField[] {});
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryReadDetails> {
    @Override
    public Class<HistoryReadDetails> getType() {
      return HistoryReadDetails.class;
    }

    @Override
    public HistoryReadDetails decodeType(EncodingContext context, UaDecoder decoder) {
      return new HistoryReadDetails();
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryReadDetails value) {}
  }
}

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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31</a>
 */
public class RelativePath extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=540");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=542");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=541");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15189");

  private final RelativePathElement @Nullable [] elements;

  public RelativePath(RelativePathElement @Nullable [] elements) {
    this.elements = elements;
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

  public RelativePathElement @Nullable [] getElements() {
    return elements;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RelativePath that = (RelativePath) object;
    var eqb = new EqualsBuilder();
    eqb.append(getElements(), that.getElements());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getElements());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RelativePath.class.getSimpleName() + "[", "]");
    joiner.add("elements=" + java.util.Arrays.toString(getElements()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 542),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Elements",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 537),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RelativePath> {
    @Override
    public Class<RelativePath> getType() {
      return RelativePath.class;
    }

    @Override
    public RelativePath decodeType(EncodingContext context, UaDecoder decoder) {
      RelativePathElement[] elements =
          (RelativePathElement[])
              decoder.decodeStructArray("Elements", RelativePathElement.TYPE_ID);
      return new RelativePath(elements);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RelativePath value) {
      encoder.encodeStructArray("Elements", value.getElements(), RelativePathElement.TYPE_ID);
    }
  }
}

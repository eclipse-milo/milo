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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.6">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.6</a>
 */
public class BrowseResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=522");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=524");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=523");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15183");

  private final StatusCode statusCode;

  private final ByteString continuationPoint;

  private final ReferenceDescription @Nullable [] references;

  public BrowseResult(
      StatusCode statusCode,
      ByteString continuationPoint,
      ReferenceDescription @Nullable [] references) {
    this.statusCode = statusCode;
    this.continuationPoint = continuationPoint;
    this.references = references;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public ByteString getContinuationPoint() {
    return continuationPoint;
  }

  public ReferenceDescription @Nullable [] getReferences() {
    return references;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrowseResult that = (BrowseResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getContinuationPoint(), that.getContinuationPoint());
    eqb.append(getReferences(), that.getReferences());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getContinuationPoint());
    hcb.append(getReferences());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BrowseResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("continuationPoint=" + getContinuationPoint());
    joiner.add("references=" + java.util.Arrays.toString(getReferences()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 524),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ContinuationPoint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 521),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "References",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 518),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrowseResult> {
    @Override
    public Class<BrowseResult> getType() {
      return BrowseResult.class;
    }

    @Override
    public BrowseResult decodeType(EncodingContext context, UaDecoder decoder) {
      StatusCode statusCode = decoder.decodeStatusCode("StatusCode");
      ByteString continuationPoint = decoder.decodeByteString("ContinuationPoint");
      ReferenceDescription[] references =
          (ReferenceDescription[])
              decoder.decodeStructArray("References", ReferenceDescription.TYPE_ID);
      return new BrowseResult(statusCode, continuationPoint, references);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BrowseResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeByteString("ContinuationPoint", value.getContinuationPoint());
      encoder.encodeStructArray("References", value.getReferences(), ReferenceDescription.TYPE_ID);
    }
  }
}

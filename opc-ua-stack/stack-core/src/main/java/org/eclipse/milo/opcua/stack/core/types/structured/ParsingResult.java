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
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.3/#5.9.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.3/#5.9.3.1</a>
 */
public class ParsingResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=610");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=612");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=611");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15236");

  private final StatusCode statusCode;

  private final StatusCode @Nullable [] dataStatusCodes;

  private final DiagnosticInfo @Nullable [] dataDiagnosticInfos;

  public ParsingResult(
      StatusCode statusCode,
      StatusCode @Nullable [] dataStatusCodes,
      DiagnosticInfo @Nullable [] dataDiagnosticInfos) {
    this.statusCode = statusCode;
    this.dataStatusCodes = dataStatusCodes;
    this.dataDiagnosticInfos = dataDiagnosticInfos;
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

  public StatusCode @Nullable [] getDataStatusCodes() {
    return dataStatusCodes;
  }

  public DiagnosticInfo @Nullable [] getDataDiagnosticInfos() {
    return dataDiagnosticInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ParsingResult that = (ParsingResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getDataStatusCodes(), that.getDataStatusCodes());
    eqb.append(getDataDiagnosticInfos(), that.getDataDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getDataStatusCodes());
    hcb.append(getDataDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ParsingResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("dataStatusCodes=" + java.util.Arrays.toString(getDataStatusCodes()));
    joiner.add("dataDiagnosticInfos=" + java.util.Arrays.toString(getDataDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 612),
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
              "DataStatusCodes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataDiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ParsingResult> {
    @Override
    public Class<ParsingResult> getType() {
      return ParsingResult.class;
    }

    @Override
    public ParsingResult decodeType(EncodingContext context, UaDecoder decoder) {
      StatusCode statusCode = decoder.decodeStatusCode("StatusCode");
      StatusCode[] dataStatusCodes = decoder.decodeStatusCodeArray("DataStatusCodes");
      DiagnosticInfo[] dataDiagnosticInfos =
          decoder.decodeDiagnosticInfoArray("DataDiagnosticInfos");
      return new ParsingResult(statusCode, dataStatusCodes, dataDiagnosticInfos);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ParsingResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeStatusCodeArray("DataStatusCodes", value.getDataStatusCodes());
      encoder.encodeDiagnosticInfoArray("DataDiagnosticInfos", value.getDataDiagnosticInfos());
    }
  }
}

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
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.6.3/#5.6.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.6.3/#5.6.3.2</a>
 */
public class ActivateSessionResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=468");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=470");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=469");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15146");

  private final ResponseHeader responseHeader;

  private final ByteString serverNonce;

  private final StatusCode @Nullable [] results;

  private final DiagnosticInfo @Nullable [] diagnosticInfos;

  public ActivateSessionResponse(
      ResponseHeader responseHeader,
      ByteString serverNonce,
      StatusCode @Nullable [] results,
      DiagnosticInfo @Nullable [] diagnosticInfos) {
    this.responseHeader = responseHeader;
    this.serverNonce = serverNonce;
    this.results = results;
    this.diagnosticInfos = diagnosticInfos;
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

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public ByteString getServerNonce() {
    return serverNonce;
  }

  public StatusCode @Nullable [] getResults() {
    return results;
  }

  public DiagnosticInfo @Nullable [] getDiagnosticInfos() {
    return diagnosticInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ActivateSessionResponse that = (ActivateSessionResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getServerNonce(), that.getServerNonce());
    eqb.append(getResults(), that.getResults());
    eqb.append(getDiagnosticInfos(), that.getDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getServerNonce());
    hcb.append(getResults());
    hcb.append(getDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ActivateSessionResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("serverNonce=" + getServerNonce());
    joiner.add("results=" + java.util.Arrays.toString(getResults()));
    joiner.add("diagnosticInfos=" + java.util.Arrays.toString(getDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 470),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ResponseHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 392),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerNonce",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Results",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ActivateSessionResponse> {
    @Override
    public Class<ActivateSessionResponse> getType() {
      return ActivateSessionResponse.class;
    }

    @Override
    public ActivateSessionResponse decodeType(EncodingContext context, UaDecoder decoder) {
      ResponseHeader responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      ByteString serverNonce = decoder.decodeByteString("ServerNonce");
      StatusCode[] results = decoder.decodeStatusCodeArray("Results");
      DiagnosticInfo[] diagnosticInfos = decoder.decodeDiagnosticInfoArray("DiagnosticInfos");
      return new ActivateSessionResponse(responseHeader, serverNonce, results, diagnosticInfos);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ActivateSessionResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeByteString("ServerNonce", value.getServerNonce());
      encoder.encodeStatusCodeArray("Results", value.getResults());
      encoder.encodeDiagnosticInfoArray("DiagnosticInfos", value.getDiagnosticInfos());
    }
  }
}

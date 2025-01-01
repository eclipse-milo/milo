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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.5/#5.10.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.5/#5.10.5.2</a>
 */
public class HistoryUpdateResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=695");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=697");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=696");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15286");

  private final StatusCode statusCode;

  private final StatusCode @Nullable [] operationResults;

  private final DiagnosticInfo @Nullable [] diagnosticInfos;

  public HistoryUpdateResult(
      StatusCode statusCode,
      StatusCode @Nullable [] operationResults,
      DiagnosticInfo @Nullable [] diagnosticInfos) {
    this.statusCode = statusCode;
    this.operationResults = operationResults;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public StatusCode @Nullable [] getOperationResults() {
    return operationResults;
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
    HistoryUpdateResult that = (HistoryUpdateResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getOperationResults(), that.getOperationResults());
    eqb.append(getDiagnosticInfos(), that.getDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getOperationResults());
    hcb.append(getDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryUpdateResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("operationResults=" + java.util.Arrays.toString(getOperationResults()));
    joiner.add("diagnosticInfos=" + java.util.Arrays.toString(getDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 697),
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
              "OperationResults",
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

  public static final class Codec extends GenericDataTypeCodec<HistoryUpdateResult> {
    @Override
    public Class<HistoryUpdateResult> getType() {
      return HistoryUpdateResult.class;
    }

    @Override
    public HistoryUpdateResult decodeType(EncodingContext context, UaDecoder decoder) {
      StatusCode statusCode = decoder.decodeStatusCode("StatusCode");
      StatusCode[] operationResults = decoder.decodeStatusCodeArray("OperationResults");
      DiagnosticInfo[] diagnosticInfos = decoder.decodeDiagnosticInfoArray("DiagnosticInfos");
      return new HistoryUpdateResult(statusCode, operationResults, diagnosticInfos);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryUpdateResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeStatusCodeArray("OperationResults", value.getOperationResults());
      encoder.encodeDiagnosticInfoArray("DiagnosticInfos", value.getDiagnosticInfos());
    }
  }
}

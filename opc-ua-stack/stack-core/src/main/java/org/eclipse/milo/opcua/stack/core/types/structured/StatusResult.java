/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.14">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.14</a>
 */
public class StatusResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=299");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=301");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=300");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15371");

  private final StatusCode statusCode;

  private final DiagnosticInfo diagnosticInfo;

  public StatusResult(StatusCode statusCode, DiagnosticInfo diagnosticInfo) {
    this.statusCode = statusCode;
    this.diagnosticInfo = diagnosticInfo;
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

  public DiagnosticInfo getDiagnosticInfo() {
    return diagnosticInfo;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StatusResult that = (StatusResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getDiagnosticInfo(), that.getDiagnosticInfo());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getDiagnosticInfo());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", StatusResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("diagnosticInfo=" + getDiagnosticInfo());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 301),
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
              "DiagnosticInfo",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StatusResult> {
    @Override
    public Class<StatusResult> getType() {
      return StatusResult.class;
    }

    @Override
    public StatusResult decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode statusCode;
      final DiagnosticInfo diagnosticInfo;
      statusCode = decoder.decodeStatusCode("StatusCode");
      diagnosticInfo = decoder.decodeDiagnosticInfo("DiagnosticInfo");
      return new StatusResult(statusCode, diagnosticInfo);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, StatusResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeDiagnosticInfo("DiagnosticInfo", value.getDiagnosticInfo());
    }
  }
}

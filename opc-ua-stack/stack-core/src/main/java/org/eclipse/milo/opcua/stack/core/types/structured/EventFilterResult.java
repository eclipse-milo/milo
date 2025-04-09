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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3</a>
 */
public class EventFilterResult extends MonitoringFilterResult implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=734");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=736");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=735");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15314");

  private final StatusCode @Nullable [] selectClauseResults;

  private final DiagnosticInfo @Nullable [] selectClauseDiagnosticInfos;

  private final ContentFilterResult whereClauseResult;

  public EventFilterResult(
      StatusCode @Nullable [] selectClauseResults,
      DiagnosticInfo @Nullable [] selectClauseDiagnosticInfos,
      ContentFilterResult whereClauseResult) {
    this.selectClauseResults = selectClauseResults;
    this.selectClauseDiagnosticInfos = selectClauseDiagnosticInfos;
    this.whereClauseResult = whereClauseResult;
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

  public StatusCode @Nullable [] getSelectClauseResults() {
    return selectClauseResults;
  }

  public DiagnosticInfo @Nullable [] getSelectClauseDiagnosticInfos() {
    return selectClauseDiagnosticInfos;
  }

  public ContentFilterResult getWhereClauseResult() {
    return whereClauseResult;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EventFilterResult that = (EventFilterResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSelectClauseResults(), that.getSelectClauseResults());
    eqb.append(getSelectClauseDiagnosticInfos(), that.getSelectClauseDiagnosticInfos());
    eqb.append(getWhereClauseResult(), that.getWhereClauseResult());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSelectClauseResults());
    hcb.append(getSelectClauseDiagnosticInfos());
    hcb.append(getWhereClauseResult());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EventFilterResult.class.getSimpleName() + "[", "]");
    joiner.add("selectClauseResults=" + java.util.Arrays.toString(getSelectClauseResults()));
    joiner.add(
        "selectClauseDiagnosticInfos="
            + java.util.Arrays.toString(getSelectClauseDiagnosticInfos()));
    joiner.add("whereClauseResult=" + getWhereClauseResult());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 736),
        new NodeId(0, 731),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SelectClauseResults",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SelectClauseDiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "WhereClauseResult",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 607),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EventFilterResult> {
    @Override
    public Class<EventFilterResult> getType() {
      return EventFilterResult.class;
    }

    @Override
    public EventFilterResult decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode[] selectClauseResults;
      final DiagnosticInfo[] selectClauseDiagnosticInfos;
      final ContentFilterResult whereClauseResult;
      selectClauseResults = decoder.decodeStatusCodeArray("SelectClauseResults");
      selectClauseDiagnosticInfos =
          decoder.decodeDiagnosticInfoArray("SelectClauseDiagnosticInfos");
      whereClauseResult =
          (ContentFilterResult)
              decoder.decodeStruct("WhereClauseResult", ContentFilterResult.TYPE_ID);
      return new EventFilterResult(
          selectClauseResults, selectClauseDiagnosticInfos, whereClauseResult);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EventFilterResult value) {
      encoder.encodeStatusCodeArray("SelectClauseResults", value.getSelectClauseResults());
      encoder.encodeDiagnosticInfoArray(
          "SelectClauseDiagnosticInfos", value.getSelectClauseDiagnosticInfos());
      encoder.encodeStruct(
          "WhereClauseResult", value.getWhereClauseResult(), ContentFilterResult.TYPE_ID);
    }
  }
}

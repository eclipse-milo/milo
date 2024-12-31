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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.1</a>
 */
public class PriorityMappingEntryType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=25220");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=25239");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=25243");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=25247");

  private final @Nullable String mappingUri;

  private final @Nullable String priorityLabel;

  private final UByte priorityValuePcp;

  private final UInteger priorityValueDscp;

  public PriorityMappingEntryType(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      UByte priorityValuePcp,
      UInteger priorityValueDscp) {
    this.mappingUri = mappingUri;
    this.priorityLabel = priorityLabel;
    this.priorityValuePcp = priorityValuePcp;
    this.priorityValueDscp = priorityValueDscp;
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

  public @Nullable String getMappingUri() {
    return mappingUri;
  }

  public @Nullable String getPriorityLabel() {
    return priorityLabel;
  }

  public UByte getPriorityValuePcp() {
    return priorityValuePcp;
  }

  public UInteger getPriorityValueDscp() {
    return priorityValueDscp;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PriorityMappingEntryType that = (PriorityMappingEntryType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMappingUri(), that.getMappingUri());
    eqb.append(getPriorityLabel(), that.getPriorityLabel());
    eqb.append(getPriorityValuePcp(), that.getPriorityValuePcp());
    eqb.append(getPriorityValueDscp(), that.getPriorityValueDscp());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMappingUri());
    hcb.append(getPriorityLabel());
    hcb.append(getPriorityValuePcp());
    hcb.append(getPriorityValueDscp());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PriorityMappingEntryType.class.getSimpleName() + "[", "]");
    joiner.add("mappingUri='" + getMappingUri() + "'");
    joiner.add("priorityLabel='" + getPriorityLabel() + "'");
    joiner.add("priorityValuePcp=" + getPriorityValuePcp());
    joiner.add("priorityValueDscp=" + getPriorityValueDscp());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 25239),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MappingUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PriorityLabel",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PriorityValue_PCP",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PriorityValue_DSCP",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PriorityMappingEntryType> {
    @Override
    public Class<PriorityMappingEntryType> getType() {
      return PriorityMappingEntryType.class;
    }

    @Override
    public PriorityMappingEntryType decodeType(EncodingContext context, UaDecoder decoder) {
      String mappingUri = decoder.decodeString("MappingUri");
      String priorityLabel = decoder.decodeString("PriorityLabel");
      UByte priorityValuePcp = decoder.decodeByte("PriorityValue_PCP");
      UInteger priorityValueDscp = decoder.decodeUInt32("PriorityValue_DSCP");
      return new PriorityMappingEntryType(
          mappingUri, priorityLabel, priorityValuePcp, priorityValueDscp);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PriorityMappingEntryType value) {
      encoder.encodeString("MappingUri", value.getMappingUri());
      encoder.encodeString("PriorityLabel", value.getPriorityLabel());
      encoder.encodeByte("PriorityValue_PCP", value.getPriorityValuePcp());
      encoder.encodeUInt32("PriorityValue_DSCP", value.getPriorityValueDscp());
    }
  }
}

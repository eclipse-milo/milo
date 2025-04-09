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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

public class EndpointConfiguration extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=331");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=333");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=332");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15199");

  private final Integer operationTimeout;

  private final Boolean useBinaryEncoding;

  private final Integer maxStringLength;

  private final Integer maxByteStringLength;

  private final Integer maxArrayLength;

  private final Integer maxMessageSize;

  private final Integer maxBufferSize;

  private final Integer channelLifetime;

  private final Integer securityTokenLifetime;

  public EndpointConfiguration(
      Integer operationTimeout,
      Boolean useBinaryEncoding,
      Integer maxStringLength,
      Integer maxByteStringLength,
      Integer maxArrayLength,
      Integer maxMessageSize,
      Integer maxBufferSize,
      Integer channelLifetime,
      Integer securityTokenLifetime) {
    this.operationTimeout = operationTimeout;
    this.useBinaryEncoding = useBinaryEncoding;
    this.maxStringLength = maxStringLength;
    this.maxByteStringLength = maxByteStringLength;
    this.maxArrayLength = maxArrayLength;
    this.maxMessageSize = maxMessageSize;
    this.maxBufferSize = maxBufferSize;
    this.channelLifetime = channelLifetime;
    this.securityTokenLifetime = securityTokenLifetime;
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

  public Integer getOperationTimeout() {
    return operationTimeout;
  }

  public Boolean getUseBinaryEncoding() {
    return useBinaryEncoding;
  }

  public Integer getMaxStringLength() {
    return maxStringLength;
  }

  public Integer getMaxByteStringLength() {
    return maxByteStringLength;
  }

  public Integer getMaxArrayLength() {
    return maxArrayLength;
  }

  public Integer getMaxMessageSize() {
    return maxMessageSize;
  }

  public Integer getMaxBufferSize() {
    return maxBufferSize;
  }

  public Integer getChannelLifetime() {
    return channelLifetime;
  }

  public Integer getSecurityTokenLifetime() {
    return securityTokenLifetime;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EndpointConfiguration that = (EndpointConfiguration) object;
    var eqb = new EqualsBuilder();
    eqb.append(getOperationTimeout(), that.getOperationTimeout());
    eqb.append(getUseBinaryEncoding(), that.getUseBinaryEncoding());
    eqb.append(getMaxStringLength(), that.getMaxStringLength());
    eqb.append(getMaxByteStringLength(), that.getMaxByteStringLength());
    eqb.append(getMaxArrayLength(), that.getMaxArrayLength());
    eqb.append(getMaxMessageSize(), that.getMaxMessageSize());
    eqb.append(getMaxBufferSize(), that.getMaxBufferSize());
    eqb.append(getChannelLifetime(), that.getChannelLifetime());
    eqb.append(getSecurityTokenLifetime(), that.getSecurityTokenLifetime());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getOperationTimeout());
    hcb.append(getUseBinaryEncoding());
    hcb.append(getMaxStringLength());
    hcb.append(getMaxByteStringLength());
    hcb.append(getMaxArrayLength());
    hcb.append(getMaxMessageSize());
    hcb.append(getMaxBufferSize());
    hcb.append(getChannelLifetime());
    hcb.append(getSecurityTokenLifetime());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EndpointConfiguration.class.getSimpleName() + "[", "]");
    joiner.add("operationTimeout=" + getOperationTimeout());
    joiner.add("useBinaryEncoding=" + getUseBinaryEncoding());
    joiner.add("maxStringLength=" + getMaxStringLength());
    joiner.add("maxByteStringLength=" + getMaxByteStringLength());
    joiner.add("maxArrayLength=" + getMaxArrayLength());
    joiner.add("maxMessageSize=" + getMaxMessageSize());
    joiner.add("maxBufferSize=" + getMaxBufferSize());
    joiner.add("channelLifetime=" + getChannelLifetime());
    joiner.add("securityTokenLifetime=" + getSecurityTokenLifetime());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 333),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "OperationTimeout",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UseBinaryEncoding",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxStringLength",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxByteStringLength",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxArrayLength",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxBufferSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ChannelLifetime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityTokenLifetime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 6),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EndpointConfiguration> {
    @Override
    public Class<EndpointConfiguration> getType() {
      return EndpointConfiguration.class;
    }

    @Override
    public EndpointConfiguration decodeType(EncodingContext context, UaDecoder decoder) {
      final Integer operationTimeout;
      final Boolean useBinaryEncoding;
      final Integer maxStringLength;
      final Integer maxByteStringLength;
      final Integer maxArrayLength;
      final Integer maxMessageSize;
      final Integer maxBufferSize;
      final Integer channelLifetime;
      final Integer securityTokenLifetime;
      operationTimeout = decoder.decodeInt32("OperationTimeout");
      useBinaryEncoding = decoder.decodeBoolean("UseBinaryEncoding");
      maxStringLength = decoder.decodeInt32("MaxStringLength");
      maxByteStringLength = decoder.decodeInt32("MaxByteStringLength");
      maxArrayLength = decoder.decodeInt32("MaxArrayLength");
      maxMessageSize = decoder.decodeInt32("MaxMessageSize");
      maxBufferSize = decoder.decodeInt32("MaxBufferSize");
      channelLifetime = decoder.decodeInt32("ChannelLifetime");
      securityTokenLifetime = decoder.decodeInt32("SecurityTokenLifetime");
      return new EndpointConfiguration(
          operationTimeout,
          useBinaryEncoding,
          maxStringLength,
          maxByteStringLength,
          maxArrayLength,
          maxMessageSize,
          maxBufferSize,
          channelLifetime,
          securityTokenLifetime);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, EndpointConfiguration value) {
      encoder.encodeInt32("OperationTimeout", value.getOperationTimeout());
      encoder.encodeBoolean("UseBinaryEncoding", value.getUseBinaryEncoding());
      encoder.encodeInt32("MaxStringLength", value.getMaxStringLength());
      encoder.encodeInt32("MaxByteStringLength", value.getMaxByteStringLength());
      encoder.encodeInt32("MaxArrayLength", value.getMaxArrayLength());
      encoder.encodeInt32("MaxMessageSize", value.getMaxMessageSize());
      encoder.encodeInt32("MaxBufferSize", value.getMaxBufferSize());
      encoder.encodeInt32("ChannelLifetime", value.getChannelLifetime());
      encoder.encodeInt32("SecurityTokenLifetime", value.getSecurityTokenLifetime());
    }
  }
}

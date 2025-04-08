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
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.6.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.6.6</a>
 */
public class BrokerDataSetReaderTransportDataType extends DataSetReaderTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15670");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15733");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16023");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16526");

  private final @Nullable String queueName;

  private final @Nullable String resourceUri;

  private final @Nullable String authenticationProfileUri;

  private final BrokerTransportQualityOfService requestedDeliveryGuarantee;

  private final @Nullable String metaDataQueueName;

  public BrokerDataSetReaderTransportDataType(
      @Nullable String queueName,
      @Nullable String resourceUri,
      @Nullable String authenticationProfileUri,
      BrokerTransportQualityOfService requestedDeliveryGuarantee,
      @Nullable String metaDataQueueName) {
    this.queueName = queueName;
    this.resourceUri = resourceUri;
    this.authenticationProfileUri = authenticationProfileUri;
    this.requestedDeliveryGuarantee = requestedDeliveryGuarantee;
    this.metaDataQueueName = metaDataQueueName;
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

  public @Nullable String getQueueName() {
    return queueName;
  }

  public @Nullable String getResourceUri() {
    return resourceUri;
  }

  public @Nullable String getAuthenticationProfileUri() {
    return authenticationProfileUri;
  }

  public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
    return requestedDeliveryGuarantee;
  }

  public @Nullable String getMetaDataQueueName() {
    return metaDataQueueName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrokerDataSetReaderTransportDataType that = (BrokerDataSetReaderTransportDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getQueueName(), that.getQueueName());
    eqb.append(getResourceUri(), that.getResourceUri());
    eqb.append(getAuthenticationProfileUri(), that.getAuthenticationProfileUri());
    eqb.append(getRequestedDeliveryGuarantee(), that.getRequestedDeliveryGuarantee());
    eqb.append(getMetaDataQueueName(), that.getMetaDataQueueName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getQueueName());
    hcb.append(getResourceUri());
    hcb.append(getAuthenticationProfileUri());
    hcb.append(getRequestedDeliveryGuarantee());
    hcb.append(getMetaDataQueueName());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", BrokerDataSetReaderTransportDataType.class.getSimpleName() + "[", "]");
    joiner.add("queueName='" + getQueueName() + "'");
    joiner.add("resourceUri='" + getResourceUri() + "'");
    joiner.add("authenticationProfileUri='" + getAuthenticationProfileUri() + "'");
    joiner.add("requestedDeliveryGuarantee=" + getRequestedDeliveryGuarantee());
    joiner.add("metaDataQueueName='" + getMetaDataQueueName() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15733),
        new NodeId(0, 15628),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "QueueName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ResourceUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuthenticationProfileUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedDeliveryGuarantee",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15008),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MetaDataQueueName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<BrokerDataSetReaderTransportDataType> {
    @Override
    public Class<BrokerDataSetReaderTransportDataType> getType() {
      return BrokerDataSetReaderTransportDataType.class;
    }

    @Override
    public BrokerDataSetReaderTransportDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final String queueName;
      final String resourceUri;
      final String authenticationProfileUri;
      final BrokerTransportQualityOfService requestedDeliveryGuarantee;
      final String metaDataQueueName;
      queueName = decoder.decodeString("QueueName");
      resourceUri = decoder.decodeString("ResourceUri");
      authenticationProfileUri = decoder.decodeString("AuthenticationProfileUri");
      requestedDeliveryGuarantee =
          BrokerTransportQualityOfService.from(decoder.decodeEnum("RequestedDeliveryGuarantee"));
      metaDataQueueName = decoder.decodeString("MetaDataQueueName");
      return new BrokerDataSetReaderTransportDataType(
          queueName,
          resourceUri,
          authenticationProfileUri,
          requestedDeliveryGuarantee,
          metaDataQueueName);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, BrokerDataSetReaderTransportDataType value) {
      encoder.encodeString("QueueName", value.getQueueName());
      encoder.encodeString("ResourceUri", value.getResourceUri());
      encoder.encodeString("AuthenticationProfileUri", value.getAuthenticationProfileUri());
      encoder.encodeEnum("RequestedDeliveryGuarantee", value.getRequestedDeliveryGuarantee());
      encoder.encodeString("MetaDataQueueName", value.getMetaDataQueueName());
    }
  }
}

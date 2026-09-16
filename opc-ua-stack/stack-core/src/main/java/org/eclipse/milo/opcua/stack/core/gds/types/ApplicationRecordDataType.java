/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.gds.types;

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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/GDS/docs/6.5.5">https://reference.opcfoundation.org/GDS/docs/6.5.5</a>
 */
public class ApplicationRecordDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/GDS/;i=1");

  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/GDS/;i=134");

  public static final ExpandedNodeId XML_ENCODING_ID =
      ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/GDS/;i=127");

  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/GDS/;i=8001");

  private final NodeId applicationId;

  private final @Nullable String applicationUri;

  private final ApplicationType applicationType;

  private final LocalizedText @Nullable [] applicationNames;

  private final @Nullable String productUri;

  private final String @Nullable [] discoveryUrls;

  private final String @Nullable [] serverCapabilities;

  public ApplicationRecordDataType(
      NodeId applicationId,
      @Nullable String applicationUri,
      ApplicationType applicationType,
      LocalizedText @Nullable [] applicationNames,
      @Nullable String productUri,
      String @Nullable [] discoveryUrls,
      String @Nullable [] serverCapabilities) {
    this.applicationId = applicationId;
    this.applicationUri = applicationUri;
    this.applicationType = applicationType;
    this.applicationNames = applicationNames;
    this.productUri = productUri;
    this.discoveryUrls = discoveryUrls;
    this.serverCapabilities = serverCapabilities;
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

  public NodeId getApplicationId() {
    return applicationId;
  }

  public @Nullable String getApplicationUri() {
    return applicationUri;
  }

  public ApplicationType getApplicationType() {
    return applicationType;
  }

  public LocalizedText @Nullable [] getApplicationNames() {
    return applicationNames;
  }

  public @Nullable String getProductUri() {
    return productUri;
  }

  public String @Nullable [] getDiscoveryUrls() {
    return discoveryUrls;
  }

  public String @Nullable [] getServerCapabilities() {
    return serverCapabilities;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ApplicationRecordDataType that = (ApplicationRecordDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getApplicationId(), that.getApplicationId());
    eqb.append(getApplicationUri(), that.getApplicationUri());
    eqb.append(getApplicationType(), that.getApplicationType());
    eqb.append(getApplicationNames(), that.getApplicationNames());
    eqb.append(getProductUri(), that.getProductUri());
    eqb.append(getDiscoveryUrls(), that.getDiscoveryUrls());
    eqb.append(getServerCapabilities(), that.getServerCapabilities());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getApplicationId());
    hcb.append(getApplicationUri());
    hcb.append(getApplicationType());
    hcb.append(getApplicationNames());
    hcb.append(getProductUri());
    hcb.append(getDiscoveryUrls());
    hcb.append(getServerCapabilities());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ApplicationRecordDataType.class.getSimpleName() + "[", "]");
    joiner.add("applicationId=" + getApplicationId());
    joiner.add("applicationUri='" + getApplicationUri() + "'");
    joiner.add("applicationType=" + getApplicationType());
    joiner.add("applicationNames=" + java.util.Arrays.toString(getApplicationNames()));
    joiner.add("productUri='" + getProductUri() + "'");
    joiner.add("discoveryUrls=" + java.util.Arrays.toString(getDiscoveryUrls()));
    joiner.add("serverCapabilities=" + java.util.Arrays.toString(getServerCapabilities()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/GDS/;i=134")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=22")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ApplicationId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ApplicationUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ApplicationType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 307),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ApplicationNames",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ProductUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscoveryUrls",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerCapabilities",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ApplicationRecordDataType> {
    @Override
    public Class<ApplicationRecordDataType> getType() {
      return ApplicationRecordDataType.class;
    }

    @Override
    public ApplicationRecordDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId applicationId;
      final String applicationUri;
      final ApplicationType applicationType;
      final LocalizedText[] applicationNames;
      final String productUri;
      final String[] discoveryUrls;
      final String[] serverCapabilities;
      applicationId = decoder.decodeNodeId("ApplicationId");
      applicationUri = decoder.decodeString("ApplicationUri");
      applicationType = ApplicationType.from(decoder.decodeEnum("ApplicationType"));
      applicationNames = decoder.decodeLocalizedTextArray("ApplicationNames");
      productUri = decoder.decodeString("ProductUri");
      discoveryUrls = decoder.decodeStringArray("DiscoveryUrls");
      serverCapabilities = decoder.decodeStringArray("ServerCapabilities");
      return new ApplicationRecordDataType(
          applicationId,
          applicationUri,
          applicationType,
          applicationNames,
          productUri,
          discoveryUrls,
          serverCapabilities);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ApplicationRecordDataType value) {
      encoder.encodeNodeId("ApplicationId", value.getApplicationId());
      encoder.encodeString("ApplicationUri", value.getApplicationUri());
      encoder.encodeEnum("ApplicationType", value.getApplicationType());
      encoder.encodeLocalizedTextArray("ApplicationNames", value.getApplicationNames());
      encoder.encodeString("ProductUri", value.getProductUri());
      encoder.encodeStringArray("DiscoveryUrls", value.getDiscoveryUrls());
      encoder.encodeStringArray("ServerCapabilities", value.getServerCapabilities());
    }
  }
}

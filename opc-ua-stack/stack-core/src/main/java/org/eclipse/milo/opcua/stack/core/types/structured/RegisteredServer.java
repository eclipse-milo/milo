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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.32">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.32</a>
 */
public class RegisteredServer extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=432");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=434");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=433");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15102");

  private final @Nullable String serverUri;

  private final @Nullable String productUri;

  private final LocalizedText @Nullable [] serverNames;

  private final ApplicationType serverType;

  private final @Nullable String gatewayServerUri;

  private final String @Nullable [] discoveryUrls;

  private final @Nullable String semaphoreFilePath;

  private final Boolean isOnline;

  public RegisteredServer(
      @Nullable String serverUri,
      @Nullable String productUri,
      LocalizedText @Nullable [] serverNames,
      ApplicationType serverType,
      @Nullable String gatewayServerUri,
      String @Nullable [] discoveryUrls,
      @Nullable String semaphoreFilePath,
      Boolean isOnline) {
    this.serverUri = serverUri;
    this.productUri = productUri;
    this.serverNames = serverNames;
    this.serverType = serverType;
    this.gatewayServerUri = gatewayServerUri;
    this.discoveryUrls = discoveryUrls;
    this.semaphoreFilePath = semaphoreFilePath;
    this.isOnline = isOnline;
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

  public @Nullable String getServerUri() {
    return serverUri;
  }

  public @Nullable String getProductUri() {
    return productUri;
  }

  public LocalizedText @Nullable [] getServerNames() {
    return serverNames;
  }

  public ApplicationType getServerType() {
    return serverType;
  }

  public @Nullable String getGatewayServerUri() {
    return gatewayServerUri;
  }

  public String @Nullable [] getDiscoveryUrls() {
    return discoveryUrls;
  }

  public @Nullable String getSemaphoreFilePath() {
    return semaphoreFilePath;
  }

  public Boolean getIsOnline() {
    return isOnline;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RegisteredServer that = (RegisteredServer) object;
    var eqb = new EqualsBuilder();
    eqb.append(getServerUri(), that.getServerUri());
    eqb.append(getProductUri(), that.getProductUri());
    eqb.append(getServerNames(), that.getServerNames());
    eqb.append(getServerType(), that.getServerType());
    eqb.append(getGatewayServerUri(), that.getGatewayServerUri());
    eqb.append(getDiscoveryUrls(), that.getDiscoveryUrls());
    eqb.append(getSemaphoreFilePath(), that.getSemaphoreFilePath());
    eqb.append(getIsOnline(), that.getIsOnline());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getServerUri());
    hcb.append(getProductUri());
    hcb.append(getServerNames());
    hcb.append(getServerType());
    hcb.append(getGatewayServerUri());
    hcb.append(getDiscoveryUrls());
    hcb.append(getSemaphoreFilePath());
    hcb.append(getIsOnline());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RegisteredServer.class.getSimpleName() + "[", "]");
    joiner.add("serverUri='" + getServerUri() + "'");
    joiner.add("productUri='" + getProductUri() + "'");
    joiner.add("serverNames=" + java.util.Arrays.toString(getServerNames()));
    joiner.add("serverType=" + getServerType());
    joiner.add("gatewayServerUri='" + getGatewayServerUri() + "'");
    joiner.add("discoveryUrls=" + java.util.Arrays.toString(getDiscoveryUrls()));
    joiner.add("semaphoreFilePath='" + getSemaphoreFilePath() + "'");
    joiner.add("isOnline=" + getIsOnline());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 434),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ServerUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
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
              "ServerNames",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 307),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "GatewayServerUri",
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
              "SemaphoreFilePath",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsOnline",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RegisteredServer> {
    @Override
    public Class<RegisteredServer> getType() {
      return RegisteredServer.class;
    }

    @Override
    public RegisteredServer decodeType(EncodingContext context, UaDecoder decoder) {
      final String serverUri;
      final String productUri;
      final LocalizedText[] serverNames;
      final ApplicationType serverType;
      final String gatewayServerUri;
      final String[] discoveryUrls;
      final String semaphoreFilePath;
      final Boolean isOnline;
      serverUri = decoder.decodeString("ServerUri");
      productUri = decoder.decodeString("ProductUri");
      serverNames = decoder.decodeLocalizedTextArray("ServerNames");
      serverType = ApplicationType.from(decoder.decodeEnum("ServerType"));
      gatewayServerUri = decoder.decodeString("GatewayServerUri");
      discoveryUrls = decoder.decodeStringArray("DiscoveryUrls");
      semaphoreFilePath = decoder.decodeString("SemaphoreFilePath");
      isOnline = decoder.decodeBoolean("IsOnline");
      return new RegisteredServer(
          serverUri,
          productUri,
          serverNames,
          serverType,
          gatewayServerUri,
          discoveryUrls,
          semaphoreFilePath,
          isOnline);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RegisteredServer value) {
      encoder.encodeString("ServerUri", value.getServerUri());
      encoder.encodeString("ProductUri", value.getProductUri());
      encoder.encodeLocalizedTextArray("ServerNames", value.getServerNames());
      encoder.encodeEnum("ServerType", value.getServerType());
      encoder.encodeString("GatewayServerUri", value.getGatewayServerUri());
      encoder.encodeStringArray("DiscoveryUrls", value.getDiscoveryUrls());
      encoder.encodeString("SemaphoreFilePath", value.getSemaphoreFilePath());
      encoder.encodeBoolean("IsOnline", value.getIsOnline());
    }
  }
}

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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.7.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.1/#6.4.1.7.6</a>
 */
public class DtlsPubSubConnectionDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18794");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=18930");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=18938");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=18946");

  private final @Nullable String clientCipherSuite;

  private final String @Nullable [] serverCipherSuites;

  private final Boolean zeroRtt;

  private final NodeId certificateGroupId;

  private final Boolean verifyClientCertificate;

  public DtlsPubSubConnectionDataType(
      @Nullable String clientCipherSuite,
      String @Nullable [] serverCipherSuites,
      Boolean zeroRtt,
      NodeId certificateGroupId,
      Boolean verifyClientCertificate) {
    this.clientCipherSuite = clientCipherSuite;
    this.serverCipherSuites = serverCipherSuites;
    this.zeroRtt = zeroRtt;
    this.certificateGroupId = certificateGroupId;
    this.verifyClientCertificate = verifyClientCertificate;
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

  public @Nullable String getClientCipherSuite() {
    return clientCipherSuite;
  }

  public String @Nullable [] getServerCipherSuites() {
    return serverCipherSuites;
  }

  public Boolean getZeroRtt() {
    return zeroRtt;
  }

  public NodeId getCertificateGroupId() {
    return certificateGroupId;
  }

  public Boolean getVerifyClientCertificate() {
    return verifyClientCertificate;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DtlsPubSubConnectionDataType that = (DtlsPubSubConnectionDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getClientCipherSuite(), that.getClientCipherSuite());
    eqb.append(getServerCipherSuites(), that.getServerCipherSuites());
    eqb.append(getZeroRtt(), that.getZeroRtt());
    eqb.append(getCertificateGroupId(), that.getCertificateGroupId());
    eqb.append(getVerifyClientCertificate(), that.getVerifyClientCertificate());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getClientCipherSuite());
    hcb.append(getServerCipherSuites());
    hcb.append(getZeroRtt());
    hcb.append(getCertificateGroupId());
    hcb.append(getVerifyClientCertificate());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", DtlsPubSubConnectionDataType.class.getSimpleName() + "[", "]");
    joiner.add("clientCipherSuite='" + getClientCipherSuite() + "'");
    joiner.add("serverCipherSuites=" + java.util.Arrays.toString(getServerCipherSuites()));
    joiner.add("zeroRtt=" + getZeroRtt());
    joiner.add("certificateGroupId=" + getCertificateGroupId());
    joiner.add("verifyClientCertificate=" + getVerifyClientCertificate());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18930),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ClientCipherSuite",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerCipherSuites",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ZeroRTT",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CertificateGroupId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "VerifyClientCertificate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DtlsPubSubConnectionDataType> {
    @Override
    public Class<DtlsPubSubConnectionDataType> getType() {
      return DtlsPubSubConnectionDataType.class;
    }

    @Override
    public DtlsPubSubConnectionDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String clientCipherSuite;
      final String[] serverCipherSuites;
      final Boolean zeroRtt;
      final NodeId certificateGroupId;
      final Boolean verifyClientCertificate;
      clientCipherSuite = decoder.decodeString("ClientCipherSuite");
      serverCipherSuites = decoder.decodeStringArray("ServerCipherSuites");
      zeroRtt = decoder.decodeBoolean("ZeroRTT");
      certificateGroupId = decoder.decodeNodeId("CertificateGroupId");
      verifyClientCertificate = decoder.decodeBoolean("VerifyClientCertificate");
      return new DtlsPubSubConnectionDataType(
          clientCipherSuite,
          serverCipherSuites,
          zeroRtt,
          certificateGroupId,
          verifyClientCertificate);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DtlsPubSubConnectionDataType value) {
      encoder.encodeString("ClientCipherSuite", value.getClientCipherSuite());
      encoder.encodeStringArray("ServerCipherSuites", value.getServerCipherSuites());
      encoder.encodeBoolean("ZeroRTT", value.getZeroRtt());
      encoder.encodeNodeId("CertificateGroupId", value.getCertificateGroupId());
      encoder.encodeBoolean("VerifyClientCertificate", value.getVerifyClientCertificate());
    }
  }
}

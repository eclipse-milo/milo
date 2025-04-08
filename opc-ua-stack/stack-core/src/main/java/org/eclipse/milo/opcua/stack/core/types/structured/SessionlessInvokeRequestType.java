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

public class SessionlessInvokeRequestType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15901");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15903");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15902");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15091");

  private final UInteger urisVersion;

  private final String @Nullable [] namespaceUris;

  private final String @Nullable [] serverUris;

  private final String @Nullable [] localeIds;

  private final UInteger serviceId;

  public SessionlessInvokeRequestType(
      UInteger urisVersion,
      String @Nullable [] namespaceUris,
      String @Nullable [] serverUris,
      String @Nullable [] localeIds,
      UInteger serviceId) {
    this.urisVersion = urisVersion;
    this.namespaceUris = namespaceUris;
    this.serverUris = serverUris;
    this.localeIds = localeIds;
    this.serviceId = serviceId;
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

  public UInteger getUrisVersion() {
    return urisVersion;
  }

  public String @Nullable [] getNamespaceUris() {
    return namespaceUris;
  }

  public String @Nullable [] getServerUris() {
    return serverUris;
  }

  public String @Nullable [] getLocaleIds() {
    return localeIds;
  }

  public UInteger getServiceId() {
    return serviceId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SessionlessInvokeRequestType that = (SessionlessInvokeRequestType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getUrisVersion(), that.getUrisVersion());
    eqb.append(getNamespaceUris(), that.getNamespaceUris());
    eqb.append(getServerUris(), that.getServerUris());
    eqb.append(getLocaleIds(), that.getLocaleIds());
    eqb.append(getServiceId(), that.getServiceId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getUrisVersion());
    hcb.append(getNamespaceUris());
    hcb.append(getServerUris());
    hcb.append(getLocaleIds());
    hcb.append(getServiceId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SessionlessInvokeRequestType.class.getSimpleName() + "[", "]");
    joiner.add("urisVersion=" + getUrisVersion());
    joiner.add("namespaceUris=" + java.util.Arrays.toString(getNamespaceUris()));
    joiner.add("serverUris=" + java.util.Arrays.toString(getServerUris()));
    joiner.add("localeIds=" + java.util.Arrays.toString(getLocaleIds()));
    joiner.add("serviceId=" + getServiceId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15903),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "UrisVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NamespaceUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocaleIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 295),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServiceId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SessionlessInvokeRequestType> {
    @Override
    public Class<SessionlessInvokeRequestType> getType() {
      return SessionlessInvokeRequestType.class;
    }

    @Override
    public SessionlessInvokeRequestType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger urisVersion;
      final String[] namespaceUris;
      final String[] serverUris;
      final String[] localeIds;
      final UInteger serviceId;
      urisVersion = decoder.decodeUInt32("UrisVersion");
      namespaceUris = decoder.decodeStringArray("NamespaceUris");
      serverUris = decoder.decodeStringArray("ServerUris");
      localeIds = decoder.decodeStringArray("LocaleIds");
      serviceId = decoder.decodeUInt32("ServiceId");
      return new SessionlessInvokeRequestType(
          urisVersion, namespaceUris, serverUris, localeIds, serviceId);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SessionlessInvokeRequestType value) {
      encoder.encodeUInt32("UrisVersion", value.getUrisVersion());
      encoder.encodeStringArray("NamespaceUris", value.getNamespaceUris());
      encoder.encodeStringArray("ServerUris", value.getServerUris());
      encoder.encodeStringArray("LocaleIds", value.getLocaleIds());
      encoder.encodeUInt32("ServiceId", value.getServiceId());
    }
  }
}

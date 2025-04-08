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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.3</a>
 */
public class PubSubKeyPushTargetDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=25270");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25530");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25546");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25562");

  private final @Nullable String applicationUri;

  private final String @Nullable [] pushTargetFolder;

  private final @Nullable String endpointUrl;

  private final @Nullable String securityPolicyUri;

  private final UserTokenPolicy userTokenType;

  private final UShort requestedKeyCount;

  private final Double retryInterval;

  private final KeyValuePair @Nullable [] pushTargetProperties;

  private final String @Nullable [] securityGroups;

  public PubSubKeyPushTargetDataType(
      @Nullable String applicationUri,
      String @Nullable [] pushTargetFolder,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      UserTokenPolicy userTokenType,
      UShort requestedKeyCount,
      Double retryInterval,
      KeyValuePair @Nullable [] pushTargetProperties,
      String @Nullable [] securityGroups) {
    this.applicationUri = applicationUri;
    this.pushTargetFolder = pushTargetFolder;
    this.endpointUrl = endpointUrl;
    this.securityPolicyUri = securityPolicyUri;
    this.userTokenType = userTokenType;
    this.requestedKeyCount = requestedKeyCount;
    this.retryInterval = retryInterval;
    this.pushTargetProperties = pushTargetProperties;
    this.securityGroups = securityGroups;
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

  public @Nullable String getApplicationUri() {
    return applicationUri;
  }

  public String @Nullable [] getPushTargetFolder() {
    return pushTargetFolder;
  }

  public @Nullable String getEndpointUrl() {
    return endpointUrl;
  }

  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  public UserTokenPolicy getUserTokenType() {
    return userTokenType;
  }

  public UShort getRequestedKeyCount() {
    return requestedKeyCount;
  }

  public Double getRetryInterval() {
    return retryInterval;
  }

  public KeyValuePair @Nullable [] getPushTargetProperties() {
    return pushTargetProperties;
  }

  public String @Nullable [] getSecurityGroups() {
    return securityGroups;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubKeyPushTargetDataType that = (PubSubKeyPushTargetDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getApplicationUri(), that.getApplicationUri());
    eqb.append(getPushTargetFolder(), that.getPushTargetFolder());
    eqb.append(getEndpointUrl(), that.getEndpointUrl());
    eqb.append(getSecurityPolicyUri(), that.getSecurityPolicyUri());
    eqb.append(getUserTokenType(), that.getUserTokenType());
    eqb.append(getRequestedKeyCount(), that.getRequestedKeyCount());
    eqb.append(getRetryInterval(), that.getRetryInterval());
    eqb.append(getPushTargetProperties(), that.getPushTargetProperties());
    eqb.append(getSecurityGroups(), that.getSecurityGroups());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getApplicationUri());
    hcb.append(getPushTargetFolder());
    hcb.append(getEndpointUrl());
    hcb.append(getSecurityPolicyUri());
    hcb.append(getUserTokenType());
    hcb.append(getRequestedKeyCount());
    hcb.append(getRetryInterval());
    hcb.append(getPushTargetProperties());
    hcb.append(getSecurityGroups());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PubSubKeyPushTargetDataType.class.getSimpleName() + "[", "]");
    joiner.add("applicationUri='" + getApplicationUri() + "'");
    joiner.add("pushTargetFolder=" + java.util.Arrays.toString(getPushTargetFolder()));
    joiner.add("endpointUrl='" + getEndpointUrl() + "'");
    joiner.add("securityPolicyUri='" + getSecurityPolicyUri() + "'");
    joiner.add("userTokenType=" + getUserTokenType());
    joiner.add("requestedKeyCount=" + getRequestedKeyCount());
    joiner.add("retryInterval=" + getRetryInterval());
    joiner.add("pushTargetProperties=" + java.util.Arrays.toString(getPushTargetProperties()));
    joiner.add("securityGroups=" + java.util.Arrays.toString(getSecurityGroups()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 25530),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ApplicationUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PushTargetFolder",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EndpointUrl",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityPolicyUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserTokenType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 304),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedKeyCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RetryInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PushTargetProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityGroups",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PubSubKeyPushTargetDataType> {
    @Override
    public Class<PubSubKeyPushTargetDataType> getType() {
      return PubSubKeyPushTargetDataType.class;
    }

    @Override
    public PubSubKeyPushTargetDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String applicationUri;
      final String[] pushTargetFolder;
      final String endpointUrl;
      final String securityPolicyUri;
      final UserTokenPolicy userTokenType;
      final UShort requestedKeyCount;
      final Double retryInterval;
      final KeyValuePair[] pushTargetProperties;
      final String[] securityGroups;
      applicationUri = decoder.decodeString("ApplicationUri");
      pushTargetFolder = decoder.decodeStringArray("PushTargetFolder");
      endpointUrl = decoder.decodeString("EndpointUrl");
      securityPolicyUri = decoder.decodeString("SecurityPolicyUri");
      userTokenType =
          (UserTokenPolicy) decoder.decodeStruct("UserTokenType", UserTokenPolicy.TYPE_ID);
      requestedKeyCount = decoder.decodeUInt16("RequestedKeyCount");
      retryInterval = decoder.decodeDouble("RetryInterval");
      pushTargetProperties =
          (KeyValuePair[]) decoder.decodeStructArray("PushTargetProperties", KeyValuePair.TYPE_ID);
      securityGroups = decoder.decodeStringArray("SecurityGroups");
      return new PubSubKeyPushTargetDataType(
          applicationUri,
          pushTargetFolder,
          endpointUrl,
          securityPolicyUri,
          userTokenType,
          requestedKeyCount,
          retryInterval,
          pushTargetProperties,
          securityGroups);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PubSubKeyPushTargetDataType value) {
      encoder.encodeString("ApplicationUri", value.getApplicationUri());
      encoder.encodeStringArray("PushTargetFolder", value.getPushTargetFolder());
      encoder.encodeString("EndpointUrl", value.getEndpointUrl());
      encoder.encodeString("SecurityPolicyUri", value.getSecurityPolicyUri());
      encoder.encodeStruct("UserTokenType", value.getUserTokenType(), UserTokenPolicy.TYPE_ID);
      encoder.encodeUInt16("RequestedKeyCount", value.getRequestedKeyCount());
      encoder.encodeDouble("RetryInterval", value.getRetryInterval());
      encoder.encodeStructArray(
          "PushTargetProperties", value.getPushTargetProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeStringArray("SecurityGroups", value.getSecurityGroups());
    }
  }
}

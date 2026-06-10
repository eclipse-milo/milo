/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.24">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.24</a>
 */
public class SecuritySettingsDataType extends BaseConfigurationRecordDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15559");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16546");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16595");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16644");

  private final MessageSecurityMode @Nullable [] securityModes;

  private final String @Nullable [] securityPolicyUris;

  private final @Nullable String certificateGroupName;

  public SecuritySettingsDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      MessageSecurityMode @Nullable [] securityModes,
      String @Nullable [] securityPolicyUris,
      @Nullable String certificateGroupName) {
    super(name, recordProperties);
    this.securityModes = securityModes;
    this.securityPolicyUris = securityPolicyUris;
    this.certificateGroupName = certificateGroupName;
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

  public MessageSecurityMode @Nullable [] getSecurityModes() {
    return securityModes;
  }

  public String @Nullable [] getSecurityPolicyUris() {
    return securityPolicyUris;
  }

  public @Nullable String getCertificateGroupName() {
    return certificateGroupName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SecuritySettingsDataType that = (SecuritySettingsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getSecurityModes(), that.getSecurityModes());
    eqb.append(getSecurityPolicyUris(), that.getSecurityPolicyUris());
    eqb.append(getCertificateGroupName(), that.getCertificateGroupName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSecurityModes());
    hcb.append(getSecurityPolicyUris());
    hcb.append(getCertificateGroupName());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SecuritySettingsDataType.class.getSimpleName() + "[", "]");
    joiner.add("securityModes=" + java.util.Arrays.toString(getSecurityModes()));
    joiner.add("securityPolicyUris=" + java.util.Arrays.toString(getSecurityPolicyUris()));
    joiner.add("certificateGroupName='" + getCertificateGroupName() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16546),
        new NodeId(0, 15435),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RecordProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityModes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 302),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityPolicyUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CertificateGroupName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SecuritySettingsDataType> {
    @Override
    public Class<SecuritySettingsDataType> getType() {
      return SecuritySettingsDataType.class;
    }

    @Override
    public SecuritySettingsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final MessageSecurityMode[] securityModes;
      final String[] securityPolicyUris;
      final String certificateGroupName;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      {
        Integer[] values = decoder.decodeEnumArray("SecurityModes");
        if (values != null) {
          securityModes = new MessageSecurityMode[values.length];
          for (int i = 0; i < values.length; i++) {
            securityModes[i] = MessageSecurityMode.from(values[i]);
          }
        } else {
          securityModes = null;
        }
      }
      securityPolicyUris = decoder.decodeStringArray("SecurityPolicyUris");
      certificateGroupName = decoder.decodeString("CertificateGroupName");
      return new SecuritySettingsDataType(
          name, recordProperties, securityModes, securityPolicyUris, certificateGroupName);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SecuritySettingsDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeEnumArray("SecurityModes", value.getSecurityModes());
      encoder.encodeStringArray("SecurityPolicyUris", value.getSecurityPolicyUris());
      encoder.encodeString("CertificateGroupName", value.getCertificateGroupName());
    }
  }
}

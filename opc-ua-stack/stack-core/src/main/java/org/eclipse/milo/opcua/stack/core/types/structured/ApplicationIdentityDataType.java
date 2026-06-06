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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.21">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.21</a>
 */
public class ApplicationIdentityDataType extends BaseConfigurationRecordDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15556");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16543");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16592");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16637");

  private final String applicationUri;

  private final LocalizedText @Nullable [] applicationNames;

  private final ApplicationDescription @Nullable [] additionalServers;

  public ApplicationIdentityDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      String applicationUri,
      LocalizedText @Nullable [] applicationNames,
      ApplicationDescription @Nullable [] additionalServers) {
    super(name, recordProperties);
    this.applicationUri = applicationUri;
    this.applicationNames = applicationNames;
    this.additionalServers = additionalServers;
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

  public String getApplicationUri() {
    return applicationUri;
  }

  public LocalizedText @Nullable [] getApplicationNames() {
    return applicationNames;
  }

  public ApplicationDescription @Nullable [] getAdditionalServers() {
    return additionalServers;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ApplicationIdentityDataType that = (ApplicationIdentityDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getApplicationUri(), that.getApplicationUri());
    eqb.append(getApplicationNames(), that.getApplicationNames());
    eqb.append(getAdditionalServers(), that.getAdditionalServers());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getApplicationUri());
    hcb.append(getApplicationNames());
    hcb.append(getAdditionalServers());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ApplicationIdentityDataType.class.getSimpleName() + "[", "]");
    joiner.add("applicationUri='" + getApplicationUri() + "'");
    joiner.add("applicationNames=" + java.util.Arrays.toString(getApplicationNames()));
    joiner.add("additionalServers=" + java.util.Arrays.toString(getAdditionalServers()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16543),
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
              "ApplicationUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23751),
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
              "AdditionalServers",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 308),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ApplicationIdentityDataType> {
    @Override
    public Class<ApplicationIdentityDataType> getType() {
      return ApplicationIdentityDataType.class;
    }

    @Override
    public ApplicationIdentityDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final String applicationUri;
      final LocalizedText[] applicationNames;
      final ApplicationDescription[] additionalServers;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      applicationUri = decoder.decodeString("ApplicationUri");
      applicationNames = decoder.decodeLocalizedTextArray("ApplicationNames");
      additionalServers =
          (ApplicationDescription[])
              decoder.decodeStructArray("AdditionalServers", ApplicationDescription.TYPE_ID);
      return new ApplicationIdentityDataType(
          name, recordProperties, applicationUri, applicationNames, additionalServers);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ApplicationIdentityDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeString("ApplicationUri", value.getApplicationUri());
      encoder.encodeLocalizedTextArray("ApplicationNames", value.getApplicationNames());
      encoder.encodeStructArray(
          "AdditionalServers", value.getAdditionalServers(), ApplicationDescription.TYPE_ID);
    }
  }
}

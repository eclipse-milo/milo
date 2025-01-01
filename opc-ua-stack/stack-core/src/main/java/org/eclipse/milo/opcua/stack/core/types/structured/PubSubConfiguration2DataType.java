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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.4</a>
 */
public class PubSubConfiguration2DataType extends PubSubConfigurationDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=23602");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23854");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23922");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23990");

  private final StandaloneSubscribedDataSetDataType @Nullable [] subscribedDataSets;

  private final DataSetMetaDataType @Nullable [] dataSetClasses;

  private final EndpointDescription @Nullable [] defaultSecurityKeyServices;

  private final SecurityGroupDataType @Nullable [] securityGroups;

  private final PubSubKeyPushTargetDataType @Nullable [] pubSubKeyPushTargets;

  private final UInteger configurationVersion;

  private final KeyValuePair @Nullable [] configurationProperties;

  public PubSubConfiguration2DataType(
      PublishedDataSetDataType @Nullable [] publishedDataSets,
      PubSubConnectionDataType @Nullable [] connections,
      Boolean enabled,
      StandaloneSubscribedDataSetDataType @Nullable [] subscribedDataSets,
      DataSetMetaDataType @Nullable [] dataSetClasses,
      EndpointDescription @Nullable [] defaultSecurityKeyServices,
      SecurityGroupDataType @Nullable [] securityGroups,
      PubSubKeyPushTargetDataType @Nullable [] pubSubKeyPushTargets,
      UInteger configurationVersion,
      KeyValuePair @Nullable [] configurationProperties) {
    super(publishedDataSets, connections, enabled);
    this.subscribedDataSets = subscribedDataSets;
    this.dataSetClasses = dataSetClasses;
    this.defaultSecurityKeyServices = defaultSecurityKeyServices;
    this.securityGroups = securityGroups;
    this.pubSubKeyPushTargets = pubSubKeyPushTargets;
    this.configurationVersion = configurationVersion;
    this.configurationProperties = configurationProperties;
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

  public StandaloneSubscribedDataSetDataType @Nullable [] getSubscribedDataSets() {
    return subscribedDataSets;
  }

  public DataSetMetaDataType @Nullable [] getDataSetClasses() {
    return dataSetClasses;
  }

  public EndpointDescription @Nullable [] getDefaultSecurityKeyServices() {
    return defaultSecurityKeyServices;
  }

  public SecurityGroupDataType @Nullable [] getSecurityGroups() {
    return securityGroups;
  }

  public PubSubKeyPushTargetDataType @Nullable [] getPubSubKeyPushTargets() {
    return pubSubKeyPushTargets;
  }

  public UInteger getConfigurationVersion() {
    return configurationVersion;
  }

  public KeyValuePair @Nullable [] getConfigurationProperties() {
    return configurationProperties;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubConfiguration2DataType that = (PubSubConfiguration2DataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getSubscribedDataSets(), that.getSubscribedDataSets());
    eqb.append(getDataSetClasses(), that.getDataSetClasses());
    eqb.append(getDefaultSecurityKeyServices(), that.getDefaultSecurityKeyServices());
    eqb.append(getSecurityGroups(), that.getSecurityGroups());
    eqb.append(getPubSubKeyPushTargets(), that.getPubSubKeyPushTargets());
    eqb.append(getConfigurationVersion(), that.getConfigurationVersion());
    eqb.append(getConfigurationProperties(), that.getConfigurationProperties());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSubscribedDataSets());
    hcb.append(getDataSetClasses());
    hcb.append(getDefaultSecurityKeyServices());
    hcb.append(getSecurityGroups());
    hcb.append(getPubSubKeyPushTargets());
    hcb.append(getConfigurationVersion());
    hcb.append(getConfigurationProperties());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PubSubConfiguration2DataType.class.getSimpleName() + "[", "]");
    joiner.add("subscribedDataSets=" + java.util.Arrays.toString(getSubscribedDataSets()));
    joiner.add("dataSetClasses=" + java.util.Arrays.toString(getDataSetClasses()));
    joiner.add(
        "defaultSecurityKeyServices=" + java.util.Arrays.toString(getDefaultSecurityKeyServices()));
    joiner.add("securityGroups=" + java.util.Arrays.toString(getSecurityGroups()));
    joiner.add("pubSubKeyPushTargets=" + java.util.Arrays.toString(getPubSubKeyPushTargets()));
    joiner.add("configurationVersion=" + getConfigurationVersion());
    joiner.add(
        "configurationProperties=" + java.util.Arrays.toString(getConfigurationProperties()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23854),
        new NodeId(0, 15530),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PublishedDataSets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15578),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Connections",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15617),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Enabled",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SubscribedDataSets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23600),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetClasses",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14523),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DefaultSecurityKeyServices",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 312),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityGroups",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23601),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PubSubKeyPushTargets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25270),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConfigurationVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConfigurationProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PubSubConfiguration2DataType> {
    @Override
    public Class<PubSubConfiguration2DataType> getType() {
      return PubSubConfiguration2DataType.class;
    }

    @Override
    public PubSubConfiguration2DataType decodeType(EncodingContext context, UaDecoder decoder) {
      PublishedDataSetDataType[] publishedDataSets =
          (PublishedDataSetDataType[])
              decoder.decodeStructArray("PublishedDataSets", PublishedDataSetDataType.TYPE_ID);
      PubSubConnectionDataType[] connections =
          (PubSubConnectionDataType[])
              decoder.decodeStructArray("Connections", PubSubConnectionDataType.TYPE_ID);
      Boolean enabled = decoder.decodeBoolean("Enabled");
      StandaloneSubscribedDataSetDataType[] subscribedDataSets =
          (StandaloneSubscribedDataSetDataType[])
              decoder.decodeStructArray(
                  "SubscribedDataSets", StandaloneSubscribedDataSetDataType.TYPE_ID);
      DataSetMetaDataType[] dataSetClasses =
          (DataSetMetaDataType[])
              decoder.decodeStructArray("DataSetClasses", DataSetMetaDataType.TYPE_ID);
      EndpointDescription[] defaultSecurityKeyServices =
          (EndpointDescription[])
              decoder.decodeStructArray("DefaultSecurityKeyServices", EndpointDescription.TYPE_ID);
      SecurityGroupDataType[] securityGroups =
          (SecurityGroupDataType[])
              decoder.decodeStructArray("SecurityGroups", SecurityGroupDataType.TYPE_ID);
      PubSubKeyPushTargetDataType[] pubSubKeyPushTargets =
          (PubSubKeyPushTargetDataType[])
              decoder.decodeStructArray(
                  "PubSubKeyPushTargets", PubSubKeyPushTargetDataType.TYPE_ID);
      UInteger configurationVersion = decoder.decodeUInt32("ConfigurationVersion");
      KeyValuePair[] configurationProperties =
          (KeyValuePair[])
              decoder.decodeStructArray("ConfigurationProperties", KeyValuePair.TYPE_ID);
      return new PubSubConfiguration2DataType(
          publishedDataSets,
          connections,
          enabled,
          subscribedDataSets,
          dataSetClasses,
          defaultSecurityKeyServices,
          securityGroups,
          pubSubKeyPushTargets,
          configurationVersion,
          configurationProperties);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PubSubConfiguration2DataType value) {
      encoder.encodeStructArray(
          "PublishedDataSets", value.getPublishedDataSets(), PublishedDataSetDataType.TYPE_ID);
      encoder.encodeStructArray(
          "Connections", value.getConnections(), PubSubConnectionDataType.TYPE_ID);
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeStructArray(
          "SubscribedDataSets",
          value.getSubscribedDataSets(),
          StandaloneSubscribedDataSetDataType.TYPE_ID);
      encoder.encodeStructArray(
          "DataSetClasses", value.getDataSetClasses(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeStructArray(
          "DefaultSecurityKeyServices",
          value.getDefaultSecurityKeyServices(),
          EndpointDescription.TYPE_ID);
      encoder.encodeStructArray(
          "SecurityGroups", value.getSecurityGroups(), SecurityGroupDataType.TYPE_ID);
      encoder.encodeStructArray(
          "PubSubKeyPushTargets",
          value.getPubSubKeyPushTargets(),
          PubSubKeyPushTargetDataType.TYPE_ID);
      encoder.encodeUInt32("ConfigurationVersion", value.getConfigurationVersion());
      encoder.encodeStructArray(
          "ConfigurationProperties", value.getConfigurationProperties(), KeyValuePair.TYPE_ID);
    }
  }
}

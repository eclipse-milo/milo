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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ManAddrIfSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.2</a>
 */
public class LldpManagementAddressTxPortType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18953");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=19079");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=19100");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=19299");

  private final UInteger addressSubtype;

  private final @Nullable String manAddress;

  private final Boolean txEnable;

  private final UInteger addrLen;

  private final ManAddrIfSubtype ifSubtype;

  private final UInteger ifId;

  public LldpManagementAddressTxPortType(
      UInteger addressSubtype,
      @Nullable String manAddress,
      Boolean txEnable,
      UInteger addrLen,
      ManAddrIfSubtype ifSubtype,
      UInteger ifId) {
    this.addressSubtype = addressSubtype;
    this.manAddress = manAddress;
    this.txEnable = txEnable;
    this.addrLen = addrLen;
    this.ifSubtype = ifSubtype;
    this.ifId = ifId;
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

  public UInteger getAddressSubtype() {
    return addressSubtype;
  }

  public @Nullable String getManAddress() {
    return manAddress;
  }

  public Boolean getTxEnable() {
    return txEnable;
  }

  public UInteger getAddrLen() {
    return addrLen;
  }

  public ManAddrIfSubtype getIfSubtype() {
    return ifSubtype;
  }

  public UInteger getIfId() {
    return ifId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    LldpManagementAddressTxPortType that = (LldpManagementAddressTxPortType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAddressSubtype(), that.getAddressSubtype());
    eqb.append(getManAddress(), that.getManAddress());
    eqb.append(getTxEnable(), that.getTxEnable());
    eqb.append(getAddrLen(), that.getAddrLen());
    eqb.append(getIfSubtype(), that.getIfSubtype());
    eqb.append(getIfId(), that.getIfId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAddressSubtype());
    hcb.append(getManAddress());
    hcb.append(getTxEnable());
    hcb.append(getAddrLen());
    hcb.append(getIfSubtype());
    hcb.append(getIfId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", LldpManagementAddressTxPortType.class.getSimpleName() + "[", "]");
    joiner.add("addressSubtype=" + getAddressSubtype());
    joiner.add("manAddress='" + getManAddress() + "'");
    joiner.add("txEnable=" + getTxEnable());
    joiner.add("addrLen=" + getAddrLen());
    joiner.add("ifSubtype=" + getIfSubtype());
    joiner.add("ifId=" + getIfId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19079),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "AddressSubtype",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ManAddress",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TxEnable",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AddrLen",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IfSubtype",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18951),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IfId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<LldpManagementAddressTxPortType> {
    @Override
    public Class<LldpManagementAddressTxPortType> getType() {
      return LldpManagementAddressTxPortType.class;
    }

    @Override
    public LldpManagementAddressTxPortType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger addressSubtype;
      final String manAddress;
      final Boolean txEnable;
      final UInteger addrLen;
      final ManAddrIfSubtype ifSubtype;
      final UInteger ifId;
      addressSubtype = decoder.decodeUInt32("AddressSubtype");
      manAddress = decoder.decodeString("ManAddress");
      txEnable = decoder.decodeBoolean("TxEnable");
      addrLen = decoder.decodeUInt32("AddrLen");
      ifSubtype = ManAddrIfSubtype.from(decoder.decodeEnum("IfSubtype"));
      ifId = decoder.decodeUInt32("IfId");
      return new LldpManagementAddressTxPortType(
          addressSubtype, manAddress, txEnable, addrLen, ifSubtype, ifId);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, LldpManagementAddressTxPortType value) {
      encoder.encodeUInt32("AddressSubtype", value.getAddressSubtype());
      encoder.encodeString("ManAddress", value.getManAddress());
      encoder.encodeBoolean("TxEnable", value.getTxEnable());
      encoder.encodeUInt32("AddrLen", value.getAddrLen());
      encoder.encodeEnum("IfSubtype", value.getIfSubtype());
      encoder.encodeUInt32("IfId", value.getIfId());
    }
  }
}

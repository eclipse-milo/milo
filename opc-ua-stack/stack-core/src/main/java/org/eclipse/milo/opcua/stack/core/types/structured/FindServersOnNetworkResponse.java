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
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.4.3/#5.4.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.4.3/#5.4.3.2</a>
 */
public class FindServersOnNetworkResponse extends Structure implements UaResponseMessageType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=12191");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12209");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12197");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15097");

    private final ResponseHeader responseHeader;

    private final DateTime lastCounterResetTime;

    private final ServerOnNetwork @Nullable [] servers;

    public FindServersOnNetworkResponse(ResponseHeader responseHeader, DateTime lastCounterResetTime,
                                        ServerOnNetwork @Nullable [] servers) {
        this.responseHeader = responseHeader;
        this.lastCounterResetTime = lastCounterResetTime;
        this.servers = servers;
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

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public DateTime getLastCounterResetTime() {
        return lastCounterResetTime;
    }

    public ServerOnNetwork @Nullable [] getServers() {
        return servers;
    }

    @Override
    public int hashCode() {
        var hcb = new HashCodeBuilder();
        hcb.append(getResponseHeader());
        hcb.append(getLastCounterResetTime());
        hcb.append(getServers());
        return hcb.build();
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", FindServersOnNetworkResponse.class.getSimpleName() + "[", "]");
        joiner.add("responseHeader=" + getResponseHeader());
        joiner.add("lastCounterResetTime=" + getLastCounterResetTime());
        joiner.add("servers=" + java.util.Arrays.toString(getServers()));
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 12209),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("ResponseHeader", LocalizedText.NULL_VALUE, new NodeId(0, 392), -1, null, UInteger.valueOf(0), false),
                new StructureField("LastCounterResetTime", LocalizedText.NULL_VALUE, new NodeId(0, 294), -1, null, UInteger.valueOf(0), false),
                new StructureField("Servers", LocalizedText.NULL_VALUE, new NodeId(0, 12189), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<FindServersOnNetworkResponse> {
        @Override
        public Class<FindServersOnNetworkResponse> getType() {
            return FindServersOnNetworkResponse.class;
        }

        @Override
        public FindServersOnNetworkResponse decodeType(EncodingContext context, UaDecoder decoder) {
            ResponseHeader responseHeader = (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
            DateTime lastCounterResetTime = decoder.decodeDateTime("LastCounterResetTime");
            ServerOnNetwork[] servers = (ServerOnNetwork[]) decoder.decodeStructArray("Servers", ServerOnNetwork.TYPE_ID);
            return new FindServersOnNetworkResponse(responseHeader, lastCounterResetTime, servers);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder,
                               FindServersOnNetworkResponse value) {
            encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
            encoder.encodeDateTime("LastCounterResetTime", value.getLastCounterResetTime());
            encoder.encodeStructArray("Servers", value.getServers(), ServerOnNetwork.TYPE_ID);
        }
    }
}

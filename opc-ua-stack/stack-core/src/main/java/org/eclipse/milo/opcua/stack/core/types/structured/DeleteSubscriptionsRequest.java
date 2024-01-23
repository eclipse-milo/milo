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

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.util.StringJoiner;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.8/#5.13.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.8/#5.13.8.2</a>
 */
public class DeleteSubscriptionsRequest extends Structure implements UaRequestMessageType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=845");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=847");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=846");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15359");

    private final RequestHeader requestHeader;

    private final UInteger @Nullable [] subscriptionIds;

    public DeleteSubscriptionsRequest(RequestHeader requestHeader,
                                      UInteger @Nullable [] subscriptionIds) {
        this.requestHeader = requestHeader;
        this.subscriptionIds = subscriptionIds;
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

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public UInteger @Nullable [] getSubscriptionIds() {
        return subscriptionIds;
    }

    @Override
    public int hashCode() {
        var hcb = new HashCodeBuilder();
        hcb.append(getRequestHeader());
        hcb.append(getSubscriptionIds());
        return hcb.build();
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", DeleteSubscriptionsRequest.class.getSimpleName() + "[", "]");
        joiner.add("requestHeader=" + getRequestHeader());
        joiner.add("subscriptionIds=" + java.util.Arrays.toString(getSubscriptionIds()));
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 847),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("RequestHeader", LocalizedText.NULL_VALUE, new NodeId(0, 389), -1, null, UInteger.valueOf(0), false),
                new StructureField("SubscriptionIds", LocalizedText.NULL_VALUE, new NodeId(0, 288), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<DeleteSubscriptionsRequest> {
        @Override
        public Class<DeleteSubscriptionsRequest> getType() {
            return DeleteSubscriptionsRequest.class;
        }

        @Override
        public DeleteSubscriptionsRequest decodeType(EncodingContext context, UaDecoder decoder) {
            RequestHeader requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
            UInteger[] subscriptionIds = decoder.decodeUInt32Array("SubscriptionIds");
            return new DeleteSubscriptionsRequest(requestHeader, subscriptionIds);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder,
                               DeleteSubscriptionsRequest value) {
            encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
            encoder.encodeUInt32Array("SubscriptionIds", value.getSubscriptionIds());
        }
    }
}

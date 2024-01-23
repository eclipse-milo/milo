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

import lombok.EqualsAndHashCode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.8.5/#6.8.5.1">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.8.5/#6.8.5.1</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
public class DeleteRawModifiedDetails extends HistoryUpdateDetails implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=686");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=688");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=687");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15283");

    private final Boolean isDeleteModified;

    private final DateTime startTime;

    private final DateTime endTime;

    public DeleteRawModifiedDetails(NodeId nodeId, Boolean isDeleteModified, DateTime startTime,
                                    DateTime endTime) {
        super(nodeId);
        this.isDeleteModified = isDeleteModified;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Boolean getIsDeleteModified() {
        return isDeleteModified;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", DeleteRawModifiedDetails.class.getSimpleName() + "[", "]");
        joiner.add("isDeleteModified=" + getIsDeleteModified());
        joiner.add("startTime=" + getStartTime());
        joiner.add("endTime=" + getEndTime());
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 688),
            new NodeId(0, 677),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("NodeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("IsDeleteModified", LocalizedText.NULL_VALUE, new NodeId(0, 1), -1, null, UInteger.valueOf(0), false),
                new StructureField("StartTime", LocalizedText.NULL_VALUE, new NodeId(0, 294), -1, null, UInteger.valueOf(0), false),
                new StructureField("EndTime", LocalizedText.NULL_VALUE, new NodeId(0, 294), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<DeleteRawModifiedDetails> {
        @Override
        public Class<DeleteRawModifiedDetails> getType() {
            return DeleteRawModifiedDetails.class;
        }

        @Override
        public DeleteRawModifiedDetails decodeType(EncodingContext context, UaDecoder decoder) {
            NodeId nodeId = decoder.decodeNodeId("NodeId");
            Boolean isDeleteModified = decoder.decodeBoolean("IsDeleteModified");
            DateTime startTime = decoder.decodeDateTime("StartTime");
            DateTime endTime = decoder.decodeDateTime("EndTime");
            return new DeleteRawModifiedDetails(nodeId, isDeleteModified, startTime, endTime);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder,
                               DeleteRawModifiedDetails value) {
            encoder.encodeNodeId("NodeId", value.getNodeId());
            encoder.encodeBoolean("IsDeleteModified", value.getIsDeleteModified());
            encoder.encodeDateTime("StartTime", value.getStartTime());
            encoder.encodeDateTime("EndTime", value.getEndTime());
        }
    }
}

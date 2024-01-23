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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.jetbrains.annotations.Nullable;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3</a>
 */
@EqualsAndHashCode(
    callSuper = true
)
public class EventFilter extends MonitoringFilter implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=725");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=727");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=726");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15295");

    private final SimpleAttributeOperand @Nullable [] selectClauses;

    private final ContentFilter whereClause;

    public EventFilter(SimpleAttributeOperand @Nullable [] selectClauses, ContentFilter whereClause) {
        this.selectClauses = selectClauses;
        this.whereClause = whereClause;
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

    public SimpleAttributeOperand @Nullable [] getSelectClauses() {
        return selectClauses;
    }

    public ContentFilter getWhereClause() {
        return whereClause;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", EventFilter.class.getSimpleName() + "[", "]");
        joiner.add("selectClauses=" + java.util.Arrays.toString(getSelectClauses()));
        joiner.add("whereClause=" + getWhereClause());
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 727),
            new NodeId(0, 719),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("SelectClauses", LocalizedText.NULL_VALUE, new NodeId(0, 601), 1, null, UInteger.valueOf(0), false),
                new StructureField("WhereClause", LocalizedText.NULL_VALUE, new NodeId(0, 586), -1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<EventFilter> {
        @Override
        public Class<EventFilter> getType() {
            return EventFilter.class;
        }

        @Override
        public EventFilter decodeType(EncodingContext context, UaDecoder decoder) {
            SimpleAttributeOperand[] selectClauses = (SimpleAttributeOperand[]) decoder.decodeStructArray("SelectClauses", SimpleAttributeOperand.TYPE_ID);
            ContentFilter whereClause = (ContentFilter) decoder.decodeStruct("WhereClause", ContentFilter.TYPE_ID);
            return new EventFilter(selectClauses, whereClause);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder, EventFilter value) {
            encoder.encodeStructArray("SelectClauses", value.getSelectClauses(), SimpleAttributeOperand.TYPE_ID);
            encoder.encodeStruct("WhereClause", value.getWhereClause(), ContentFilter.TYPE_ID);
        }
    }
}

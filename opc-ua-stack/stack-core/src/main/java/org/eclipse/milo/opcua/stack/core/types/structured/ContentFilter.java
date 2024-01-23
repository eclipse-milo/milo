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
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.4</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
public class ContentFilter extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=586");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=588");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=587");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15205");

    private final ContentFilterElement @Nullable [] elements;

    public ContentFilter(ContentFilterElement @Nullable [] elements) {
        this.elements = elements;
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

    public ContentFilterElement @Nullable [] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", ContentFilter.class.getSimpleName() + "[", "]");
        joiner.add("elements=" + java.util.Arrays.toString(getElements()));
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 588),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("Elements", LocalizedText.NULL_VALUE, new NodeId(0, 583), 1, null, UInteger.valueOf(0), false)
            }
        );
    }

    public static final class Codec extends GenericDataTypeCodec<ContentFilter> {
        @Override
        public Class<ContentFilter> getType() {
            return ContentFilter.class;
        }

        @Override
        public ContentFilter decodeType(EncodingContext context, UaDecoder decoder) {
            ContentFilterElement[] elements = (ContentFilterElement[]) decoder.decodeStructArray("Elements", ContentFilterElement.TYPE_ID);
            return new ContentFilter(elements);
        }

        @Override
        public void encodeType(EncodingContext context, UaEncoder encoder, ContentFilter value) {
            encoder.encodeStructArray("Elements", value.getElements(), ContentFilterElement.TYPE_ID);
        }
    }
}

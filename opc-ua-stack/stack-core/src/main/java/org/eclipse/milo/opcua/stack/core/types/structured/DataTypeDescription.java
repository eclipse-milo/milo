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
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.32">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.32</a>
 */
@EqualsAndHashCode(
    callSuper = false
)
public abstract class DataTypeDescription extends Structure implements UaStructuredType {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=14525");

    public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=125");

    public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=14796");

    public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15057");

    private final NodeId dataTypeId;

    private final QualifiedName name;

    public DataTypeDescription(NodeId dataTypeId, QualifiedName name) {
        this.dataTypeId = dataTypeId;
        this.name = name;
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

    public NodeId getDataTypeId() {
        return dataTypeId;
    }

    public QualifiedName getName() {
        return name;
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(", ", DataTypeDescription.class.getSimpleName() + "[", "]");
        joiner.add("dataTypeId=" + getDataTypeId());
        joiner.add("name=" + getName());
        return joiner.toString();
    }

    public static StructureDefinition definition(NamespaceTable namespaceTable) {
        return new StructureDefinition(
            new NodeId(0, 125),
            new NodeId(0, 22),
            StructureType.Structure,
            new StructureField[]{
                new StructureField("DataTypeId", LocalizedText.NULL_VALUE, new NodeId(0, 17), -1, null, UInteger.valueOf(0), false),
                new StructureField("Name", LocalizedText.NULL_VALUE, new NodeId(0, 20), -1, null, UInteger.valueOf(0), false)
            }
        );
    }
}

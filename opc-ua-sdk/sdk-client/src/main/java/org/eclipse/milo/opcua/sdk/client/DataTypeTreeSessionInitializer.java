/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.session.SessionFsm;
import org.eclipse.milo.opcua.sdk.core.types.DataType;
import org.eclipse.milo.opcua.sdk.core.types.DataTypeTree;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructCodec;
import org.eclipse.milo.opcua.stack.client.UaStackClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.slf4j.LoggerFactory;

/**
 * Builds a {@link DataTypeTree} and stores it on an {@link OpcUaSession} as an attribute under
 * the key {@link DataTypeTreeSessionInitializer#SESSION_ATTRIBUTE_KEY}.
 * <p>
 * Optionally, and by default, the tree will be traversed and dynamic DataTypeCodecs registered
 * with the client's dynamic DataTypeManager for each structured and enumerated type.
 */
public class DataTypeTreeSessionInitializer implements SessionFsm.SessionInitializer {

    /**
     * The attribute key that the {@link DataTypeTree} will be stored under in the {@link OpcUaSession}.
     *
     * @see OpcUaSession#getAttribute(String)
     * @see OpcUaSession#setAttribute(String, Object)
     */
    public static final String SESSION_ATTRIBUTE_KEY = "dataTypeTree";

    private final boolean registerCodecs;

    /**
     * Create a {@link DataTypeTreeSessionInitializer} that registers dynamic codecs once the tree
     * is built.
     */
    public DataTypeTreeSessionInitializer() {
        this(true);
    }

    /**
     * Create a {@link DataTypeTreeSessionInitializer} that optionally registers dynamic codecs
     * once the tree is built.
     *
     * @param registerCodecs {@code true} if the tree should be traversed and dynamic codecs
     *                       registered for all structured and enumerated types.
     */
    public DataTypeTreeSessionInitializer(boolean registerCodecs) {
        this.registerCodecs = registerCodecs;
    }

    @Override
    public CompletableFuture<Unit> initialize(UaStackClient stackClient, OpcUaSession session) {
        return DataTypeTreeBuilder.buildAsync(stackClient, session)
            .thenAccept(tree -> {
                session.setAttribute(SESSION_ATTRIBUTE_KEY, tree);

                if (registerCodecs) {
//                    registerCodecs(stackClient, tree);
                }
            })
            .thenApply(v -> Unit.VALUE);
    }

    private static void registerCodecs(UaStackClient stackClient, DataTypeTree dataTypeTree) {
        Tree<DataType> structureNode = dataTypeTree.getTreeNode(NodeIds.Structure);
        if (structureNode != null) {
            structureNode.traverse(dataType -> {
                DataTypeDefinition definition = dataType.getDataTypeDefinition();

                if (definition instanceof StructureDefinition) {
                    stackClient.getDynamicDataTypeManager().registerType(
                        dataType.getNodeId(),
                        new DynamicStructCodec(dataTypeTree, dataType),
                        dataType.getBinaryEncodingId(),
                        dataType.getXmlEncodingId(),
                        dataType.getJsonEncodingId()
                    );
                }
            });
        } else {
            LoggerFactory.getLogger(DataTypeTreeSessionInitializer.class)
                .warn("Tree for NodeIds.Structure not found; is the server DataType hierarchy sane?");
        }

//        Tree<DataType> enumerationNode = dataTypeTree.getTreeNode(NodeIds.Enumeration);
//        if (enumerationNode != null) {
//            enumerationNode.traverse(dataType -> {
//                DataTypeDefinition definition = dataType.getDataTypeDefinition();
//
//                if (definition instanceof EnumDefinition) {
//                    stackClient.getDynamicDataTypeManager().registerEnumType(
//                        dataType.getNodeId(),
//                        new DynamicEnumCodec(dataType)
//                    );
//                }
//            });
//        } else {
//            LoggerFactory.getLogger(DataTypeTreeSessionInitializer.class)
//                .warn("Tree for NodeIds.Enumeration not found; is the server DataType hierarchy sane?");
//        }
    }

}

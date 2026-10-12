/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

/**
 * Configuration for an application-defined alias category.
 *
 * <p>The application controls where the category and its alias Nodes live and how their NodeIds are
 * allocated; the framework materializes the category Node, its Method instances, and the parent
 * linkage.
 *
 * @param categoryNodeId the NodeId of the category Node itself.
 * @param parentCategoryId the NodeId of the category this category is organized by, e.g. {@code
 *     NodeIds.TagVariables} or another application category.
 * @param browseName the BrowseName of the category Node. The name text is also used as the
 *     category's DisplayName.
 * @param nodeManager the {@link NodeManager} the category Node, its Method Nodes, and the alias
 *     Nodes added to it are created in.
 * @param aliasNodeIdFactory allocates the NodeId for an alias Node from its alias name. Called
 *     exactly once per alias creation — never for the category itself — so stateful factories are
 *     safe; must yield NodeIds that are unique within {@code nodeManager} and distinct from {@code
 *     categoryNodeId}.
 * @param lastChangeEnabled whether the category gets a {@code LastChange} Property maintained by
 *     the manager. The Property is Optional per category; the root {@code Aliases} value is
 *     maintained regardless.
 * @param findAliasVerboseEnabled whether a {@code FindAliasVerbose} Method instance is materialized
 *     and bound on the category.
 * @param configurationEnabled whether the {@code AddAliasesToCategory} and {@code
 *     DeleteAliasesFromCategory} Methods are materialized and bound on the category. The Methods
 *     are network-callable only for sessions the {@link AliasAuthorizationPolicy} grants mutation
 *     to; the default policy denies every session, so enabling network mutation requires this flag
 *     <em>and</em> an explicit policy grant.
 */
public record AliasCategoryConfig(
    NodeId categoryNodeId,
    NodeId parentCategoryId,
    QualifiedName browseName,
    NodeManager<UaNode> nodeManager,
    Function<String, NodeId> aliasNodeIdFactory,
    boolean lastChangeEnabled,
    boolean findAliasVerboseEnabled,
    boolean configurationEnabled) {}

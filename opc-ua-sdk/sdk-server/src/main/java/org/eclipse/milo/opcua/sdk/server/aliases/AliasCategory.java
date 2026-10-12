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

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

/**
 * An immutable handle describing an alias category under management.
 *
 * <p>Returned when a category is added or adopted; use {@link #nodeId()} with the manager's
 * programmatic API to add, delete, or look up aliases in the category.
 *
 * @param nodeId the NodeId of the category Node.
 * @param browseName the BrowseName of the category Node.
 * @param lastChangeEnabled whether the manager maintains a {@code LastChange} Property on this
 *     category.
 * @param findAliasVerboseEnabled whether a {@code FindAliasVerbose} Method instance is bound on
 *     this category.
 * @param configurationEnabled whether the {@code AddAliasesToCategory} and {@code
 *     DeleteAliasesFromCategory} Method instances are bound on this category.
 */
public record AliasCategory(
    NodeId nodeId,
    QualifiedName browseName,
    boolean lastChangeEnabled,
    boolean findAliasVerboseEnabled,
    boolean configurationEnabled) {}

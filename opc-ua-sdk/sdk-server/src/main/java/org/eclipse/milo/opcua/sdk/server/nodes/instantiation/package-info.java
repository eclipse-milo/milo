/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Type-model-driven node instantiation: compiles a TypeDefinition's fully-inherited
 * InstanceDeclarationHierarchy (OPC UA Part 3 §6.3.3) into an immutable, cacheable {@link
 * org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeInstantiationModel}.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeModelCompiler} reads live
 * address-space state and either produces a validated model or fails with a {@link
 * org.eclipse.milo.opcua.sdk.server.nodes.instantiation.ModelCompilationException} carrying
 * structured {@link org.eclipse.milo.opcua.sdk.server.nodes.instantiation.ModelDiagnostic}s — never
 * a silently truncated hierarchy. Models are structural snapshots keyed by {@link
 * org.eclipse.milo.opcua.sdk.server.nodes.instantiation.BrowsePath}, with every ModellingRule
 * classified and provenance preserved for diagnostics.
 *
 * <p><strong>Experimental:</strong> this package is new API, subject to adjustment for one minor
 * release based on experience from Milo's in-tree migrations and validation of the placeholder
 * surface against real companion-specification workloads, after which it freezes. The legacy {@code
 * org.eclipse.milo.opcua.sdk.server.nodes.factories} subsystem remains available, with unchanged
 * behavior, for the whole deprecation period; see {@code
 * docs/features/node-instantiation-migration.md} for the legacy-to-new mapping.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import org.jspecify.annotations.NullMarked;

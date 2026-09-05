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
 * Opt-in server-side support for OPC UA Part 17 Alias Names.
 *
 * <p>Alias names are the portable-configuration mechanism defined by OPC 10000-17: applications
 * publish stable, human-meaningful names ({@code AliasNameType} Objects organized into {@code
 * AliasNameCategoryType} folders) that resolve to one or more target Nodes through {@code AliasFor}
 * References. Clients discover targets by calling the {@code FindAlias} and {@code
 * FindAliasVerbose} Methods with a Part 4 {@code Like} wildcard pattern.
 *
 * <p>Milo's standard namespace already loads the complete Part 17 model surface — the {@code
 * Aliases}, {@code TagVariables}, and {@code Topics} Objects and their {@code FindAlias} Method
 * instances — but gives them no behavior. This package supplies the behavior, on an opt-in basis:
 * nothing here runs unless an application constructs and starts an alias manager.
 *
 * <h2>Ownership split</h2>
 *
 * <p>The framework owns the reusable Part 17 mechanics: Method handler binding, wildcard lookup
 * with recursive category traversal, ReferenceType subtype filtering, deterministic result
 * ordering, mutation validation, and {@code LastChange} version maintenance. The application owns
 * everything vocabulary-shaped: which aliases and categories exist, the NodeIds and NodeManager
 * they live in ({@link org.eclipse.milo.opcua.sdk.server.aliases.AliasCategoryConfig}), how version
 * state is persisted ({@link org.eclipse.milo.opcua.sdk.server.aliases.AliasVersionStore}), and who
 * may search or mutate over the network ({@link
 * org.eclipse.milo.opcua.sdk.server.aliases.AliasAuthorizationPolicy}). Every SPI has a usable
 * default, but the defaults are deliberately conservative: the in-memory version store does not
 * survive restart, and the default policy denies all network mutation.
 *
 * <h2>Lifecycle</h2>
 *
 * <p>The alias manager is an application-constructed lifecycle component, created with the {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer} and an {@link
 * org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig}, and started after server startup.
 * On startup it registers its own AddressSpace fragment, binds handlers onto the standard {@code
 * FindAlias} Method Nodes (restoring their executable flags), optionally materializes the Optional
 * Method instances, and loads persisted {@code LastChange} values — a failed startup (handler
 * conflict, NodeId collision, unreadable version store) rolls back anything already applied and
 * leaves no trace. On shutdown it unbinds its handlers, deletes the Method Nodes it materialized,
 * and unregisters its fragment: Nodes the manager hosts there (materialized Methods, alias Nodes
 * created in standard or adopted categories) leave the AddressSpace with it, while category and
 * alias Nodes hosted in application NodeManagers remain in place. Applications register their own
 * categories and aliases through the manager's programmatic API after startup. Removing a
 * manager-created category detaches its surviving aliases and child categories before deleting the
 * category itself.
 *
 * <h2>Data flow</h2>
 *
 * <p>The AddressSpace is the single source of truth; there is no shadow index. Lookup walks forward
 * {@code Organizes} References from the called category at call time, recursing into {@code
 * AliasNameCategoryType} instances and collecting {@code AliasNameType} instances, so aliases
 * loaded from a NodeSet file or created by other components are found without registration. The
 * trade-off is weak consistency under concurrent mutation — the same weak consistency Browse
 * already has — and that out-of-band AddressSpace edits do not bump {@code LastChange}; mutations
 * must flow through the manager (or be followed by an explicit {@code touch}) for version
 * correctness. Association removal covers every registered NodeManager that may store either
 * direction, matching the scope of reference collection. Whole-alias deletion preserves the owning
 * manager's child-node traversal and removes the alias's collected References from all registered
 * managers, so NodeId reuse cannot restore a deleted target or category membership. Out-of-band
 * edits carry one further caveat: References recorded in only one direction (e.g. a
 * category-side-only {@code Organizes} Reference loaded from a NodeSet without its inverse) are
 * still found by search, which follows the forward direction, but are invisible to alias-side
 * operations — organizing-category resolution during delete and ancestor discovery during version
 * propagation both walk inverse References and miss such linkage.
 *
 * <h2>Network mutation</h2>
 *
 * <p>The {@code AddAliasesToCategory} and {@code DeleteAliasesFromCategory} Methods are
 * deny-by-default at two independent layers: the Method instances are only materialized (or, on
 * adopted categories, bound) when configuration is enabled, and even then the {@link
 * org.eclipse.milo.opcua.sdk.server.aliases.AliasAuthorizationPolicy} — whose default denies every
 * session — must grant mutation per call. An authorized call is validated at two levels, per Part
 * 17 §6.3.4/§6.3.5: call-level failures (null or non-parallel arrays, an invalid {@code
 * TargetReferenceType}, operation counts over the configured limit) fail the whole call before any
 * entry is processed, while everything else is reported through the per-entry {@code ErrorCodes}
 * output. Entries are independent — a failed entry leaves its own state untouched and does not
 * affect the others — and duplicate additions, including duplicates within one request, succeed
 * without changing anything. All entries of a call apply under the manager's write lock, and {@code
 * LastChange} is bumped once per affected category when the call completes. The programmatic API
 * ({@code addAlias} / {@code deleteAlias}) shares the same validation and apply logic but is
 * trusted application code: it bypasses the policy and fails fast on the first error.
 *
 * <h2>The LastChange / VersionTime invariant</h2>
 *
 * <p>Part 17 §6.3.1 requires the root {@code Aliases} Object's {@code LastChange} Property to be
 * monotonic and persisted across restart. Every mutation bumps the version of each affected
 * category and of every ancestor category up to and including the root, computing {@code next =
 * max(secondsSince2000(now), previous + 1)} so that clock rollback never produces a regression. New
 * values are persisted through the {@link
 * org.eclipse.milo.opcua.sdk.server.aliases.AliasVersionStore} <em>before</em> the mutation they
 * describe is applied or the value is published: a failed save aborts the operation (or fails the
 * entry) with {@code Bad_InternalError} and nothing changed, so no Client can ever observe an
 * unpersisted {@code LastChange} value — after a restart the sequence therefore never repeats an
 * observed value for different content, which would leave Client caches undetectably stale.
 * Likewise a failed load at startup fails startup, because silently reset versions would violate
 * the persistence contract undetectably. Categories whose {@code LastChange} Property does not
 * exist (it is Optional per category) still participate in propagation; only the Property write is
 * skipped.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.aliases;

import org.jspecify.annotations.NullMarked;

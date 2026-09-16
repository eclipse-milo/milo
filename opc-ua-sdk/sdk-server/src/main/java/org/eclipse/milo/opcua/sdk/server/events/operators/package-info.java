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
 * Server-side implementations of OPC UA {@code ContentFilter} operators used while evaluating event
 * monitored item {@code whereClause} expressions.
 *
 * <p>The operators in this package are intentionally small and are wired through {@link Operators}.
 * Conversion, ordering, array, and bitwise details that are shared across operators live in package
 * helpers so the implementation follows the same OPC UA conversion and NULL semantics in every
 * operator.
 *
 * @see <a href="https://reference.opcfoundation.org/specs/OPC-10000-4/7.7.3">OPC UA Part 4, 7.7.3
 *     FilterOperator</a>
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.events.operators;

import org.jspecify.annotations.NullMarked;

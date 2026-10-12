/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import org.eclipse.milo.opcua.sdk.core.Reference;

/**
 * One reference row an apply committed into the target, including the inverse rows added alongside
 * each planned reference. Rows that were already logically present in the target were skipped by
 * the commit's deduplication and report {@code added == false}; {@link
 * InstantiationResult#deleteCreated()} removes only the added rows, so a pre-existing occurrence is
 * never removed by cleanup.
 *
 * @param reference the committed reference row.
 * @param added {@code true} if the commit physically added the row (it is part of the journal);
 *     {@code false} if an equal row was already present.
 */
public record MaterializedReference(Reference reference, boolean added) {}

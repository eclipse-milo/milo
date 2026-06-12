/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

/**
 * The source address of a published dataset field: either a Node in an OPC UA address space ({@link
 * NodeFieldAddress}) or an application-defined key ({@link KeyFieldAddress}) for standalone use
 * without any address space.
 *
 * <p>This interface is sealed, but new permitted subtypes may be added in future versions. Adding a
 * permit is source-compatible only for callers that include a {@code default} branch when switching
 * over instances of this type; always include one.
 */
public sealed interface FieldAddress permits NodeFieldAddress, KeyFieldAddress {}

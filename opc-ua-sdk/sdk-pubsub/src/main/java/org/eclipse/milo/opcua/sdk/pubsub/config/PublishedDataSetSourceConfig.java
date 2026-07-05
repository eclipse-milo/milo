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
 * Specifies where a {@link PublishedDataSetConfig} sources its values: from an ordered list of
 * field definitions supplied with snapshots at runtime ({@link PublishedDataItemsConfig}) or from
 * events emitted by an event notifier Node ({@link PublishedEventsConfig}).
 *
 * <p>This interface is sealed, but new permitted subtypes may be added in future versions. Adding a
 * permit is source-compatible only for callers that include a {@code default} branch when switching
 * over instances of this type; always include one.
 */
public sealed interface PublishedDataSetSourceConfig
    permits PublishedDataItemsConfig, PublishedEventsConfig {}

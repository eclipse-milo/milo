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
 * Immutable PubSub configuration model: {@code PubSubConfig} and the builder-built config types for
 * connections, groups, writers, readers, published and subscribed datasets, security groups, and
 * message settings, along with by-name refs, validation ({@code PubSubConfigValidationException}),
 * and the round-trip mapping boundary to the Part 14 {@code PubSubConfiguration2DataType}.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.config;

import org.jspecify.annotations.NullMarked;

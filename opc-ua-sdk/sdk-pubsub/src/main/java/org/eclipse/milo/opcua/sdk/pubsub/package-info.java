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
 * Runtime API for OPC UA Part 14 PubSub: the {@code PubSubService} entry point and its service and
 * binding configuration, opaque component handles, data-flow types for publishing ({@code
 * PublishedDataSetSource}, {@code DataSetSnapshot}) and subscribing ({@code DataSetListener},
 * {@code DataSetReceivedEvent}), plus state, metadata, and diagnostics listeners and their events.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub;

import org.jspecify.annotations.NullMarked;

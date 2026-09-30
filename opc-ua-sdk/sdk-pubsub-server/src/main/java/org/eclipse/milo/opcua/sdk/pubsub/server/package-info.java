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
 * Server integration for OPC UA Part 14 PubSub: {@code ServerPubSub} attaches a standalone {@code
 * PubSubService} runtime to an {@code OpcUaServer}, automatically binding address-space-backed
 * sources for published datasets addressed by {@code NodeFieldAddress}, automatically writing
 * received dataset fields to address-space variables for DataSetReaders configured with {@code
 * TargetVariablesConfig}, completing the DataTypeSchemaHeader of published datasets from the
 * server's DataType hierarchy ({@code DataTypeSchemaHeaders}), and optionally persisting
 * configuration via a {@code PubSubConfigurationStore} and exposing the read-only PublishSubscribe
 * information model.
 *
 * <p>The configuration the runtime, the information model, and the store observe is the effective
 * configuration: the attach-time (or store-loaded) configuration after server-side completion.
 * Every later apply through {@code ServerPubSub.runtime()} passes through the same completion in
 * {@code ManagedPubSubService} before validation, so all surfaces stay in agreement.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.server;

import org.jspecify.annotations.NullMarked;

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
 * A by-name reference to a {@link StandaloneSubscribedDataSetConfig} in the same {@link
 * PubSubConfig}.
 *
 * <p>References are resolved when {@code PubSubConfig.Builder#build()} validates the configuration;
 * an unresolvable reference fails validation.
 *
 * @param name the name of the referenced standalone subscribed dataset.
 */
public record StandaloneSubscribedDataSetRef(String name) implements SubscribedDataSetSpec {}

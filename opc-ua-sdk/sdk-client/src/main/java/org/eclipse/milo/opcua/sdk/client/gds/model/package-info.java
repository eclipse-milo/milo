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
 * Client type registration for the OPC UA Global Discovery Server information model.
 *
 * <p>The initializers register the generated classes in {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.objects} after the client knows the GDS namespace
 * index. Applications normally enter through {@code GdsClient.create} in the separate {@code
 * milo-sdk-client-gds} module. These model classes belong to {@code milo-sdk-client}; the higher
 * level module owns the parent {@code org.eclipse.milo.opcua.sdk.client.gds} package.
 *
 * <p>Generated initializers follow the package mapping in {@code docs/features/gds-client.md}.
 */
package org.eclipse.milo.opcua.sdk.client.gds.model;

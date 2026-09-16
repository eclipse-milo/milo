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
 * Discovers server type metadata and makes it available to client codecs and address-space APIs.
 *
 * <p>Type-tree factories select eager discovery or lazy resolution. Builders read definitions and
 * browse inheritance and encoding references within a session, honoring operation limits and
 * following continuation points until the server returns a null or empty token. Lazy trees resolve
 * types on demand and discard stale work when their session or cache generation changes.
 *
 * <p>DataType managers register codecs from the resolved metadata. A structure's DefaultEncodingId
 * supplies its binary encoding ID when the server omits HasEncoding references. The effective ID
 * belongs to the metadata as well as the registration, allowing decoded dynamic values to be
 * encoded again. Definitions and codec registrations must describe the same type-tree generation.
 */
package org.eclipse.milo.opcua.sdk.client.typetree;

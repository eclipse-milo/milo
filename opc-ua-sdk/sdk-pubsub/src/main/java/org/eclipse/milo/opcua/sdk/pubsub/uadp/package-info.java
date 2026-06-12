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
 * UADP message mapping (OPC UA Part 14 §7.2.2): the UADP NetworkMessage and DataSetMessage model,
 * binary encoder and decoder, and the built-in {@code "uadp"} {@code MessageMappingProvider}
 * implementation together with the encode/decode context types it consumes. Also home to the UADP
 * discovery model (Part 14 §7.2.4.6) — DataSetMetaData probes and announcements — and its codec
 * entry points, which are UADP-internal and not part of the {@code MessageMappingProvider} SPI.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import org.jspecify.annotations.NullMarked;

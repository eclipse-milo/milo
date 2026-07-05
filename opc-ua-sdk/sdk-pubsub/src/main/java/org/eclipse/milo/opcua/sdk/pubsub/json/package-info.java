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
 * JSON message mapping (OPC UA Part 14 §7.2.5): encoder and decoder for {@code ua-data}, {@code
 * ua-metadata}, and {@code ua-status} JSON NetworkMessages, the DataSet field codec implementing
 * the PubSub VerboseEncoding collapse rules (§7.2.5.4.2/.3), and the built-in {@code "json"} {@code
 * MessageMappingProvider} implementation.
 *
 * <p>Field values are encoded with the OPC UA JSON Data Encoding (OPC 10000-6 §5.4) via {@code
 * milo-encoding-json}; NetworkMessage, DataSetMessage, metadata, and status framing is implemented
 * here with Gson streaming. Decoded status messages feed the subscriber-side remote publisher
 * status event path rather than the DataSet delivery path.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.json;

import org.jspecify.annotations.NullMarked;

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
 * Represents dynamic OPC UA structures as JSON objects while preserving their declared wire types.
 *
 * <p>A DataType tree supplies field definitions and inheritance information. Codecs map JSON
 * members to those fields and use the encoding context for nested structured values. The JSON
 * object holds application data; the definition determines field order, optional-field mask
 * positions, and union selection when serializing through an OPC UA encoder.
 *
 * <p>Absent optional members remain absent after decoding and re-encoding. Their omission does not
 * move the mask bits assigned to later fields.
 */
package org.eclipse.milo.sdk.core.types.json;

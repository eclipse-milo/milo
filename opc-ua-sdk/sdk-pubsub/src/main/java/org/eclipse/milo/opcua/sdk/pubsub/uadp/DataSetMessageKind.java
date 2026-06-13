/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

/**
 * The kind of a DataSetMessage, used on both sides of the codec: decode surfaces it on {@link
 * DecodedDataSetMessage#kind()}, and encode consumes it from {@link DataSetMessageDraft#kind()} to
 * select the wire shape (the UADP DataSetFlags2 type bits of Part 14 §7.2.4.5.4 Table 162, the JSON
 * MessageType member of §7.2.5.4.1 Table 185). The built-in mappings decode all four kinds but emit
 * only {@link #KEY_FRAME}, {@link #DELTA_FRAME}, and {@link #KEEP_ALIVE}; {@link #EVENT} drafts are
 * rejected at encode.
 */
public enum DataSetMessageKind {

  /** A key frame: carries a value for every field of the dataset. */
  KEY_FRAME,

  /** A delta frame: carries values only for fields that changed, with explicit field indices. */
  DELTA_FRAME,

  /** A keep-alive message: carries no fields. */
  KEEP_ALIVE,

  /** An event message: carries the fields of one event. */
  EVENT
}

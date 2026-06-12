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

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.jspecify.annotations.Nullable;

/**
 * One decoded field of a DataSetMessage.
 *
 * @param index the index of the field within the dataset: for key frames the position of the field
 *     in the message, for delta frames the explicit index from the wire.
 * @param fieldName the field name from the wire, or {@code null} when the mapping carries fields
 *     positionally (UADP). Name-keyed mappings (the JSON mapping's key and delta frame payloads)
 *     supply it; the engine then prefers name-matching against the reader's effective metadata over
 *     the index.
 * @param value the decoded field value.
 */
public record DecodedField(int index, @Nullable String fieldName, DataValue value) {

  /**
   * Create a new positional {@link DecodedField} without a wire field name.
   *
   * @param index the index of the field within the dataset.
   * @param value the decoded field value.
   */
  public DecodedField(int index, DataValue value) {
    this(index, null, value);
  }
}

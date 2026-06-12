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
 * A {@link FieldSelector} that selects a field by its positional index in the dataset.
 *
 * @param index the zero-based index of the field.
 */
public record FieldIndexSelector(int index) implements FieldSelector {

  /**
   * Create a {@link FieldIndexSelector}.
   *
   * @param index the zero-based index of the field.
   * @throws IllegalArgumentException if {@code index} is negative.
   */
  public FieldIndexSelector {
    if (index < 0) {
      throw new IllegalArgumentException("index must be non-negative: " + index);
    }
  }
}

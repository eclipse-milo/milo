/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * A sampled value to be applied to a UaNode.
 *
 * @param item SampledItem this value is for.
 * @param value the sampled value.
 * @param statusCode an optional status code. If {@code null}, {@link StatusCode#GOOD} is used.
 * @param sourceTime an optional source timestamp that came from the original data source (e.g.,
 *     protocols like DNP3 that provide timestamps with their values). If {@code null}, the
 *     timestamp from the {@link Sample} will be used.
 */
public record SampledValue(
    SampledItem item,
    Variant value,
    @Nullable StatusCode statusCode,
    @Nullable DateTime sourceTime) {

  /**
   * Create a SampledValue with a good status code and no source timestamp.
   *
   * @param item SampledItem this value is for.
   * @param value the sampled value.
   * @return a new SampledValue.
   */
  public static SampledValue of(SampledItem item, Variant value) {
    return new SampledValue(item, value, null, null);
  }
}

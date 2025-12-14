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

/**
 * Interface for posting sampled values to be applied to UaNodes.
 *
 * <p>Implementations vary in timing guarantees:
 *
 * <ul>
 *   <li>{@link AsyncValueChannel} - async, values queued for near-immediate processing
 *   <li>{@link BlockingValueChannel} - blocking, values applied immediately before returning
 * </ul>
 *
 * <p>Uses a bulk-only API to encourage efficient batching.
 */
public interface SampleSink {

  /**
   * Post a sample batch to be applied to the corresponding UaNodes.
   *
   * <p>The {@code timestamp} from the {@link Sample} represents when the values were polled/sampled
   * and is used as the SourceTime for any {@link SampledValue} that doesn't explicitly include one.
   *
   * @param sample the Sample containing values to apply.
   */
  void post(Sample sample);
}

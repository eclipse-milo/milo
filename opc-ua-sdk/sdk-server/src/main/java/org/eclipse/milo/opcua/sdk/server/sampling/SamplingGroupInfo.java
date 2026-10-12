/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

/**
 * A snapshot of one {@link SamplingGroup}, for diagnostics.
 *
 * @param intervalMillis the group's sampling interval.
 * @param itemCount the number of items the group samples.
 * @param requestCount the number of protocol requests the group last reported through {@code
 *     SamplingGroup.setRequestCount}; zero if it reports none.
 */
public record SamplingGroupInfo(long intervalMillis, int itemCount, int requestCount) {}

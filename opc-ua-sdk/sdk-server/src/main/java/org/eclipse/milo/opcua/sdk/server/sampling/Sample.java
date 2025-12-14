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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;

/**
 * A batch of sampled values to be applied to UaNodes.
 *
 * @param timestamp the time the sample was taken. Used as the SourceTime when a {@link
 *     SampledValue} doesn't explicitly include one.
 * @param values a list of SampledValue entries to apply.
 * @see SampleSink
 * @see SampledValue
 * @see SamplingGroup
 */
public record Sample(DateTime timestamp, List<SampledValue> values) {}

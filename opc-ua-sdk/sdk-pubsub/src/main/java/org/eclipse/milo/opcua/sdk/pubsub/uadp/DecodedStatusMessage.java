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

import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * A decoded PubSub Status message (OPC UA Part 14 §7.2.5.5.5 / §7.2.4.6.7).
 *
 * @param messageId the status MessageId.
 * @param publisherId the remote PublisherId.
 * @param timestamp the cyclic report timestamp, or {@code null} for non-cyclic status.
 * @param cyclic whether this is a cyclic status report.
 * @param status the remote PubSubConnection status.
 * @param nextReportTime the expected next cyclic report time, or {@code null} for non-cyclic
 *     status.
 */
public record DecodedStatusMessage(
    String messageId,
    PublisherId publisherId,
    @Nullable DateTime timestamp,
    boolean cyclic,
    PubSubState status,
    @Nullable DateTime nextReportTime) {}

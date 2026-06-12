/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * DataSetMetaData received from the wire, delivered to {@link MetaDataListener}s.
 *
 * <p>This event is notification-only: it reports a metadata announcement that was received and
 * matched a reader on (PublisherId, DataSetWriterId), regardless of the reader's {@link
 * org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy}. Whether the metadata is also
 * <i>applied</i> as the reader's effective decode metadata is policy-gated: readers with {@link
 * org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy#REQUIRE_CONFIGURED} are notified but
 * continue decoding with their configured metadata.
 *
 * @param reader the handle of the DataSetReader that matched the announcement; the metadata is
 *     applied to its decoding only when the reader's MetadataPolicy permits.
 * @param dataSetName the name of the DataSet the metadata describes, or {@code null} if unknown.
 * @param metaData the received metadata.
 * @param configurationVersion the configuration version of the received metadata.
 */
public record MetaDataReceivedEvent(
    PubSubHandle reader,
    @Nullable String dataSetName,
    DataSetMetaDataType metaData,
    ConfigurationVersionDataType configurationVersion) {}

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

/**
 * A by-name reference to a DataSetReader in a {@code PubSubConfig}, used to bind listeners to a
 * specific reader.
 *
 * @param connectionName the name of the connection containing the reader.
 * @param readerGroupName the name of the ReaderGroup containing the reader.
 * @param readerName the name of the DataSetReader.
 */
public record DataSetReaderRef(String connectionName, String readerGroupName, String readerName) {}

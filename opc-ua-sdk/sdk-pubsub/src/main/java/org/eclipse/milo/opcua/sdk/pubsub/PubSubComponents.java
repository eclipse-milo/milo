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

import java.util.Optional;

/**
 * Registry of the runtime components of a {@link PubSubService}, looked up by the names used in the
 * {@code PubSubConfig}.
 *
 * <p>Lookups return empty for names that do not exist in the current configuration; handles for
 * components removed by reconfiguration are invalidated and no longer returned.
 */
public interface PubSubComponents {

  /**
   * Look up the handle of the connection with the given name.
   *
   * @param connectionName the name of the connection.
   * @return the handle of the connection, or empty if it does not exist.
   */
  Optional<PubSubHandle> connection(String connectionName);

  /**
   * Look up the handle of a WriterGroup by connection and group name.
   *
   * @param connectionName the name of the connection.
   * @param writerGroupName the name of the WriterGroup.
   * @return the handle of the WriterGroup, or empty if it does not exist.
   */
  Optional<PubSubHandle> writerGroup(String connectionName, String writerGroupName);

  /**
   * Look up the handle of a DataSetWriter by connection, group, and writer name.
   *
   * @param connectionName the name of the connection.
   * @param groupName the name of the WriterGroup.
   * @param writerName the name of the DataSetWriter.
   * @return the handle of the DataSetWriter, or empty if it does not exist.
   */
  Optional<PubSubHandle> dataSetWriter(String connectionName, String groupName, String writerName);

  /**
   * Look up the handle of a ReaderGroup by connection and group name.
   *
   * @param connectionName the name of the connection.
   * @param readerGroupName the name of the ReaderGroup.
   * @return the handle of the ReaderGroup, or empty if it does not exist.
   */
  Optional<PubSubHandle> readerGroup(String connectionName, String readerGroupName);

  /**
   * Look up the handle of a DataSetReader by connection, group, and reader name.
   *
   * @param connectionName the name of the connection.
   * @param groupName the name of the ReaderGroup.
   * @param readerName the name of the DataSetReader.
   * @return the handle of the DataSetReader, or empty if it does not exist.
   */
  Optional<PubSubHandle> dataSetReader(String connectionName, String groupName, String readerName);
}

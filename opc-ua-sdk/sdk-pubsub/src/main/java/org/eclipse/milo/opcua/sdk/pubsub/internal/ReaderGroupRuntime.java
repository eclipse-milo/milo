/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.jspecify.annotations.Nullable;

/**
 * Runtime for one ReaderGroup: holds the DataSetReader runtimes and ensures the connection's
 * subscriber channel is open while the group is active.
 */
final class ReaderGroupRuntime extends AbstractComponentRuntime {

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;
  private final ReaderGroupConfig config;

  private volatile List<DataSetReaderRuntime> readers;

  ReaderGroupRuntime(
      PubSubServiceImpl service, ConnectionRuntime connection, ReaderGroupConfig config) {

    super(
        ComponentType.READER_GROUP,
        connection.path() + "/" + config.getName(),
        connection,
        config.isEnabled());

    this.service = service;
    this.connection = connection;
    this.config = config;

    var readers = new ArrayList<DataSetReaderRuntime>();
    for (DataSetReaderConfig readerConfig : config.getDataSetReaders()) {
      readers.add(new DataSetReaderRuntime(service, connection, this, readerConfig));
    }
    this.readers = List.copyOf(readers);
  }

  ReaderGroupConfig config() {
    return config;
  }

  List<DataSetReaderRuntime> readerRuntimes() {
    return readers;
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    return readers;
  }

  @Override
  void activate() throws UaException {
    checkMessageSecurity();

    connection.ensureSubscriberChannel();
  }

  /** Release all resources of this runtime. The runtime is unusable afterwards. */
  void dispose() {
    readers.forEach(DataSetReaderRuntime::dispose);
  }

  void addReaderRuntime(DataSetReaderRuntime reader) {
    var readers = new ArrayList<>(this.readers);
    readers.add(reader);
    this.readers = List.copyOf(readers);
  }

  void removeReaderRuntime(DataSetReaderRuntime reader) {
    var readers = new ArrayList<>(this.readers);
    readers.remove(reader);
    this.readers = List.copyOf(readers);
  }

  @Nullable DataSetReaderRuntime findReaderRuntime(String name) {
    for (DataSetReaderRuntime reader : readers) {
      if (reader.config().getName().equals(name)) {
        return reader;
      }
    }
    return null;
  }

  private void checkMessageSecurity() throws UaException {
    MessageSecurityConfig security = config.getMessageSecurity();

    if (security != null && security.getMode() != MessageSecurityMode.None) {
      var e =
          new UaException(
              StatusCodes.Bad_NotSupported,
              "MessageSecurityMode %s is not supported (reader group '%s'); only None is"
                      .formatted(security.getMode(), path())
                  + " supported in this version");
      service.getDiagnostics().error(path(), e.getStatusCode(), e.getMessage(), e);
      throw e;
    }
  }
}

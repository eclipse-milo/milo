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
 * Listener notified when DataSetMetaData is received from the wire.
 *
 * <p>Notification is independent of metadata application: an event is delivered for every received
 * announcement that matches a reader on (PublisherId, DataSetWriterId), including readers whose
 * {@link org.eclipse.milo.opcua.sdk.pubsub.config.MetadataPolicy} (e.g. {@code REQUIRE_CONFIGURED})
 * prevents the metadata from affecting their decoding. See {@link MetaDataReceivedEvent}.
 */
@FunctionalInterface
public interface MetaDataListener {

  /**
   * Notification that DataSetMetaData was received.
   *
   * @param event the received metadata event.
   */
  void onMetaData(MetaDataReceivedEvent event);
}

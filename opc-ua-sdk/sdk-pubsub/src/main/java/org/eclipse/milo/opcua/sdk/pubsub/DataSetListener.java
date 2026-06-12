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
 * Subscriber-side listener: pushed decoded DataSetMessages as {@link DataSetReceivedEvent}s.
 *
 * <p>One listener type serves both global and per-reader registration; see {@link
 * PubSubService#addDataSetListener(DataSetListener)} and {@link
 * PubSubService#addDataSetListener(DataSetReaderRef, DataSetListener)}.
 */
@FunctionalInterface
public interface DataSetListener {

  /**
   * Notification that a DataSet was received and decoded.
   *
   * @param event the received DataSet event.
   */
  void onDataSet(DataSetReceivedEvent event);
}

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

/** The type of PubSub component identified by a {@link PubSubHandle}. */
public enum ComponentType {

  /** A PubSubConnection. */
  CONNECTION,

  /** A WriterGroup belonging to a connection. */
  WRITER_GROUP,

  /** A DataSetWriter belonging to a WriterGroup. */
  DATA_SET_WRITER,

  /** A ReaderGroup belonging to a connection. */
  READER_GROUP,

  /** A DataSetReader belonging to a ReaderGroup. */
  DATA_SET_READER,

  /** A PublishedDataSet. */
  PUBLISHED_DATA_SET,

  /** A standalone SubscribedDataSet. */
  STANDALONE_SUBSCRIBED_DATA_SET,

  /** A SecurityGroup. */
  SECURITY_GROUP
}

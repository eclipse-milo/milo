/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

/**
 * Controls how a DataSetReader obtains and trusts the {@code DataSetMetaData} used to decode
 * received DataSetMessages.
 */
public enum MetadataPolicy {

  /**
   * Decode only with locally configured metadata; messages with a mismatched configuration version
   * are dropped and counted in diagnostics.
   */
  REQUIRE_CONFIGURED,

  /** Accept metadata discovered from the wire (metadata messages / discovery announcements). */
  ACCEPT_DISCOVERED,

  /**
   * Request metadata via discovery when missing, then accept it.
   *
   * <p>A reader with this policy and no effective metadata emits UADP discovery probes (randomized
   * initial delay, then doubling retry intervals) until metadata is obtained or the publisher
   * denies the request; readers whose PublisherId or DataSetWriterId filters are wildcards defer
   * probing until a first matching data message reveals the identifiers. Received metadata is then
   * accepted as with {@link #ACCEPT_DISCOVERED}.
   */
  REQUEST_IF_MISSING
}

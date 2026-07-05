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

/** Listener notified when a remote publisher status message is received. */
@FunctionalInterface
public interface PublisherStatusListener {

  /**
   * Notification that a remote publisher reported PubSub status, or that a cyclic status report
   * timed out.
   *
   * @param event the received or synthesized status event.
   */
  void onPublisherStatus(PublisherStatusReceivedEvent event);
}

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
 * Listener notified of diagnostic events from a {@link PubSubService}.
 *
 * <p>In v1 only error events are delivered, e.g. decode failures, source read failures, and
 * transport send failures.
 */
@FunctionalInterface
public interface PubSubDiagnosticsListener {

  /**
   * Notification of a diagnostic event.
   *
   * @param event the diagnostic event.
   */
  void onDiagnosticsEvent(PubSubDiagnosticsEvent event);
}

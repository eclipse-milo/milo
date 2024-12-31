/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;

public interface EventNotifier {

  /**
   * Fire an Event to registers {@link EventListener}s of this notifier.
   *
   * @param event the Event instance to fire.
   */
  void fire(BaseEventTypeNode event);

  /**
   * Register an {@link EventListener} to receive Event notifications from this notifier.
   *
   * @param eventListener the {@link EventListener} to register.
   */
  void register(EventListener eventListener);

  /**
   * Unregister a previously-registered {@link EventListener} from this notifier.
   *
   * @param eventListener the {@link EventListener} to unregister.
   */
  void unregister(EventListener eventListener);
}

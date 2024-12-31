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

public interface EventListener {

  /**
   * An Event was fired by an {@link EventNotifier} this listener is registered with.
   *
   * @param node the Event that was fired.
   */
  void onEvent(BaseEventTypeNode node);
}

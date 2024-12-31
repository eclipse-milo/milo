/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class TriggeringLinks {

  private final Map<UInteger, BaseMonitoredItem<?>> triggeredItems = new ConcurrentHashMap<>();

  private final BaseMonitoredItem<?> triggeringItem;

  public TriggeringLinks(BaseMonitoredItem<?> triggeringItem) {
    this.triggeringItem = triggeringItem;
  }

  public BaseMonitoredItem<?> getTriggeringItem() {
    return triggeringItem;
  }

  public Map<UInteger, BaseMonitoredItem<?>> getTriggeredItems() {
    return triggeredItems;
  }

  public boolean isEmpty() {
    return triggeredItems.isEmpty();
  }
}

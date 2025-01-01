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

public class SubscriptionDeletedEvent {

  private final Subscription subscription;

  public SubscriptionDeletedEvent(Subscription subscription) {
    this.subscription = subscription;
  }

  public Subscription getSubscription() {
    return subscription;
  }
}

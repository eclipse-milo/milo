/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.servicesets.SubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultSubscriptionServiceSet;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

/**
 * A {@link SubscriptionServiceSet} that forwards every operation to a delegate.
 *
 * <p>Mirrors {@link DelegatingSessionServiceSet}. Subclass and override individual {@code on*}
 * methods to deterministically control the server responses the client's {@code PublishingManager}
 * and {@code OpcUaSubscription} observe, while leaving the remainder of the real server behavior
 * intact. Register with {@code server.addServiceSet(path, serviceSet)}; a later registration
 * replaces the default handlers for that path.
 *
 * @see ScriptableSubscriptionServiceSet
 */
public class DelegatingSubscriptionServiceSet implements SubscriptionServiceSet {

  private final SubscriptionServiceSet delegate;

  public DelegatingSubscriptionServiceSet(OpcUaServer server) {
    this(new DefaultSubscriptionServiceSet(server));
  }

  public DelegatingSubscriptionServiceSet(SubscriptionServiceSet delegate) {
    this.delegate = Objects.requireNonNull(delegate, "delegate");
  }

  @Override
  public CreateSubscriptionResponse onCreateSubscription(
      ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException {
    return delegate.onCreateSubscription(context, request);
  }

  @Override
  public ModifySubscriptionResponse onModifySubscription(
      ServiceRequestContext context, ModifySubscriptionRequest request) throws UaException {
    return delegate.onModifySubscription(context, request);
  }

  @Override
  public DeleteSubscriptionsResponse onDeleteSubscriptions(
      ServiceRequestContext context, DeleteSubscriptionsRequest request) throws UaException {
    return delegate.onDeleteSubscriptions(context, request);
  }

  @Override
  public TransferSubscriptionsResponse onTransferSubscriptions(
      ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException {
    return delegate.onTransferSubscriptions(context, request);
  }

  @Override
  public SetPublishingModeResponse onSetPublishingMode(
      ServiceRequestContext context, SetPublishingModeRequest request) throws UaException {
    return delegate.onSetPublishingMode(context, request);
  }

  @Override
  public RepublishResponse onRepublish(ServiceRequestContext context, RepublishRequest request)
      throws UaException {
    return delegate.onRepublish(context, request);
  }

  @Override
  public CompletableFuture<PublishResponse> onPublish(
      ServiceRequestContext context, PublishRequest request) {
    return delegate.onPublish(context, request);
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets;

import java.util.concurrent.CompletableFuture;
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

public interface SubscriptionServiceSet {

  CreateSubscriptionResponse onCreateSubscription(
      ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException;

  ModifySubscriptionResponse onModifySubscription(
      ServiceRequestContext context, ModifySubscriptionRequest request) throws UaException;

  DeleteSubscriptionsResponse onDeleteSubscriptions(
      ServiceRequestContext context, DeleteSubscriptionsRequest request) throws UaException;

  TransferSubscriptionsResponse onTransferSubscriptions(
      ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException;

  SetPublishingModeResponse onSetPublishingMode(
      ServiceRequestContext context, SetPublishingModeRequest request) throws UaException;

  RepublishResponse onRepublish(ServiceRequestContext context, RepublishRequest request)
      throws UaException;

  CompletableFuture<PublishResponse> onPublish(
      ServiceRequestContext context, PublishRequest request);
}

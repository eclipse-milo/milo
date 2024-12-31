/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.servicesets.SubscriptionServiceSet;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
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
import org.eclipse.milo.opcua.stack.core.types.structured.TransferResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public class DefaultSubscriptionServiceSet implements SubscriptionServiceSet {

  private final OpcUaServer server;

  public DefaultSubscriptionServiceSet(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public CreateSubscriptionResponse onCreateSubscription(
      ServiceRequestContext context, CreateSubscriptionRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().createSubscription(request).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getCreateSubscriptionCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(e).orElse(new UaException(e));
    } finally {
      session.getSessionDiagnostics().getCreateSubscriptionCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public ModifySubscriptionResponse onModifySubscription(
      ServiceRequestContext context, ModifySubscriptionRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().modifySubscription(request).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getModifySubscriptionCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(e).orElse(new UaException(e));
    } finally {
      session.getSessionDiagnostics().getModifySubscriptionCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public DeleteSubscriptionsResponse onDeleteSubscriptions(
      ServiceRequestContext context, DeleteSubscriptionsRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().deleteSubscriptions(request).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getDeleteSubscriptionsCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(e).orElse(new UaException(e));
    } finally {
      session.getSessionDiagnostics().getDeleteSubscriptionsCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public TransferSubscriptionsResponse onTransferSubscriptions(
      ServiceRequestContext context, TransferSubscriptionsRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return transferSubscriptions(request, session).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getTransferSubscriptionsCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(e).orElse(new UaException(e));
    } finally {
      session.getSessionDiagnostics().getTransferSubscriptionsCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public SetPublishingModeResponse onSetPublishingMode(
      ServiceRequestContext context, SetPublishingModeRequest request) throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().setPublishingMode(request).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getSetPublishingModeCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(e).orElse(new UaException(e));
    } finally {
      session.getSessionDiagnostics().getSetPublishingModeCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public RepublishResponse onRepublish(ServiceRequestContext context, RepublishRequest request)
      throws UaException {

    Session session = server.getSessionManager().getSession(context, request.getRequestHeader());

    try {
      return session.getSubscriptionManager().republish(request).get();
    } catch (Exception e) {
      session.getSessionDiagnostics().getRepublishCount().incrementErrorCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementErrorCount();

      throw UaException.extract(e).orElse(new UaException(e));
    } finally {
      session.getSessionDiagnostics().getRepublishCount().incrementTotalCount();
      session.getSessionDiagnostics().getTotalRequestCount().incrementTotalCount();
    }
  }

  @Override
  public CompletableFuture<PublishResponse> onPublish(
      ServiceRequestContext context, PublishRequest request) {
    Session session;
    try {
      session = server.getSessionManager().getSession(context, request.getRequestHeader());
    } catch (UaException e) {
      return CompletableFuture.failedFuture(e);
    }

    CompletableFuture<PublishResponse> future =
        session.getSubscriptionManager().publish(context, request);

    session.getSessionDiagnostics().getPublishCount().record(future);
    session.getSessionDiagnostics().getTotalRequestCount().record(future);

    return future;
  }

  private CompletableFuture<TransferSubscriptionsResponse> transferSubscriptions(
      TransferSubscriptionsRequest request, Session session) {

    List<UInteger> subscriptionIds = Lists.ofNullable(request.getSubscriptionIds());

    if (subscriptionIds.isEmpty()) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    var results = new ArrayList<TransferResult>();

    for (UInteger subscriptionId : subscriptionIds) {
      Subscription subscription = server.getSubscriptions().get(subscriptionId);

      if (subscription == null) {
        results.add(
            new TransferResult(
                new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid), new UInteger[0]));
      } else {
        Session otherSession = subscription.getSession();

        if (!sessionsHaveSameUser(session, otherSession)) {
          results.add(
              new TransferResult(
                  new StatusCode(StatusCodes.Bad_UserAccessDenied), new UInteger[0]));
        } else {
          UInteger[] availableSequenceNumbers;

          synchronized (subscription) {
            otherSession
                .getSubscriptionManager()
                .sendStatusChangeNotification(
                    subscription, new StatusCode(StatusCodes.Good_SubscriptionTransferred));
            otherSession.getSubscriptionManager().removeSubscription(subscriptionId);

            subscription.setSubscriptionManager(session.getSubscriptionManager());
            session.getSubscriptionManager().addSubscription(subscription);

            subscription.getMonitoredItems().values().forEach(item -> item.setSession(session));

            availableSequenceNumbers = subscription.getAvailableSequenceNumbers();

            if (request.getSendInitialValues()) {
              subscription.getMonitoredItems().values().stream()
                  .filter(item -> item instanceof MonitoredDataItem)
                  .map(item -> (MonitoredDataItem) item)
                  .forEach(MonitoredDataItem::maybeSendLastValue);
            }
          }

          subscription.getSubscriptionDiagnostics().getTransferRequestCount().increment();

          ApplicationDescription toClient = session.getClientDescription();
          ApplicationDescription fromClient = otherSession.getClientDescription();

          if (Objects.equals(toClient, fromClient)) {
            subscription.getSubscriptionDiagnostics().getTransferredToSameClientCount().increment();
          } else {
            subscription.getSubscriptionDiagnostics().getTransferredToAltClientCount().increment();
          }

          results.add(new TransferResult(StatusCode.GOOD, availableSequenceNumbers));
        }
      }
    }

    var response =
        new TransferSubscriptionsResponse(
            createResponseHeader(request),
            results.toArray(TransferResult[]::new),
            new DiagnosticInfo[0]);

    return CompletableFuture.completedFuture(response);
  }

  private static boolean sessionsHaveSameUser(Session s1, Session s2) {
    Identity i1 = s1.getIdentity();
    Identity i2 = s2.getIdentity();
    return i1 != null && i2 != null && i1.equalTo(i2);
  }
}

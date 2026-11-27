/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jspecify.annotations.Nullable;

interface Event {

  // user-initiated events
  class OpenSession implements Event {
    final CompletableFuture<OpcUaSession> future = new CompletableFuture<>();

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class CloseSession implements Event {
    final CompletableFuture<Unit> future = new CompletableFuture<>();

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class GetSession implements Event {
    final CompletableFuture<OpcUaSession> future = new CompletableFuture<>();

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ChangeIdentity implements Event {
    final IdentityProvider identityProvider;
    final CompletableFuture<OpcUaSession> future = new CompletableFuture<>();

    ChangeIdentity(IdentityProvider identityProvider) {
      this.identityProvider = identityProvider;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  // "internal" events
  class CloseSessionSuccess implements Event {
    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class CreatingWaitExpired implements Event {
    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ReactivatingWaitExpired implements Event {
    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class CreateSessionSuccess implements Event {
    final CreateSessionResponse response;

    CreateSessionSuccess(CreateSessionResponse response) {
      this.response = response;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class CreateSessionFailure implements Event {
    final Throwable failure;

    CreateSessionFailure(Throwable failure) {
      this.failure = failure;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ActivateSessionSuccess implements Event {
    final OpcUaSession session;

    ActivateSessionSuccess(OpcUaSession session) {
      this.session = session;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ActivateSessionFailure implements Event {
    final Throwable failure;

    ActivateSessionFailure(Throwable failure) {
      this.failure = failure;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ReactivateSessionSuccess implements Event {
    final OpcUaSession session;

    ReactivateSessionSuccess(OpcUaSession session) {
      this.session = session;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ReactivateSessionFailure implements Event {
    final Throwable failure;

    ReactivateSessionFailure(Throwable failure) {
      this.failure = failure;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class TransferSubscriptionsSuccess implements Event {
    final OpcUaSession session;

    TransferSubscriptionsSuccess(OpcUaSession session) {
      this.session = session;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class TransferSubscriptionsFailure implements Event {
    final Throwable failure;

    TransferSubscriptionsFailure(Throwable failure) {
      this.failure = failure;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class InitializeSuccess implements Event {
    final OpcUaSession session;

    InitializeSuccess(OpcUaSession session) {
      this.session = session;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class InitializeFailure implements Event {
    final Throwable failure;

    InitializeFailure(Throwable failure) {
      this.failure = failure;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class KeepAlive implements Event {
    final OpcUaSession session;

    KeepAlive(OpcUaSession session) {
      this.session = session;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class KeepAliveFailure implements Event {
    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ServiceFault implements Event {
    final StatusCode statusCode;

    ServiceFault(StatusCode statusCode) {
      this.statusCode = statusCode;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ConnectionLost implements Event {
    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ChangeIdentityPreNegotiationSuccess implements Event {
    final SessionFsm.ChangeIdentityAttempt attempt;
    final OpcUaSession session;
    final EndpointDescription endpoint;
    final IdentityProvider currentProvider;
    final ByteString serverNonce;
    final SecurityPolicy targetEnhancedPolicy;
    final ByteString receiverEphemeralPublicKey;

    ChangeIdentityPreNegotiationSuccess(
        SessionFsm.ChangeIdentityAttempt attempt,
        OpcUaSession session,
        EndpointDescription endpoint,
        IdentityProvider currentProvider,
        ByteString serverNonce,
        SecurityPolicy targetEnhancedPolicy,
        ByteString receiverEphemeralPublicKey) {
      this.attempt = attempt;
      this.session = session;
      this.endpoint = endpoint;
      this.currentProvider = currentProvider;
      this.serverNonce = serverNonce;
      this.targetEnhancedPolicy = targetEnhancedPolicy;
      this.receiverEphemeralPublicKey = receiverEphemeralPublicKey;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ChangeIdentitySuccess implements Event {
    final SessionFsm.ChangeIdentityAttempt attempt;
    final OpcUaSession session;
    final StatusCode serviceResult;
    final ByteString serverNonce;
    final @Nullable ByteString receiverEphemeralPublicKey;
    final @Nullable SecurityPolicy receiverEphemeralPublicKeyPolicy;

    ChangeIdentitySuccess(
        SessionFsm.ChangeIdentityAttempt attempt,
        OpcUaSession session,
        StatusCode serviceResult,
        ByteString serverNonce,
        @Nullable ByteString receiverEphemeralPublicKey,
        @Nullable SecurityPolicy receiverEphemeralPublicKeyPolicy) {
      this.attempt = attempt;
      this.session = session;
      this.serviceResult = serviceResult;
      this.serverNonce = serverNonce;
      this.receiverEphemeralPublicKey = receiverEphemeralPublicKey;
      this.receiverEphemeralPublicKeyPolicy = receiverEphemeralPublicKeyPolicy;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }

  class ChangeIdentityFailure implements Event {
    final SessionFsm.ChangeIdentityAttempt attempt;
    final Throwable failure;
    final boolean recoverSession;

    ChangeIdentityFailure(
        SessionFsm.ChangeIdentityAttempt attempt, Throwable failure, boolean recoverSession) {
      this.attempt = attempt;
      this.failure = failure;
      this.recoverSession = recoverSession;
    }

    @Override
    public String toString() {
      return getClass().getSimpleName();
    }
  }
}

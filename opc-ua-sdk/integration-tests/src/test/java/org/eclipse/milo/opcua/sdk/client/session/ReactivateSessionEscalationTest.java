/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

/**
 * Reactivation abandoned in favor of a new Session when ActivateSession tells the Client the
 * Session is gone.
 *
 * <p>Part 4 §6.7: if ActivateSession fails the Client shall create a new Session. The Reactivating
 * state decided that by exception class — only a {@link UaServiceFaultException} escalated to
 * CreatingWait — but a StatusCode is not always delivered as an application-level ServiceFault. A
 * Server that answers ActivateSession with a channel-level Error message instead has it turned into
 * a plain {@link UaException} by {@code InboundUascResponseHandler}, which took the catch-all edge
 * back to ReactivatingWait and re-sent the same doomed ActivateSession forever, at the 16s backoff
 * cap, for a Session the Server has already told the Client it no longer has. It is the same
 * defective-Server behaviour {@code SessionFsmFactory} already guards against on the
 * TransferSubscriptions path.
 *
 * <p>Both tests arm the same StatusCode — {@code Bad_SessionIdInvalid}, one of the definitive
 * session-level statuses — and differ only in how it is delivered, so the contrast isolates the
 * exception class as the thing being (wrongly) dispatched on.
 *
 * <p>The failure is injected at the transport rather than provoked from the Server because a
 * conformant Server, Milo's own included, always answers a stale ActivateSession with a
 * ServiceFault and cannot be made to emit a channel-level Error through a {@code
 * SessionServiceSet}.
 */
public class ReactivateSessionEscalationTest {

  /**
   * Reactivation retries at 1s, 2s, 4s, 8s..., so this covers four attempts: long enough that a
   * client which escalates at all has escalated, and that a client which does not has visibly
   * settled into the loop.
   */
  private static final long AWAIT_TIMEOUT_MILLIS = 15_000;

  /**
   * Control: the identical injection delivered as an application-level ServiceFault. It proves the
   * fixture observes escalation at all, so the test below fails because of how the StatusCode
   * arrived and not because a new Session is unobservable here.
   */
  @Test
  void reactivationEscalatesOnAServiceFault() throws Exception {
    try (var fixture = new Fixture(ActivateSessionFailure.SERVICE_FAULT)) {
      fixture.loseConnection();

      assertTrue(fixture.awaitReactivationAttempt(), "the client never tried to reactivate");

      assertTrue(
          fixture.awaitNewSession(),
          "the client never created a new Session after ActivateSession answered"
              + " Bad_SessionIdInvalid as a ServiceFault");
    }
  }

  @Test
  void reactivationEscalatesOnAChannelLevelError() throws Exception {
    try (var fixture = new Fixture(ActivateSessionFailure.CHANNEL_LEVEL_ERROR)) {
      fixture.loseConnection();

      assertTrue(fixture.awaitReactivationAttempt(), "the client never tried to reactivate");

      assertTrue(
          fixture.awaitNewSession(),
          "the client never created a new Session after ActivateSession answered"
              + " Bad_SessionIdInvalid as a channel-level Error message: the failure carried the"
              + " same definitive StatusCode as a ServiceFault would, but arrived as a plain"
              + " UaException, so Reactivating kept re-sending ActivateSession for a Session the"
              + " Server has already disowned");
    }
  }

  // region helpers

  /**
   * How the transport hands the Client {@code Bad_SessionIdInvalid} in answer to ActivateSession.
   */
  private enum ActivateSessionFailure {

    /** As an application-level ServiceFault, i.e. what a conformant Server sends. */
    SERVICE_FAULT,

    /**
     * As a channel-level Error message, which {@code InboundUascResponseHandler} turns into a plain
     * {@link UaException} before the SDK sees it.
     */
    CHANNEL_LEVEL_ERROR;

    UaException create() {
      if (this == SERVICE_FAULT) {
        var responseHeader =
            new ResponseHeader(
                DateTime.now(),
                uint(0),
                new StatusCode(StatusCodes.Bad_SessionIdInvalid),
                DiagnosticInfo.NULL_VALUE,
                null,
                null);

        return new UaServiceFaultException(new ServiceFault(responseHeader));
      } else {
        return new UaException(StatusCodes.Bad_SessionIdInvalid);
      }
    }
  }

  /**
   * An {@link OpcTcpClientTransport} that, once armed, answers every ActivateSessionRequest itself
   * without sending it, and counts the CreateSessionRequests the Client sends.
   */
  private static final class SessionDisowningTransport extends OpcTcpClientTransport {

    private final AtomicInteger createSessionRequests = new AtomicInteger(0);
    private final AtomicInteger interceptedActivations = new AtomicInteger(0);

    private final ActivateSessionFailure failure;

    private volatile boolean armed = false;

    SessionDisowningTransport(OpcTcpClientTransportConfig config, ActivateSessionFailure failure) {
      super(config);

      this.failure = failure;
    }

    void arm() {
      armed = true;
    }

    void disarm() {
      armed = false;
    }

    int createSessionCount() {
      return createSessionRequests.get();
    }

    int interceptedActivationCount() {
      return interceptedActivations.get();
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      if (requestMessage instanceof CreateSessionRequest) {
        createSessionRequests.incrementAndGet();
      }

      if (armed && requestMessage instanceof ActivateSessionRequest) {
        interceptedActivations.incrementAndGet();

        var future = new CompletableFuture<UaResponseMessageType>();

        // Completed on the transport's executor, which is where AbstractUascClientTransport fails a
        // request, rather than inline on the caller's thread.
        getConfig().getExecutor().execute(() -> future.completeExceptionally(failure.create()));

        return future;
      }

      return super.sendRequestMessage(requestMessage);
    }
  }

  /**
   * A running Server and a connected client whose transport is armed to fail every subsequent
   * ActivateSession, so the arming is strictly ordered before the connection loss that starts
   * reactivation.
   */
  private static final class Fixture implements AutoCloseable {

    private final OpcUaServer server;
    private final OpcUaClient client;
    private final SessionDisowningTransport transport;

    private final int createSessionsBeforeArming;

    Fixture(ActivateSessionFailure failure) throws Exception {
      server = TestServer.create().getServer();
      server.startup().get();

      var transportHolder = new SessionDisowningTransport[1];

      client =
          TestClient.createWithTransport(
              server,
              transportConfig -> {
                transportHolder[0] = new SessionDisowningTransport(transportConfig, failure);
                return transportHolder[0];
              },
              // No keep-alive traffic within this test's own bounds: a keep-alive failure would
              // reach ReactivatingWait by a different route than the connection loss being
              // scripted.
              cfg -> cfg.setKeepAliveInterval(uint(60_000)));

      transport = transportHolder[0];

      client.connect();

      createSessionsBeforeArming = transport.createSessionCount();
      transport.arm();
    }

    /**
     * Drop the SecureChannel, which is what moves the SessionFsm from Active to ReactivatingWait.
     */
    void loseConnection() throws Exception {
      Channel channel = transport.getChannelFsm().getChannel().get(10, SECONDS);
      channel.close().syncUninterruptibly();
    }

    boolean awaitReactivationAttempt() throws Exception {
      return awaitTrue(() -> transport.interceptedActivationCount() >= 1);
    }

    boolean awaitNewSession() throws Exception {
      return awaitTrue(() -> transport.createSessionCount() > createSessionsBeforeArming);
    }

    @Override
    public void close() throws Exception {
      // Let the shutdown sequence talk to the Server again; a client stuck reactivating has a
      // Session to close and cannot get there through an interception that never ends.
      transport.disarm();
      try {
        client.disconnectAsync().get(10, SECONDS);
      } finally {
        server.shutdown().get(10, SECONDS);
      }
    }
  }

  /** Polls {@code condition} until it holds or {@link #AWAIT_TIMEOUT_MILLIS} elapses. */
  private static boolean awaitTrue(ThrowingBooleanSupplier condition) throws Exception {
    long deadline = System.nanoTime() + MILLISECONDS.toNanos(AWAIT_TIMEOUT_MILLIS);

    while (System.nanoTime() < deadline) {
      if (condition.get()) {
        return true;
      }
      Thread.sleep(25);
    }

    return condition.get();
  }

  @FunctionalInterface
  private interface ThrowingBooleanSupplier {
    boolean get() throws Exception;
  }

  // endregion
}

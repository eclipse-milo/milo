/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReverseConnectChannelOwnerTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test-server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private final AtomicLong requestId = new AtomicLong();

  private HashedWheelTimer wheelTimer;
  private ManualScheduler scheduler;
  private TestHandshakeStarter handshakeStarter;
  private ReverseConnectChannelOwner owner;

  @BeforeEach
  void setUp() {
    wheelTimer = new HashedWheelTimer();
    scheduler = new ManualScheduler();
    handshakeStarter = new TestHandshakeStarter();
    owner = newOwner(handshakeStarter, scheduler);
  }

  @AfterEach
  void tearDown() {
    wheelTimer.stop();
  }

  @Test
  void connectCompletesWhenHandshakeSucceeds() throws Exception {
    ClientApplicationContext application = newApplicationContext();
    CompletableFuture<Channel> connectFuture = owner.connect(application);

    var channel = new EmbeddedChannel();
    owner.accepted(channel, reverseHello());
    handshakeStarter.succeed(channel);

    assertSame(channel, connectFuture.get(1, TimeUnit.SECONDS));
    assertSame(channel, owner.getChannel().get(1, TimeUnit.SECONDS));
    assertSame(application, handshakeStarter.applications().get(0));
    assertEquals(ReverseConnectChannelOwner.State.Connected, owner.getState());
  }

  @Test
  void handshakeFailureFailsWaitingConnect() {
    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());

    var channel = new EmbeddedChannel();
    owner.accepted(channel, reverseHello());

    var failure = new UaException(StatusCodes.Bad_Timeout, "handshake timed out");
    handshakeStarter.fail(channel, failure);

    assertUaStatus(StatusCodes.Bad_Timeout, connectFuture);
    assertFalse(channel.isOpen());
    assertEquals(ReverseConnectChannelOwner.State.Armed, owner.getState());
  }

  @Test
  void duplicateAcceptedChannelIsClosedDuringHandshake() throws Exception {
    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());

    var channel = new EmbeddedChannel();
    var duplicate = new EmbeddedChannel();

    owner.accepted(channel, reverseHello());
    owner.accepted(duplicate, reverseHello());
    handshakeStarter.succeed(channel);

    assertFalse(duplicate.isOpen());
    assertEquals(1, handshakeStarter.starts());
    assertSame(channel, connectFuture.get(1, TimeUnit.SECONDS));
  }

  @Test
  void duplicateConnectsShareOneLifecycleAndKeepFirstContext() throws Exception {
    ClientApplicationContext firstApplication = newApplicationContext();
    ClientApplicationContext duplicateApplication = newApplicationContext();

    CompletableFuture<Channel> firstConnect = owner.connect(firstApplication);
    CompletableFuture<Channel> duplicateConnect = owner.connect(duplicateApplication);

    var channel = new EmbeddedChannel();
    owner.accepted(channel, reverseHello());
    handshakeStarter.succeed(channel);

    assertSame(channel, firstConnect.get(1, TimeUnit.SECONDS));
    assertSame(channel, duplicateConnect.get(1, TimeUnit.SECONDS));
    assertEquals(1, handshakeStarter.starts());
    assertSame(firstApplication, handshakeStarter.applications().get(0));
  }

  @Test
  void disconnectDuringWaitFailsWaiterAndResetsOwner() throws Exception {
    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());

    CompletableFuture<Unit> disconnectFuture = owner.disconnect();

    assertEquals(Unit.VALUE, disconnectFuture.get(1, TimeUnit.SECONDS));
    assertUaStatus(StatusCodes.Bad_ConnectionClosed, connectFuture);
    assertEquals(ReverseConnectChannelOwner.State.Idle, owner.getState());
  }

  @Test
  void listenerStopDuringWaitFailsWaiterAndStopsOwner() throws Exception {
    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());

    CompletableFuture<Unit> listenerStoppedFuture =
        owner.listenerStopped(new UaException(StatusCodes.Bad_Shutdown, "listener stopped"));

    assertEquals(Unit.VALUE, listenerStoppedFuture.get(1, TimeUnit.SECONDS));
    assertUaStatus(StatusCodes.Bad_Shutdown, connectFuture);
    assertEquals(ReverseConnectChannelOwner.State.Stopped, owner.getState());
  }

  @Test
  void listenerStopFromConnectedClosesChannelAndRejectsFutureWaiters() throws Exception {
    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());

    var channel = new EmbeddedChannel();
    owner.accepted(channel, reverseHello());
    handshakeStarter.succeed(channel);

    assertSame(channel, connectFuture.get(1, TimeUnit.SECONDS));

    CompletableFuture<Unit> listenerStoppedFuture =
        owner.listenerStopped(new UaException(StatusCodes.Bad_Shutdown, "listener stopped"));

    assertEquals(Unit.VALUE, listenerStoppedFuture.get(1, TimeUnit.SECONDS));
    assertFalse(channel.isOpen());
    assertEquals(ReverseConnectChannelOwner.State.Stopped, owner.getState());
    assertUaStatus(StatusCodes.Bad_Shutdown, owner.connect(newApplicationContext()));
    assertUaStatus(StatusCodes.Bad_Shutdown, owner.getChannel());
  }

  @Test
  void connectWaiterTimesOutWithoutAcceptedChannel() {
    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());

    scheduler.runNext();

    assertUaStatus(StatusCodes.Bad_Timeout, connectFuture);
    assertEquals(ReverseConnectChannelOwner.State.Armed, owner.getState());
  }

  @Test
  void getChannelWaiterTimesOutWithoutAcceptedChannel() {
    CompletableFuture<Channel> getChannelFuture = owner.getChannel();

    scheduler.runNext();

    assertUaStatus(StatusCodes.Bad_Timeout, getChannelFuture);
    assertEquals(ReverseConnectChannelOwner.State.Idle, owner.getState());
  }

  @Test
  void acceptedBeforeContextStartsHandshakeWhenConnectArrivesBeforeTimeout() throws Exception {
    var channel = new EmbeddedChannel();

    owner.accepted(channel, reverseHello());

    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());
    scheduler.runNext();

    assertEquals(1, handshakeStarter.starts());
    assertFalse(connectFuture.isDone());

    handshakeStarter.succeed(channel);
    scheduler.runAll();

    assertSame(channel, connectFuture.get(1, TimeUnit.SECONDS));
    assertSame(channel, owner.getActiveChannel());
  }

  @Test
  void duplicateAcceptedBeforeContextIsClosedAndOriginalIsKept() throws Exception {
    var channel = new EmbeddedChannel();
    var duplicate = new EmbeddedChannel();

    owner.accepted(channel, reverseHello());
    owner.accepted(duplicate, reverseHello());

    assertFalse(duplicate.isOpen());

    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());
    handshakeStarter.succeed(channel);

    assertEquals(1, handshakeStarter.starts());
    assertSame(channel, connectFuture.get(1, TimeUnit.SECONDS));
  }

  @Test
  void acceptedBeforeContextTimesOutWithoutStartingHandshake() {
    var channel = new EmbeddedChannel();

    owner.accepted(channel, reverseHello());
    scheduler.runNext();

    assertFalse(channel.isOpen());
    assertEquals(0, handshakeStarter.starts());
    assertEquals(ReverseConnectChannelOwner.State.Idle, owner.getState());
  }

  @Test
  void reconnectAfterNormalDisconnectStartsNewLifecycle() throws Exception {
    CompletableFuture<Channel> firstConnect = owner.connect(newApplicationContext());
    var firstChannel = new EmbeddedChannel();
    owner.accepted(firstChannel, reverseHello());
    handshakeStarter.succeed(firstChannel);

    assertSame(firstChannel, firstConnect.get(1, TimeUnit.SECONDS));
    assertEquals(Unit.VALUE, owner.disconnect().get(1, TimeUnit.SECONDS));

    CompletableFuture<Channel> secondConnect = owner.connect(newApplicationContext());
    var secondChannel = new EmbeddedChannel();
    owner.accepted(secondChannel, reverseHello());
    handshakeStarter.succeed(secondChannel);

    assertFalse(firstChannel.isOpen());
    assertSame(secondChannel, secondConnect.get(1, TimeUnit.SECONDS));
    assertEquals(2, handshakeStarter.starts());
  }

  @Test
  void stalePendingAcceptTimeoutFromPreviousLifecycleCannotCloseCurrentPendingChannel()
      throws Exception {

    var firstChannel = new EmbeddedChannel();
    owner.accepted(firstChannel, reverseHello());

    assertEquals(Unit.VALUE, owner.disconnect().get(1, TimeUnit.SECONDS));

    var secondChannel = new EmbeddedChannel();
    owner.accepted(secondChannel, reverseHello());
    owner.fireEvent(new ReverseConnectChannelOwner.Event.PendingAcceptTimedOut(0L, secondChannel));

    assertTrue(secondChannel.isOpen());

    CompletableFuture<Channel> secondConnect = owner.connect(newApplicationContext());
    handshakeStarter.succeed(secondChannel);

    assertSame(secondChannel, secondConnect.get(1, TimeUnit.SECONDS));
  }

  @Test
  void channelInactiveDuringHandshakeFailsWaiterAndIgnoresLateHandshakeSuccess() throws Exception {

    CompletableFuture<Channel> firstConnect = owner.connect(newApplicationContext());
    var firstChannel = new EmbeddedChannel();
    owner.accepted(firstChannel, reverseHello());

    firstChannel.close();
    handshakeStarter.succeed(firstChannel);

    assertUaStatus(StatusCodes.Bad_ConnectionClosed, firstConnect);

    CompletableFuture<Channel> secondConnect = owner.connect(newApplicationContext());
    var secondChannel = new EmbeddedChannel();
    owner.accepted(secondChannel, reverseHello());
    handshakeStarter.succeed(secondChannel);

    assertSame(secondChannel, secondConnect.get(1, TimeUnit.SECONDS));
  }

  @Test
  void channelInactiveAfterHandshakeSuccessClearsActiveChannelAndBoundsNextWaiter()
      throws Exception {

    CompletableFuture<Channel> connectFuture = owner.connect(newApplicationContext());
    var channel = new EmbeddedChannel();
    owner.accepted(channel, reverseHello());
    handshakeStarter.succeed(channel);

    assertSame(channel, connectFuture.get(1, TimeUnit.SECONDS));

    channel.close();

    assertEquals(ReverseConnectChannelOwner.State.Armed, owner.getState());

    CompletableFuture<Channel> getChannelFuture = owner.getChannel();
    scheduler.runAll();

    assertUaStatus(StatusCodes.Bad_Timeout, getChannelFuture);
  }

  @Test
  void staleHandshakeAndCloseCallbacksFromPreviousLifecycleAreIgnored() throws Exception {
    CompletableFuture<Channel> firstConnect = owner.connect(newApplicationContext());
    var firstChannel = new EmbeddedChannel();
    owner.accepted(firstChannel, reverseHello());

    assertEquals(Unit.VALUE, owner.disconnect().get(1, TimeUnit.SECONDS));
    assertUaStatus(StatusCodes.Bad_ConnectionClosed, firstConnect);

    CompletableFuture<Channel> secondConnect = owner.connect(newApplicationContext());
    var secondChannel = new EmbeddedChannel();
    owner.accepted(secondChannel, reverseHello());

    handshakeStarter.succeed(firstChannel);
    owner.fireEvent(new ReverseConnectChannelOwner.Event.ChannelInactive(0L, firstChannel));

    assertFalse(secondConnect.isDone());

    handshakeStarter.succeed(secondChannel);

    assertSame(secondChannel, secondConnect.get(1, TimeUnit.SECONDS));
    assertSame(secondChannel, owner.getActiveChannel());
  }

  private ReverseConnectChannelOwner newOwner(
      TestHandshakeStarter handshakeStarter, ManualScheduler scheduler) {

    var config =
        new ReverseConnectChannelOwner.ChannelOwnerConfig(
            newUascConfig(), Set.of(SERVER_URI), 5_000L, 1_000L);

    return new ReverseConnectChannelOwner(
        config,
        newResponseHandler(),
        requestId::getAndIncrement,
        Runnable::run,
        scheduler,
        handshakeStarter);
  }

  private UascClientConfig newUascConfig() {
    return new UascClientConfig() {
      @Override
      public UInteger getAcknowledgeTimeout() {
        return uint(5_000);
      }

      @Override
      public UInteger getChannelLifetime() {
        return uint(60_000);
      }

      @Override
      public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
      }
    };
  }

  private static UascResponseHandler newResponseHandler() {
    return new UascResponseHandler() {
      @Override
      public void handleResponse(long requestId, UaResponseMessageType msg) {}

      @Override
      public void handleSendFailure(long requestId, UaException ex) {}

      @Override
      public void handleReceiveFailure(long requestId, UaException ex) {}

      @Override
      public void handleChannelError(UaException ex) {}

      @Override
      public void handleChannelInactive() {}
    };
  }

  private static ReverseHelloMessage reverseHello() {
    return new ReverseHelloMessage(SERVER_URI, ENDPOINT_URL);
  }

  private static ClientApplicationContext newApplicationContext() {
    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return new EndpointDescription(
            ENDPOINT_URL,
            null,
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            TransportProfile.TCP_UASC_UABINARY.getUri(),
            ubyte(0));
      }

      @Override
      public Optional<KeyPair> getKeyPair() {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate> getCertificate() {
        return Optional.empty();
      }

      @Override
      public Optional<X509Certificate[]> getCertificateChain() {
        return Optional.empty();
      }

      @Override
      public CertificateValidator getCertificateValidator() {
        return new CertificateValidator.InsecureCertificateValidator();
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public UInteger getRequestTimeout() {
        return uint(5_000);
      }
    };
  }

  private static void assertUaStatus(long expectedStatus, CompletableFuture<?> future) {
    try {
      future.get(1, TimeUnit.SECONDS);
      fail("Expected UaException");
    } catch (ExecutionException e) {
      UaException uaException = assertInstanceOf(UaException.class, e.getCause());
      assertEquals(expectedStatus, uaException.getStatusCode().value());
    } catch (Exception e) {
      fail("Expected UaException", e);
    }
  }

  private static final class TestHandshakeStarter
      implements ReverseConnectChannelOwner.HandshakeStarter {

    private final IdentityHashMap<Channel, CompletableFuture<ClientSecureChannel>> futures =
        new IdentityHashMap<>();
    private final List<ClientApplicationContext> applications = new ArrayList<>();

    @Override
    public CompletableFuture<ClientSecureChannel> start(
        Channel channel,
        ReverseHelloMessage reverseHello,
        UascClientConfig config,
        ClientApplicationContext application,
        UascResponseHandler responseHandler,
        java.util.function.Supplier<Long> requestIdSupplier,
        Set<String> allowedServerUris,
        long reverseHelloTimeoutMs) {

      var future = new CompletableFuture<ClientSecureChannel>();

      futures.put(channel, future);
      applications.add(application);

      return future;
    }

    void succeed(Channel channel) {
      var secureChannel = new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
      secureChannel.setChannel(channel);

      future(channel).complete(secureChannel);
    }

    void fail(Channel channel, Throwable failure) {
      future(channel).completeExceptionally(failure);
    }

    int starts() {
      return futures.size();
    }

    List<ClientApplicationContext> applications() {
      return applications;
    }

    private CompletableFuture<ClientSecureChannel> future(Channel channel) {
      CompletableFuture<ClientSecureChannel> future = futures.get(channel);

      if (future == null) {
        org.junit.jupiter.api.Assertions.fail("No handshake started for channel " + channel);
      }

      return future;
    }
  }

  private static final class ManualScheduler implements ReverseConnectChannelOwner.Scheduler {

    private final ArrayDeque<ScheduledTask> tasks = new ArrayDeque<>();

    @Override
    public ReverseConnectChannelOwner.Cancellable schedule(
        Runnable task, long delay, TimeUnit unit) {

      var scheduledTask = new ScheduledTask(task);
      tasks.add(scheduledTask);

      return () -> scheduledTask.cancelled = true;
    }

    void runNext() {
      ScheduledTask task = tasks.remove();

      if (!task.cancelled) {
        task.command.run();
      }
    }

    void runAll() {
      while (!tasks.isEmpty()) {
        runNext();
      }
    }

    private static final class ScheduledTask {

      private boolean cancelled;
      private final Runnable command;

      private ScheduledTask(Runnable command) {
        this.command = command;
      }
    }
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that SecureChannel renewal is single-flight: the AEAD sequence-space threshold and the
 * 75%-lifetime timer share one in-flight guard, and the lifetime timer is rescheduled (not
 * duplicated) on each token install.
 *
 * <p>A second OpenSecureChannel Renew sent while the first response is still outstanding would
 * overwrite the channel's single ephemeral-key/nonce slot and tear the channel down, so only one
 * Renew may ever be in flight. The renewal-decision logic exercised here is policy-independent, so
 * these tests drive a {@code SecurityPolicy.None} channel to keep the OpenSecureChannel encode
 * deterministic and free of crypto setup.
 */
class UascClientMessageHandlerRenewalTest {

  private static final ChannelParameters CHANNEL_PARAMETERS =
      new ChannelParameters(65535, 65535, 8196, 0, 65535, 65535, 8196, 0);

  private final HashedWheelTimer wheelTimer = new HashedWheelTimer();

  @AfterEach
  void stopTimer() {
    wheelTimer.stop();
  }

  // The threshold path sends a Renew (setting renewalRequestPending); the lifetime timer firing
  // before the response arrives must be suppressed by that flag, so exactly one Renew is written.
  // Without the shared guard the timer would send a second overlapping Renew that clobbers the
  // single ephemeral-key/nonce slot and kills the channel.
  @Test
  void timerRenewalIsSuppressedWhileThresholdRenewalIsInFlight() throws Exception {
    Fixture fixture = new Fixture();

    invokeRenew(fixture.handler, fixture.ctx);
    fixture.channel.runPendingTasks();
    assertEquals(
        1, drainOutbound(fixture.channel), "the threshold-triggered Renew must be written once");
    assertTrue(
        getRenewalRequestPending(fixture.handler), "the first Renew must mark a renewal in flight");

    // Simulate the still-pending lifetime timer firing before the first response is delivered.
    invokeRenew(fixture.handler, fixture.ctx);
    fixture.channel.runPendingTasks();

    assertEquals(
        0, drainOutbound(fixture.channel), "no second Renew may be sent while one is in flight");
  }

  // installSecurityToken arms the 75%-lifetime timer. A threshold-triggered renewal installs a new
  // token without firing the prior timer, so each install must cancel the previous renewFuture
  // before scheduling a new one; otherwise orphaned timer chains accumulate and later fire
  // overlapping Renews into the channel.
  @Test
  void installSecurityTokenCancelsPreviousRenewalTimerBeforeRescheduling() throws Exception {
    Fixture fixture = new Fixture();

    invokeInstallSecurityToken(fixture.handler, fixture.ctx, installResponse(2L));
    ScheduledFuture<?> firstTimer = getRenewFuture(fixture.handler);
    assertNotNull(firstTimer, "a lifetime renewal timer must be scheduled");

    invokeInstallSecurityToken(fixture.handler, fixture.ctx, installResponse(3L));
    ScheduledFuture<?> secondTimer = getRenewFuture(fixture.handler);

    assertNotSame(firstTimer, secondTimer, "a fresh renewal timer must be scheduled on reinstall");
    assertTrue(firstTimer.isCancelled(), "the previous renewal timer must be cancelled");
    assertFalse(secondTimer.isCancelled(), "exactly one armed renewal timer must remain");
  }

  // After the channel closes, a stale timer firing (or any other initiator) must not write an OPN
  // into a dead context; the liveness guard short-circuits the renewal.
  @Test
  void renewalIsSuppressedAfterChannelClose() throws Exception {
    Fixture fixture = new Fixture();

    fixture.channel.close().sync();
    assertFalse(fixture.channel.isActive(), "channel must be closed before the renewal attempt");

    invokeRenew(fixture.handler, fixture.ctx);

    assertEquals(
        0, drainOutbound(fixture.channel), "no OPN may be written after the channel is closed");
    assertFalse(
        getRenewalRequestPending(fixture.handler), "a suppressed renewal must not latch the guard");
  }

  /**
   * Wires a handler into an {@link EmbeddedChannel}, lets {@code handlerAdded} run (which sends the
   * initial Issue OpenSecureChannel that is drained away), then injects a {@code None}-policy
   * {@link ClientSecureChannel} carrying an installed token so renewal can be exercised and
   * observed as an outbound write without crypto setup.
   */
  private final class Fixture {
    final EmbeddedChannel channel = new EmbeddedChannel();
    final UascClientMessageHandler handler;
    final ChannelHandlerContext ctx;

    Fixture() throws Exception {
      AtomicLong requestId = new AtomicLong(1L);

      handler =
          new UascClientMessageHandler(
              stubConfig(),
              stubApplicationContext(),
              requestId::getAndIncrement,
              new CompletableFuture<>(),
              List.of(),
              CHANNEL_PARAMETERS);

      // Adding the handler triggers handlerAdded, which sends the initial Issue OPN; drain it so
      // the
      // renewal-write assertions start from a clean outbound buffer.
      channel.pipeline().addLast(handler);
      channel.runPendingTasks();
      drainOutbound(channel);

      ctx = channel.pipeline().context(handler);

      ClientSecureChannel secureChannel =
          new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);
      secureChannel.setChannelId(1L);
      secureChannel.setChannel(channel);

      ChannelSecurityToken token =
          new ChannelSecurityToken(uint(1), uint(1), DateTime.now(), uint(3600000));
      secureChannel.setChannelSecurity(new ChannelSecurity(null, token));

      Field secureChannelField = UascClientMessageHandler.class.getDeclaredField("secureChannel");
      secureChannelField.setAccessible(true);
      secureChannelField.set(handler, secureChannel);
    }
  }

  private static OpenSecureChannelResponse installResponse(long tokenId) {
    ChannelSecurityToken token =
        new ChannelSecurityToken(uint(1), uint(tokenId), DateTime.now(), uint(3600000));

    return new OpenSecureChannelResponse(
        new ResponseHeader(DateTime.now(), uint(0), StatusCode.GOOD, null, null, null),
        uint(0),
        token,
        ByteString.NULL_VALUE);
  }

  private static void invokeRenew(UascClientMessageHandler handler, ChannelHandlerContext ctx)
      throws Exception {
    Method method =
        UascClientMessageHandler.class.getDeclaredMethod(
            "renewSecureChannel", ChannelHandlerContext.class);
    method.setAccessible(true);
    method.invoke(handler, ctx);
  }

  private static void invokeInstallSecurityToken(
      UascClientMessageHandler handler,
      ChannelHandlerContext ctx,
      OpenSecureChannelResponse response)
      throws Exception {
    Method method =
        UascClientMessageHandler.class.getDeclaredMethod(
            "installSecurityToken", ChannelHandlerContext.class, OpenSecureChannelResponse.class);
    method.setAccessible(true);
    method.invoke(handler, ctx, response);
  }

  private static boolean getRenewalRequestPending(UascClientMessageHandler handler)
      throws Exception {
    Field field = UascClientMessageHandler.class.getDeclaredField("renewalRequestPending");
    field.setAccessible(true);
    return field.getBoolean(handler);
  }

  private static ScheduledFuture<?> getRenewFuture(UascClientMessageHandler handler)
      throws Exception {
    Field field = UascClientMessageHandler.class.getDeclaredField("renewFuture");
    field.setAccessible(true);
    return (ScheduledFuture<?>) field.get(handler);
  }

  private static int drainOutbound(EmbeddedChannel channel) {
    int count = 0;
    ByteBuf buffer;
    while ((buffer = channel.readOutbound()) != null) {
      count++;
      buffer.release();
    }
    return count;
  }

  private UascClientConfig stubConfig() {
    return new UascClientConfig() {
      @Override
      public UInteger getAcknowledgeTimeout() {
        return uint(60000);
      }

      @Override
      public UInteger getChannelLifetime() {
        return uint(3600000);
      }

      @Override
      public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
      }
    };
  }

  private static ClientApplicationContext stubApplicationContext() {
    EndpointDescription endpoint =
        new EndpointDescription(
            "opc.tcp://localhost:0/test",
            new ApplicationDescription(
                "urn:eclipse:milo:test",
                "urn:eclipse:milo:test",
                LocalizedText.english("test"),
                ApplicationType.Client,
                null,
                null,
                null),
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            null,
            ubyte(0));

    // getCertificateValidator is not exercised by the renewal paths under test; the validator is a
    // no-op so the stub stays minimal.
    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return endpoint;
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
        return (certificateChain, applicationUri, validHostnames) -> {};
      }

      @Override
      public EncodingContext getEncodingContext() {
        return new DefaultEncodingContext();
      }

      @Override
      public UInteger getRequestTimeout() {
        return uint(60000);
      }
    };
  }
}

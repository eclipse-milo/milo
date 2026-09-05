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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.ReferenceCountUtil;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;

/**
 * Retained partial responses must be released when their owning handler can no longer finish them.
 */
class UascClientChunkLifecycleTest {

  private static final ChannelParameters CHANNEL_PARAMETERS =
      new ChannelParameters(65535, 65535, 8196, 0, 65535, 65535, 8196, 0);

  @org.junit.jupiter.params.ParameterizedTest
  @org.junit.jupiter.params.provider.EnumSource(Cleanup.class)
  void partialResponseIsReleasedAtEveryTerminationBoundary(Cleanup cleanup) throws Exception {
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    ByteBuf chunk = PooledByteBufAllocator.DEFAULT.directBuffer();
    try {
      var handler =
          new UascClientMessageHandler(
              config(wheelTimer),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              new CompletableFuture<>(),
              List.of(),
              CHANNEL_PARAMETERS);
      channel.pipeline().addLast(handler);
      channel.runPendingTasks();
      Object outbound;
      while ((outbound = channel.readOutbound()) != null) ReferenceCountUtil.release(outbound);

      chunk.writeMediumLE(MessageType.toMediumInt(MessageType.SecureMessage));
      chunk.writeByte('C').writeIntLE(112).writeIntLE(0).writeZero(100);
      channel.writeInbound(chunk);
      assertEquals(1, chunk.refCnt(), "the partial response must be retained before termination");
      switch (cleanup) {
        case CLOSE -> channel.close().syncUninterruptibly();
        case REMOVE -> channel.pipeline().remove(handler);
        case EXCEPTION ->
            channel.pipeline().fireExceptionCaught(new RuntimeException("test failure"));
      }
      channel.runPendingTasks();
      assertEquals(0, chunk.refCnt(), "termination must release the retained pooled response");
      channel.close().syncUninterruptibly();
      assertEquals(0, chunk.refCnt(), "a second cleanup boundary must not double-release");
    } finally {
      channel.finishAndReleaseAll();
      if (chunk.refCnt() > 0) chunk.release();
      wheelTimer.stop();
    }
  }

  private enum Cleanup {
    CLOSE,
    REMOVE,
    EXCEPTION
  }

  static UascClientConfig config(HashedWheelTimer wheelTimer) {
    return new UascClientConfig() {
      public UInteger getAcknowledgeTimeout() {
        return uint(60000);
      }

      public UInteger getChannelLifetime() {
        return uint(3600000);
      }

      public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
      }
    };
  }

  static ClientApplicationContext application() {
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
    return new ClientApplicationContext() {
      public EndpointDescription getEndpoint() {
        return endpoint;
      }

      public Optional<CertificateIdentity> getCertificateIdentity(SecurityPolicyProfile p) {
        return Optional.empty();
      }

      public CertificateValidator getCertificateValidator() {
        return (chain, uri, hosts) -> {};
      }

      public EncodingContext getEncodingContext() {
        return new DefaultEncodingContext();
      }

      public UInteger getRequestTimeout() {
        return uint(60000);
      }
    };
  }
}

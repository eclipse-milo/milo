/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.uasc.UascServerHelloHandler;
import org.junit.jupiter.api.Test;

class OpcTcpServerChannelInitializerTest {

  @Test
  void passiveInitializerAddsRateLimitingHelloHandlerCustomizerAndTracking() {
    Set<Channel> childChannelReferences = Collections.synchronizedSet(new HashSet<>());
    EmbeddedChannel channel = new EmbeddedChannel();

    try {
      OpcTcpServerChannelInitializer.initializePassiveChannel(
          channel, newServerConfig(), newServerApplicationContext(), childChannelReferences);

      assertNotNull(channel.pipeline().get(RateLimitingHandler.class));
      assertServerPipelineInitialized(channel);
      assertTrue(childChannelReferences.contains(channel));

      channel.close().syncUninterruptibly();
      channel.runPendingTasks();

      assertFalse(childChannelReferences.contains(channel));
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  @Test
  void reverseInitializerSkipsInboundRateLimiting() {
    EmbeddedChannel channel = new EmbeddedChannel();

    try {
      OpcTcpServerChannelInitializer.initializeReverseChannel(
          channel, newServerConfig(), newServerApplicationContext());

      assertNull(channel.pipeline().get(RateLimitingHandler.class));
      assertServerPipelineInitialized(channel);
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  @Test
  void reverseInitializerSchedulesHelloDeadlineOnActiveChannel() {
    EmbeddedChannel channel = new EmbeddedChannel();

    try {
      OpcTcpServerChannelInitializer.initializeReverseChannel(
          channel, newServerConfig(1), newServerApplicationContext());

      channel.advanceTimeBy(1, TimeUnit.MILLISECONDS);
      channel.runScheduledPendingTasks();

      assertFalse(channel.isOpen());
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  private static OpcTcpServerTransportConfig newServerConfig() {
    return newServerConfig(10_000);
  }

  private static OpcTcpServerTransportConfig newServerConfig(int helloDeadlineMs) {
    return OpcTcpServerTransportConfig.newBuilder()
        .setHelloDeadline(uint(helloDeadlineMs))
        .setChannelPipelineCustomizer(pipeline -> pipeline.addLast(new MarkerHandler()))
        .build();
  }

  private static void assertServerPipelineInitialized(EmbeddedChannel channel) {
    assertNotNull(channel.pipeline().get(UascServerHelloHandler.class));
    assertNotNull(channel.pipeline().get(MarkerHandler.class));
  }

  private static ServerApplicationContext newServerApplicationContext() {
    return new ServerApplicationContext() {

      private final AtomicLong secureChannelId = new AtomicLong(0L);
      private final AtomicLong secureChannelTokenId = new AtomicLong(0L);

      @Override
      public List<EndpointDescription> getEndpointDescriptions() {
        return List.of(newEndpointDescription("opc.tcp://localhost:12685/milo"));
      }

      @Override
      public CertificateManager getCertificateManager() {
        return EmptyCertificateManager.INSTANCE;
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public Long getNextSecureChannelId() {
        return secureChannelId.getAndIncrement();
      }

      @Override
      public Long getNextSecureChannelTokenId() {
        return secureChannelTokenId.getAndIncrement();
      }

      @Override
      public CompletableFuture<UaResponseMessageType> handleServiceRequest(
          ServiceRequestContext context, UaRequestMessageType requestMessage) {

        return CompletableFuture.failedFuture(new UnsupportedOperationException());
      }
    };
  }

  private static EndpointDescription newEndpointDescription(String endpointUrl) {
    return new EndpointDescription(
        endpointUrl,
        new ApplicationDescription(
            "urn:server",
            "productUri",
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {endpointUrl}),
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static final class MarkerHandler extends ChannelInboundHandlerAdapter {}

  private enum EmptyCertificateManager implements CertificateManager {
    INSTANCE;

    private final CertificateQuarantine certificateQuarantine = new MemoryCertificateQuarantine();

    @Override
    public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
      return Optional.empty();
    }

    @Override
    public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
      return Optional.empty();
    }

    @Override
    public List<CertificateGroup> getCertificateGroups() {
      return List.of();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      return certificateQuarantine;
    }
  }
}

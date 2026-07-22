/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class UascServerSymmetricHandlerTest {

  // A half-dead AEAD channel that can decode requests but can no longer frame any response must not
  // silently swallow the ServiceFault and black-hole the client. The original code logged the
  // failed fault encode and returned, leaving a channel that receives requests but can never reply
  // (the real-world trigger is an AEAD sequence stream that can no longer produce a unique nonce).
  // The server has to close the channel with an Error so the client stops waiting for a response
  // that will never arrive.
  @Test
  void closesChannelWithErrorWhenServiceFaultCannotBeEncoded() throws Exception {
    long tokenId = 0x42L;

    ServerSecureChannel secureChannel = new ServerSecureChannel();
    secureChannel.setChannelId(1L);
    secureChannel.setSecurityPolicy(SecurityPolicy.ECC_nistP256_AesGcm);
    secureChannel.setMessageSecurityMode(MessageSecurityMode.SignAndEncrypt);

    // Certificates only need to be present so symmetric signing/encryption are enabled and the AEAD
    // chunk path runs; the symmetric codecs protect chunks with the installed keys, not the certs.
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("UascServerSymmetricHandlerTest")
            .setApplicationUri("urn:eclipse:milo:test")
            .build();
    secureChannel.setKeyPair(keyPair);
    secureChannel.setLocalCertificate(certificate);
    secureChannel.setRemoteCertificate(certificate.getEncoded());

    ChannelSecurity.SecurityKeys keys =
        ChannelSecurity.createAeadKeyPair(new byte[16], new byte[12], new byte[16], new byte[12]);
    ChannelSecurityToken token =
        new ChannelSecurityToken(uint(1), uint(tokenId), DateTime.now(), uint(3600000));
    secureChannel.setChannelSecurity(new ChannelSecurity(keys, token));

    // Drive the server-direction nonce state so the next reserve (LastSequenceNumber 0, the first
    // value the encoder will use) is rejected as reuse. Every outbound encode then fails, so both
    // the service response and the ServiceFault fall through to the close-with-Error path - the
    // observable outcome of an exhausted outbound stream that can no longer frame anything.
    exhaustSendNonceState(keys, tokenId);

    ChannelParameters channelParameters =
        new ChannelParameters(8192, 8192, 8192, 0, 8192, 8192, 8192, 0);

    var handler =
        new UascServerSymmetricHandler(
            stubConfig(),
            stubApplicationContext(),
            TransportProfile.TCP_UASC_UABINARY,
            channelParameters,
            new ChunkEncoder(channelParameters),
            new ChunkDecoder(channelParameters, EncodingLimits.DEFAULT),
            secureChannel);

    EmbeddedChannel channel = new EmbeddedChannel(handler);

    UascServiceResponse response =
        new UascServiceResponse(
            new ServiceFault(
                new ResponseHeader(DateTime.now(), uint(0), StatusCode.GOOD, null, null, null)),
            1L);

    // Write through the pipeline rather than EmbeddedChannel.writeOutbound: the close-with-Error
    // path closes the channel synchronously on the embedded event loop, and writeOutbound would
    // rethrow the resulting ClosedChannelException from the codec's trailing empty-buffer flush
    // (benign here because a real channel closes asynchronously after the flush completes).
    channel.pipeline().writeAndFlush(response);
    channel.runPendingTasks();

    ByteBuf errorBuffer = drainErrorMessage(channel);
    assertNotNull(errorBuffer, "server must emit an Error message instead of swallowing the fault");

    try {
      ErrorMessage errorMessage = TcpMessageDecoder.decodeError(errorBuffer);
      assertTrue(errorMessage.getError().isBad());
    } finally {
      errorBuffer.release();
    }

    assertFalse(channel.isOpen(), "server must close the half-dead channel");
  }

  /**
   * Drains the channel's outbound buffers and returns the first one framed as a UASC {@code ERR}
   * message, releasing any other (for example the codec's empty output buffer). Returns {@code
   * null} if no Error message was emitted.
   */
  private static @Nullable ByteBuf drainErrorMessage(EmbeddedChannel channel) {
    ByteBuf candidate;
    while ((candidate = channel.readOutbound()) != null) {
      if (candidate.readableBytes() >= 3
          && candidate.getByte(candidate.readerIndex()) == 'E'
          && candidate.getByte(candidate.readerIndex() + 1) == 'R'
          && candidate.getByte(candidate.readerIndex() + 2) == 'R') {
        return candidate;
      }

      candidate.release();
    }

    return null;
  }

  /**
   * Reflectively advances the server-direction AEAD nonce state so that the next reserve at
   * LastSequenceNumber 0 - the first value the encoder uses - is rejected as nonce reuse, modelling
   * a directional stream that has already consumed that value and cannot frame another chunk.
   */
  private static void exhaustSendNonceState(ChannelSecurity.SecurityKeys keys, long tokenId)
      throws Exception {

    ChannelSecurity.SecretKeys serverKeys = keys.getServerKeys();

    Field nonceStateField = ChannelSecurity.SecretKeys.class.getDeclaredField("aeadNonceState");
    nonceStateField.setAccessible(true);
    Object nonceState = nonceStateField.get(serverKeys);

    setLongField(nonceState, "tokenId", tokenId);
    setLongField(nonceState, "firstLastSequenceNumber", 0L);
    setLongField(nonceState, "highestLastSequenceNumber", 0L);
  }

  private static void setLongField(Object target, String name, long value) throws Exception {
    Field field = target.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.setLong(target, value);
  }

  private static UascServerConfig stubConfig() {
    return new UascServerConfig() {
      @Override
      public ExecutorService getExecutor() {
        return null;
      }

      @Override
      public UInteger getHelloDeadline() {
        return uint(0);
      }

      @Override
      public UInteger getMinimumSecureChannelLifetime() {
        return uint(0);
      }

      @Override
      public UInteger getMaximumSecureChannelLifetime() {
        return uint(0);
      }
    };
  }

  private static ServerApplicationContext stubApplicationContext() {
    return new ServerApplicationContext() {
      @Override
      public List<EndpointDescription> getEndpointDescriptions() {
        return List.of();
      }

      @Override
      public CertificateManager getCertificateManager() {
        return null;
      }

      @Override
      public EncodingContext getEncodingContext() {
        return new DefaultEncodingContext();
      }

      @Override
      public Long getNextSecureChannelId() {
        return 1L;
      }

      @Override
      public Long getNextSecureChannelTokenId() {
        return 1L;
      }

      @Override
      public CompletableFuture<UaResponseMessageType> handleServiceRequest(
          ServiceRequestContext context, UaRequestMessageType requestMessage) {
        return new CompletableFuture<>();
      }
    };
  }
}

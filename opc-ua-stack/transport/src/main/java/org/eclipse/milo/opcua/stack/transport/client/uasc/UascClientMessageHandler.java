/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.Timeout;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder.DecodedMessage;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder.EncodedMessage;
import org.eclipse.milo.opcua.stack.core.channel.MessageAbortException;
import org.eclipse.milo.opcua.stack.core.channel.MessageDecodeException;
import org.eclipse.milo.opcua.stack.core.channel.MessageEncodeException;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UascClientMessageHandler extends ByteToMessageCodec<UascRequest> {

  private static final long PROTOCOL_VERSION = 0L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicReference<AsymmetricSecurityHeader> headerRef = new AtomicReference<>();

  private List<ByteBuf> chunkBuffers = new ArrayList<>();

  private ScheduledFuture<?> renewFuture;
  private Timeout secureChannelTimeout;

  private ClientSecureChannel secureChannel;

  private final OpcUaBinaryDecoder binaryDecoder;
  private final OpcUaBinaryEncoder binaryEncoder;
  private final ChunkDecoder chunkDecoder;
  private final ChunkEncoder chunkEncoder;

  private final UascClientConfig config;
  private final ClientApplicationContext application;
  private final Supplier<Long> requestIdSupplier;
  private final CompletableFuture<ClientSecureChannel> handshakeFuture;
  private final ChannelParameters channelParameters;

  public UascClientMessageHandler(
      UascClientConfig config,
      ClientApplicationContext application,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshakeFuture,
      List<UaRequestMessageType> awaitingHandshake,
      ChannelParameters channelParameters) {

    this.config = config;
    this.application = application;
    this.requestIdSupplier = requestIdSupplier;
    this.handshakeFuture = handshakeFuture;
    this.channelParameters = channelParameters;

    binaryDecoder = new OpcUaBinaryDecoder(application.getEncodingContext());
    binaryEncoder = new OpcUaBinaryEncoder(application.getEncodingContext());

    chunkDecoder =
        new ChunkDecoder(channelParameters, application.getEncodingContext().getEncodingLimits());
    chunkEncoder = new ChunkEncoder(channelParameters);

    handshakeFuture.thenAccept(
        sc -> {
          Channel channel = sc.getChannel();

          channel
              .eventLoop()
              .execute(
                  () -> {
                    logger.debug(
                        "{} message(s) queued before handshake completed; sending now.",
                        awaitingHandshake.size());

                    awaitingHandshake.forEach(channel::writeAndFlush);
                    awaitingHandshake.clear();
                  });
        });
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (renewFuture != null) {
      renewFuture.cancel(false);
    }

    UaException exception = new UaException(StatusCodes.Bad_ConnectionClosed, "connection closed");

    handshakeFuture.completeExceptionally(exception);

    super.channelInactive(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.error(
        "[remote={}] Exception caught: {}",
        ctx.channel().remoteAddress(),
        cause.getMessage(),
        cause);

    chunkBuffers.forEach(ReferenceCountUtil::safeRelease);
    chunkBuffers.clear();

    // If the handshake hasn't completed yet this cause will be more
    // accurate than the generic "connection closed" exception that
    // channelInactive() will use.
    handshakeFuture.completeExceptionally(cause);

    ctx.close();
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    secureChannel = newSecureChannel(application);
    secureChannel.setChannel(ctx.channel());

    SecurityTokenRequestType requestType =
        secureChannel.getChannelId() == 0
            ? SecurityTokenRequestType.Issue
            : SecurityTokenRequestType.Renew;

    secureChannelTimeout =
        config
            .getWheelTimer()
            .newTimeout(
                timeout -> {
                  if (!timeout.isCancelled()) {
                    handshakeFuture.completeExceptionally(
                        new UaException(
                            StatusCodes.Bad_Timeout, "timed out waiting for secure channel"));
                    ctx.close();
                  }
                },
                application.getRequestTimeout().longValue(),
                TimeUnit.MILLISECONDS);

    logger.debug("OpenSecureChannel timeout scheduled for +{}ms", application.getRequestTimeout());

    sendOpenSecureChannelRequest(ctx, requestType);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
    if (evt instanceof CloseSecureChannelRequest) {
      sendCloseSecureChannelRequest(ctx, (CloseSecureChannelRequest) evt);
    }
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, UascRequest request, ByteBuf buffer)
      throws Exception {
    ByteBuf messageBuffer = BufferUtil.pooledBuffer();

    try {
      binaryEncoder.setBuffer(messageBuffer);
      binaryEncoder.encodeMessage(null, request.getRequestMessage());

      checkMessageSize(messageBuffer);

      EncodedMessage encodedMessage =
          chunkEncoder.encodeSymmetric(
              secureChannel, request.getRequestId(), messageBuffer, MessageType.SecureMessage);

      List<ByteBuf> messageChunks = encodedMessage.getMessageChunks();

      CompositeByteBuf chunkComposite = BufferUtil.compositeBuffer();

      for (ByteBuf chunk : messageChunks) {
        chunkComposite.addComponent(chunk);
        chunkComposite.writerIndex(chunkComposite.writerIndex() + chunk.readableBytes());
      }

      ctx.writeAndFlush(chunkComposite, ctx.voidPromise());
    } catch (MessageEncodeException e) {
      logger.error("Error encoding {}: {}", request, e.getMessage(), e);

      UaException responseException = UaException.extract(e).orElseGet(() -> new UaException(e));

      UascResponse response = UascResponse.failure(request.getRequestId(), responseException);

      ctx.fireUserEventTriggered(response);

      // failure during symmetric encoding is fatal
      ctx.close();
    } catch (UaSerializationException e) {
      logger.error("Error serializing {}: {}", request, e.getMessage(), e);

      UaException responseException = UaException.extract(e).orElseGet(() -> new UaException(e));

      UascResponse response = UascResponse.failure(request.getRequestId(), responseException);

      ctx.fireUserEventTriggered(response);
    } finally {
      messageBuffer.release();
    }
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
      throws Exception {
    if (buffer.readableBytes() >= 8) {
      int messageLength = getMessageLength(buffer, channelParameters.getLocalReceiveBufferSize());

      if (buffer.readableBytes() >= messageLength) {
        MessageType messageType =
            MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));

        switch (messageType) {
          case OpenSecureChannel:
            onOpenSecureChannel(ctx, buffer.readSlice(messageLength));
            break;

          case SecureMessage:
            onSecureMessage(ctx, buffer.readSlice(messageLength), out);
            break;

          case Error:
            onError(ctx, buffer.readSlice(messageLength));
            break;

          default:
            throw new UaException(
                StatusCodes.Bad_TcpMessageTypeInvalid, "unexpected MessageType: " + messageType);
        }
      }
    }
  }

  private void onSecureMessage(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
      throws UaException {
    buffer.skipBytes(3 + 1 + 4); // skip messageType, chunkType, messageSize

    long secureChannelId = buffer.readUnsignedIntLE();
    if (secureChannelId != secureChannel.getChannelId()) {
      throw new UaException(
          StatusCodes.Bad_SecureChannelIdInvalid, "invalid secure channel id: " + secureChannelId);
    }

    if (accumulateChunk(buffer)) {
      final List<ByteBuf> buffersToDecode = chunkBuffers;
      chunkBuffers = new ArrayList<>(getMaxChunkCount());

      ByteBuf messageBuffer = null;

      try {
        DecodedMessage decodedMessage =
            chunkDecoder.decodeSymmetric(secureChannel, buffersToDecode);

        messageBuffer = decodedMessage.getMessage();

        binaryDecoder.setBuffer(messageBuffer);
        UaMessageType message = binaryDecoder.decodeMessage(null);

        if (message instanceof ServiceFault) {
          ServiceFault serviceFault = (ServiceFault) message;

          UascResponse response =
              UascResponse.failure(
                  decodedMessage.getRequestId(), new UaServiceFaultException(serviceFault));
          out.add(response);
        } else if (message instanceof UaResponseMessageType) {
          UascResponse response =
              UascResponse.success(decodedMessage.getRequestId(), (UaResponseMessageType) message);
          out.add(response);
        } else {
          UascResponse response =
              UascResponse.failure(
                  decodedMessage.getRequestId(),
                  new UaException(
                      StatusCodes.Bad_UnknownResponse, message.getClass().getSimpleName()));
          out.add(response);
        }
      } catch (MessageAbortException e) {
        logger.warn(
            "Received message abort chunk; error={}, reason={}", e.getStatusCode(), e.getMessage());

        out.add(
            UascResponse.failure(
                e.getRequestId(), new UaException(e.getStatusCode(), e.getMessage())));
      } catch (MessageDecodeException e) {
        logger.error("Error decoding symmetric message", e);

        ctx.close();
      } finally {
        if (messageBuffer != null) {
          messageBuffer.release();
        }
      }
    }
  }

  private void onOpenSecureChannel(ChannelHandlerContext ctx, ByteBuf buffer) throws UaException {
    if (secureChannelTimeout != null) {
      if (secureChannelTimeout.cancel()) {
        logger.debug("OpenSecureChannel timeout canceled");

        secureChannelTimeout = null;
      } else {
        logger.warn("timed out waiting for secure channel");

        handshakeFuture.completeExceptionally(
            new UaException(StatusCodes.Bad_Timeout, "timed out waiting for secure channel"));
        ctx.close();
        return;
      }
    }

    buffer.skipBytes(3 + 1 + 4 + 4); // skip messageType, chunkType, messageSize, secureChannelId

    AsymmetricSecurityHeader securityHeader =
        AsymmetricSecurityHeader.decode(
            buffer, application.getEncodingContext().getEncodingLimits());

    if (headerRef.compareAndSet(null, securityHeader)) {
      // first time we've received the header; validate and verify the server certificate
      CertificateValidator certificateValidator = application.getCertificateValidator();

      SecurityPolicy securityPolicy = SecurityPolicy.fromUri(securityHeader.getSecurityPolicyUri());

      if (securityPolicy != SecurityPolicy.None) {
        ByteString serverCertificateBytes = securityHeader.getSenderCertificate();

        List<X509Certificate> serverCertificateChain =
            CertificateUtil.decodeCertificates(serverCertificateBytes.bytesOrEmpty());

        certificateValidator.validateCertificateChain(serverCertificateChain, null, null);
      }
    } else {
      if (!securityHeader.equals(headerRef.get())) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "subsequent AsymmetricSecurityHeader did not match");
      }
    }

    if (accumulateChunk(buffer)) {
      final List<ByteBuf> buffersToDecode = chunkBuffers;
      chunkBuffers = new ArrayList<>(getMaxChunkCount());

      ByteBuf messageBuffer = null;

      try {
        DecodedMessage decodedMessage =
            chunkDecoder.decodeAsymmetric(secureChannel, buffersToDecode);

        messageBuffer = decodedMessage.getMessage();

        binaryDecoder.setBuffer(messageBuffer);
        UaResponseMessageType responseMessage =
            (UaResponseMessageType) binaryDecoder.decodeMessage(null);

        StatusCode serviceResult = responseMessage.getResponseHeader().getServiceResult();

        if (serviceResult.isGood()) {
          OpenSecureChannelResponse response = (OpenSecureChannelResponse) responseMessage;
          logger.debug("Received OpenSecureChannelResponse.");

          secureChannel.setChannelId(response.getSecurityToken().getChannelId().longValue());

          installSecurityToken(ctx, response);

          handshakeFuture.complete(secureChannel);
        } else {
          ServiceFault serviceFault =
              (responseMessage instanceof ServiceFault)
                  ? (ServiceFault) responseMessage
                  : new ServiceFault(responseMessage.getResponseHeader());

          handshakeFuture.completeExceptionally(new UaServiceFaultException(serviceFault));
          ctx.close();
        }
      } catch (MessageAbortException e) {
        logger.warn(
            "Received message abort chunk; error={}, reason={}", e.getStatusCode(), e.getMessage());
      } catch (MessageDecodeException e) {
        logger.error("Error decoding asymmetric message", e);

        handshakeFuture.completeExceptionally(e);

        ctx.close();
      } catch (Exception e) {
        logger.error("Error decoding OpenSecureChannelResponse", e);

        handshakeFuture.completeExceptionally(e);

        ctx.close();
      } finally {
        if (messageBuffer != null) {
          messageBuffer.release();
        }
      }
    }
  }

  private void installSecurityToken(ChannelHandlerContext ctx, OpenSecureChannelResponse response)
      throws UaException {

    if (response.getServerProtocolVersion().longValue() < PROTOCOL_VERSION) {
      throw new UaException(
          StatusCodes.Bad_ProtocolVersionUnsupported,
          "server protocol version unsupported: " + response.getServerProtocolVersion());
    }

    ChannelSecurity.SecurityKeys newKeys = null;
    ChannelSecurityToken newToken = response.getSecurityToken();

    if (secureChannel.isSymmetricSigningEnabled()) {
      ByteString serverNonce = response.getServerNonce();

      NonceUtil.validateNonce(serverNonce, secureChannel.getSecurityPolicy());

      secureChannel.setRemoteNonce(serverNonce);

      newKeys =
          ChannelSecurity.generateKeyPair(
              secureChannel, secureChannel.getLocalNonce(), secureChannel.getRemoteNonce());
    }

    ChannelSecurity oldSecrets = secureChannel.getChannelSecurity();
    ChannelSecurity.SecurityKeys oldKeys = oldSecrets != null ? oldSecrets.getCurrentKeys() : null;
    ChannelSecurityToken oldToken = oldSecrets != null ? oldSecrets.getCurrentToken() : null;

    secureChannel.setChannelSecurity(new ChannelSecurity(newKeys, newToken, oldKeys, oldToken));

    DateTime createdAt = response.getSecurityToken().getCreatedAt();
    long revisedLifetime = response.getSecurityToken().getRevisedLifetime().longValue();

    if (revisedLifetime > 0) {
      long renewAt = (long) (revisedLifetime * 0.75);
      renewFuture =
          ctx.executor()
              .schedule(
                  () -> sendOpenSecureChannelRequest(ctx, SecurityTokenRequestType.Renew),
                  renewAt,
                  TimeUnit.MILLISECONDS);
    } else {
      logger.warn("Server revised secure channel lifetime to 0; renewal will not occur.");
    }

    ctx.executor()
        .execute(
            () -> {
              // SecureChannel is ready; remove the acknowledge handler.
              if (ctx.pipeline().get(UascClientAcknowledgeHandler.class) != null) {
                ctx.pipeline().remove(UascClientAcknowledgeHandler.class);
              }
            });

    ChannelSecurity channelSecurity = secureChannel.getChannelSecurity();

    long currentTokenId = channelSecurity.getCurrentToken().getTokenId().longValue();

    long previousTokenId =
        channelSecurity.getPreviousToken().map(t -> t.getTokenId().longValue()).orElse(-1L);

    logger.debug(
        "SecureChannel id={}, currentTokenId={}, previousTokenId={}, lifetime={}ms, createdAt={}",
        secureChannel.getChannelId(),
        currentTokenId,
        previousTokenId,
        revisedLifetime,
        createdAt);
  }

  private void onError(ChannelHandlerContext ctx, ByteBuf buffer) {
    try {
      ErrorMessage errorMessage = TcpMessageDecoder.decodeError(buffer);
      StatusCode statusCode = errorMessage.getError();

      logger.error("[remote={}] errorMessage={}", ctx.channel().remoteAddress(), errorMessage);

      handshakeFuture.completeExceptionally(new UaException(statusCode, errorMessage.getReason()));

      ctx.fireUserEventTriggered(errorMessage);
    } catch (UaException e) {
      logger.error(
          "[remote={}] An exception occurred while decoding an error message: {}",
          ctx.channel().remoteAddress(),
          e.getMessage(),
          e);

      handshakeFuture.completeExceptionally(e);
    } finally {
      ctx.close();
    }
  }

  private boolean accumulateChunk(ByteBuf buffer) throws UaException {
    int maxChunkCount = getMaxChunkCount();
    int maxChunkSize = getMaxChunkSize();

    int chunkSize = buffer.readerIndex(0).readableBytes();

    if (chunkSize > maxChunkSize) {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTooLarge,
          String.format("max chunk size exceeded (%s)", maxChunkSize));
    }

    chunkBuffers.add(buffer.retain());

    if (maxChunkCount > 0 && chunkBuffers.size() > maxChunkCount) {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTooLarge,
          String.format("max chunk count exceeded (%s)", maxChunkCount));
    }

    char chunkType = (char) buffer.getByte(3);

    return (chunkType == 'A' || chunkType == 'F');
  }

  private void sendOpenSecureChannelRequest(
      ChannelHandlerContext ctx, SecurityTokenRequestType requestType) {
    ByteString clientNonce =
        secureChannel.isSymmetricSigningEnabled()
            ? NonceUtil.generateNonce(secureChannel.getSecurityPolicy())
            : ByteString.NULL_VALUE;

    secureChannel.setLocalNonce(clientNonce);

    var header =
        new RequestHeader(
            null, DateTime.now(), uint(0), uint(0), null, application.getRequestTimeout(), null);

    var request =
        new OpenSecureChannelRequest(
            header,
            uint(PROTOCOL_VERSION),
            requestType,
            secureChannel.getMessageSecurityMode(),
            secureChannel.getLocalNonce(),
            config.getChannelLifetime());

    ByteBuf messageBuffer = BufferUtil.pooledBuffer();

    try {
      binaryEncoder.setBuffer(messageBuffer);
      binaryEncoder.encodeMessage(null, request);

      checkMessageSize(messageBuffer);

      EncodedMessage encodedMessage =
          chunkEncoder.encodeAsymmetric(
              secureChannel, requestIdSupplier.get(), messageBuffer, MessageType.OpenSecureChannel);

      CompositeByteBuf chunkComposite = BufferUtil.compositeBuffer();

      for (ByteBuf chunk : encodedMessage.getMessageChunks()) {
        chunkComposite.addComponent(chunk);
        chunkComposite.writerIndex(chunkComposite.writerIndex() + chunk.readableBytes());
      }

      ctx.writeAndFlush(chunkComposite, ctx.voidPromise());

      ChannelSecurity channelSecurity = secureChannel.getChannelSecurity();

      long currentTokenId = -1L;
      if (channelSecurity != null) {
        currentTokenId = channelSecurity.getCurrentToken().getTokenId().longValue();
      }

      long previousTokenId = -1L;
      if (channelSecurity != null) {
        previousTokenId =
            channelSecurity
                .getPreviousToken()
                .map(token -> token.getTokenId().longValue())
                .orElse(-1L);
      }

      logger.debug(
          "Sent OpenSecureChannelRequest ({}, id={}, currentToken={}, previousToken={}).",
          request.getRequestType(),
          secureChannel.getChannelId(),
          currentTokenId,
          previousTokenId);
    } catch (MessageEncodeException e) {
      logger.error("Error encoding {}: {}", request, e.getMessage(), e);

      ctx.close();
    } finally {
      messageBuffer.release();
    }
  }

  private void sendCloseSecureChannelRequest(
      ChannelHandlerContext ctx, CloseSecureChannelRequest request) {
    ByteBuf messageBuffer = BufferUtil.pooledBuffer();

    try {
      binaryEncoder.setBuffer(messageBuffer);
      binaryEncoder.encodeMessage(null, request);

      checkMessageSize(messageBuffer);

      EncodedMessage encodedMessage =
          chunkEncoder.encodeSymmetric(
              secureChannel,
              requestIdSupplier.get(),
              messageBuffer,
              MessageType.CloseSecureChannel);

      CompositeByteBuf chunkComposite = BufferUtil.compositeBuffer();

      for (ByteBuf chunk : encodedMessage.getMessageChunks()) {
        chunkComposite.addComponent(chunk);
        chunkComposite.writerIndex(chunkComposite.writerIndex() + chunk.readableBytes());
      }

      ctx.writeAndFlush(chunkComposite).addListener(future -> ctx.close());

      secureChannel.setChannelId(0);
    } catch (MessageEncodeException e) {
      logger.error("Error encoding {}: {}", request, e.getMessage(), e);
      handshakeFuture.completeExceptionally(e);
      ctx.close();
    } catch (UaSerializationException e) {
      logger.error("Error serializing {}: {}", request, e.getMessage(), e);
      handshakeFuture.completeExceptionally(e);
      ctx.close();
    } finally {
      messageBuffer.release();
    }
  }

  private void checkMessageSize(ByteBuf messageBuffer) throws UaSerializationException {
    int messageSize = messageBuffer.readableBytes();
    int remoteMaxMessageSize = channelParameters.getRemoteMaxMessageSize();

    if (remoteMaxMessageSize > 0 && messageSize > remoteMaxMessageSize) {
      throw new UaSerializationException(
          StatusCodes.Bad_RequestTooLarge,
          "request exceeds remote max message size: " + messageSize + " > " + remoteMaxMessageSize);
    }
  }

  private int getMaxChunkCount() {
    return channelParameters.getLocalMaxChunkCount();
  }

  private int getMaxChunkSize() {
    return channelParameters.getLocalReceiveBufferSize();
  }

  private static ClientSecureChannel newSecureChannel(ClientApplicationContext application)
      throws UaException {
    EndpointDescription endpoint = application.getEndpoint();

    SecurityPolicy securityPolicy = SecurityPolicy.fromUri(endpoint.getSecurityPolicyUri());

    if (securityPolicy == SecurityPolicy.None) {
      return new ClientSecureChannel(securityPolicy, endpoint.getSecurityMode());
    } else {
      KeyPair keyPair =
          application
              .getKeyPair()
              .orElseThrow(
                  () ->
                      new UaException(StatusCodes.Bad_ConfigurationError, "no KeyPair configured"));

      X509Certificate certificate =
          application
              .getCertificate()
              .orElseThrow(
                  () ->
                      new UaException(
                          StatusCodes.Bad_ConfigurationError, "no certificate configured"));

      List<X509Certificate> certificateChain =
          Arrays.asList(
              application
                  .getCertificateChain()
                  .orElseThrow(
                      () ->
                          new UaException(
                              StatusCodes.Bad_ConfigurationError,
                              "no certificate chain configured")));

      X509Certificate remoteCertificate =
          CertificateUtil.decodeCertificate(endpoint.getServerCertificate().bytes());

      List<X509Certificate> remoteCertificateChain =
          CertificateUtil.decodeCertificates(endpoint.getServerCertificate().bytes());

      return new ClientSecureChannel(
          keyPair,
          certificate,
          certificateChain,
          remoteCertificate,
          remoteCertificateChain,
          securityPolicy,
          endpoint.getSecurityMode());
    }
  }

  private static int getMessageLength(ByteBuf buffer, int maxMessageLength) throws UaException {
    long messageLength = buffer.getUnsignedIntLE(buffer.readerIndex() + 4);

    if (messageLength <= maxMessageLength) {
      return (int) messageLength;
    } else {
      throw new UaException(
          StatusCodes.Bad_TcpMessageTooLarge,
          String.format("max message length exceeded (%s > %s)", messageLength, maxMessageLength));
    }
  }
}

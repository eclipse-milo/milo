/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.NonceUtil.generateNonce;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.Timeout;
import java.io.IOException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecurityKeys;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder.EncodedMessage;
import org.eclipse.milo.opcua.stack.core.channel.ExceptionHandler;
import org.eclipse.milo.opcua.stack.core.channel.MessageAbortException;
import org.eclipse.milo.opcua.stack.core.channel.MessageDecodeException;
import org.eclipse.milo.opcua.stack.core.channel.MessageEncodeException;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.HeaderDecoder;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UascServerAsymmetricHandler extends ByteToMessageDecoder implements HeaderDecoder {

  static final AttributeKey<EndpointDescription> ENDPOINT_KEY = AttributeKey.valueOf("endpoint");

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private ServerSecureChannel secureChannel;
  private Timeout secureChannelTimeout;

  private boolean symmetricHandlerAdded = false;

  private List<ByteBuf> chunkBuffers = new ArrayList<>();

  private final AtomicReference<AsymmetricSecurityHeader> headerRef = new AtomicReference<>();

  private final OpcUaBinaryEncoder binaryEncoder;
  private final OpcUaBinaryDecoder binaryDecoder;
  private final ChunkEncoder chunkEncoder;
  private final ChunkDecoder chunkDecoder;

  private final int maxChunkCount;
  private final int maxChunkSize;

  private final UascServerConfig config;
  private final ServerApplicationContext application;
  private final TransportProfile transportProfile;
  private final ChannelParameters channelParameters;

  UascServerAsymmetricHandler(
      UascServerConfig config,
      ServerApplicationContext application,
      TransportProfile transportProfile,
      ChannelParameters channelParameters) {

    this.config = config;
    this.application = application;
    this.transportProfile = transportProfile;
    this.channelParameters = channelParameters;

    binaryEncoder = new OpcUaBinaryEncoder(application.getEncodingContext());
    binaryDecoder = new OpcUaBinaryDecoder(application.getEncodingContext());

    chunkEncoder = new ChunkEncoder(channelParameters);
    chunkDecoder =
        new ChunkDecoder(channelParameters, application.getEncodingContext().getEncodingLimits());

    maxChunkCount = channelParameters.getLocalMaxChunkCount();
    maxChunkSize = channelParameters.getLocalReceiveBufferSize();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (secureChannelTimeout != null) {
      secureChannelTimeout.cancel();
      secureChannelTimeout = null;
    }

    super.channelInactive(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    chunkBuffers.forEach(ReferenceCountUtil::safeRelease);
    chunkBuffers.clear();

    if (cause instanceof IOException) {
      ctx.close();
      logger.debug(
          "[remote={}] IOException caught; channel closed", ctx.channel().remoteAddress(), cause);
    } else {
      ErrorMessage errorMessage = ExceptionHandler.sendErrorMessage(ctx, cause);

      if (cause instanceof UaException) {
        logger.debug(
            "[remote={}] UaException caught; sent {}",
            ctx.channel().remoteAddress(),
            errorMessage,
            cause);
      } else {
        logger.error(
            "[remote={}] Exception caught; sent {}",
            ctx.channel().remoteAddress(),
            errorMessage,
            cause);
      }
    }
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out)
      throws Exception {
    if (buffer.readableBytes() >= HEADER_LENGTH) {
      int messageLength = getMessageLength(buffer, maxChunkSize);

      if (buffer.readableBytes() >= messageLength) {
        MessageType messageType =
            MessageType.fromMediumInt(buffer.getMediumLE(buffer.readerIndex()));

        switch (messageType) {
          case OpenSecureChannel:
            onOpenSecureChannel(ctx, buffer.readSlice(messageLength));
            break;

          case CloseSecureChannel:
            logger.debug("Received CloseSecureChannelRequest");

            buffer.skipBytes(messageLength);

            if (secureChannelTimeout != null) {
              secureChannelTimeout.cancel();
              secureChannelTimeout = null;
            }

            ctx.close();
            break;

          default:
            throw new UaException(
                StatusCodes.Bad_TcpMessageTypeInvalid, "unexpected MessageType: " + messageType);
        }
      }
    }
  }

  private void onOpenSecureChannel(ChannelHandlerContext ctx, ByteBuf buffer) throws UaException {
    buffer.skipBytes(3); // Skip messageType

    char chunkType = (char) buffer.readByte();

    if (chunkType == 'A') {
      chunkBuffers.forEach(ByteBuf::release);
      chunkBuffers.clear();
      headerRef.set(null);
    } else {
      buffer.skipBytes(4); // Skip messageSize

      final long secureChannelId = buffer.readUnsignedIntLE();

      final AsymmetricSecurityHeader header =
          AsymmetricSecurityHeader.decode(
              buffer, application.getEncodingContext().getEncodingLimits());

      if (!headerRef.compareAndSet(null, header)) {
        if (!header.equals(headerRef.get())) {
          throw new UaException(
              StatusCodes.Bad_SecurityChecksFailed,
              "subsequent AsymmetricSecurityHeader did not match");
        }
      }

      if (secureChannelId != 0) {
        if (secureChannel == null) {
          throw new UaException(
              StatusCodes.Bad_TcpSecureChannelUnknown,
              "unknown secure channel id: " + secureChannelId);
        }

        if (secureChannelId != secureChannel.getChannelId()) {
          throw new UaException(
              StatusCodes.Bad_TcpSecureChannelUnknown,
              "unknown secure channel id: " + secureChannelId);
        }
      }

      if (secureChannel == null) {
        secureChannel = new ServerSecureChannel();
        secureChannel.setChannelId(application.getNextSecureChannelId());

        String securityPolicyUri = header.getSecurityPolicyUri();
        SecurityPolicy securityPolicy = SecurityPolicy.fromUri(securityPolicyUri);

        secureChannel.setSecurityPolicy(securityPolicy);

        if (securityPolicy != SecurityPolicy.None) {
          CertificateManager certificateManager = application.getCertificateManager();

          Optional<X509Certificate[]> localCertificateChain =
              certificateManager.getCertificateChain(header.getReceiverThumbprint());

          Optional<KeyPair> keyPair = certificateManager.getKeyPair(header.getReceiverThumbprint());

          if (localCertificateChain.isPresent() && keyPair.isPresent()) {
            secureChannel.setRemoteCertificate(header.getSenderCertificate().bytesOrEmpty());

            CertificateGroup certificateGroup =
                application
                    .getCertificateManager()
                    .getCertificateGroup(header.getReceiverThumbprint())
                    .orElseThrow(
                        () ->
                            new UaException(
                                StatusCodes.Bad_SecurityChecksFailed,
                                "no certificate group for provided thumbprint"));

            CertificateValidator certificateValidator = certificateGroup.getCertificateValidator();

            certificateValidator.validateCertificateChain(
                secureChannel.getRemoteCertificateChain(), null, null);

            X509Certificate[] chain = localCertificateChain.get();

            secureChannel.setLocalCertificate(chain[0]);
            secureChannel.setLocalCertificateChain(chain);
            secureChannel.setKeyPair(keyPair.get());
          } else {
            throw new UaException(
                StatusCodes.Bad_SecurityChecksFailed, "no certificate for provided thumbprint");
          }
        }
      }

      // Before attempting decryption, ensure the SecurityPolicy used in the
      // AsymmetricSecurityHeader is one that is supported by the configured
      // endpoints.

      String endpointUrl = ctx.channel().attr(UascServerHelloHandler.ENDPOINT_URL_KEY).get();

      if (application.getEndpointDescriptions().stream()
          .noneMatch(
              e -> {
                boolean transportMatch =
                    Objects.equals(e.getTransportProfileUri(), transportProfile.getUri());

                boolean pathMatch =
                    Objects.equals(
                        EndpointUtil.getPath(e.getEndpointUrl()),
                        EndpointUtil.getPath(endpointUrl));

                boolean securityPolicyMatch =
                    Objects.equals(
                        e.getSecurityPolicyUri(), secureChannel.getSecurityPolicy().getUri());

                boolean thumbprintMatch = true;
                if (!header.getReceiverThumbprint().isNullOrEmpty()) {
                  thumbprintMatch =
                      Arrays.equals(
                          DigestUtil.sha1(e.getServerCertificate().bytesOrEmpty()),
                          header.getReceiverThumbprint().bytesOrEmpty());
                }

                // allow a matched endpoint OR any unsecured connection, regardless of the
                // endpoint security, so that the receiving ServerApplication can decide if
                // it wants to allow unsecured Discovery services.
                return transportMatch
                    && pathMatch
                    && thumbprintMatch
                    && (securityPolicyMatch
                        || secureChannel.getSecurityPolicy() == SecurityPolicy.None);
              })) {

        String message =
            String.format(
                "no matching endpoint found: "
                    + "transportProfile=%s, endpointUrl=%s, securityPolicy=%s",
                transportProfile, endpointUrl, secureChannel.getSecurityPolicy());

        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, message);
      }

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

      if (chunkType == 'F') {
        final List<ByteBuf> buffersToDecode = chunkBuffers;

        chunkBuffers = new ArrayList<>();
        headerRef.set(null);

        ByteBuf message;
        long requestId;

        try {
          ChunkDecoder.DecodedMessage decodedMessage =
              chunkDecoder.decodeAsymmetric(secureChannel, buffersToDecode);

          message = decodedMessage.getMessage();
          requestId = decodedMessage.getRequestId();
        } catch (MessageAbortException e) {
          logger.warn(
              "Received message abort chunk; error={}, reason={}",
              e.getStatusCode(),
              e.getMessage());
          return;
        } catch (MessageDecodeException e) {
          logger.error("Error decoding asymmetric message", e);

          ctx.executor()
              .schedule(() -> ctx.close(), new Random().nextInt(1000), TimeUnit.MILLISECONDS);

          return;
        }

        try {
          OpenSecureChannelRequest request =
              (OpenSecureChannelRequest) binaryDecoder.setBuffer(message).decodeMessage(null);

          logger.debug(
              "Received OpenSecureChannelRequest ({}, id={}).",
              request.getRequestType(),
              secureChannelId);

          if (request.getRequestType() == SecurityTokenRequestType.Renew) {
            if (secureChannelId == 0L) {
              throw new UaException(
                  StatusCodes.Bad_SecurityChecksFailed,
                  "secure channel renewal for secureChannelId=0");
            }
          }

          sendOpenSecureChannelResponse(ctx, requestId, header, request);
        } catch (Throwable t) {
          logger.error("Error decoding OpenSecureChannelRequest", t);

          ctx.close();
        } finally {
          message.release();
          buffersToDecode.clear();
        }
      }
    }
  }

  private void sendOpenSecureChannelResponse(
      ChannelHandlerContext ctx,
      long requestId,
      AsymmetricSecurityHeader header,
      OpenSecureChannelRequest request) {

    ByteBuf messageBuffer = BufferUtil.pooledBuffer();

    try {
      OpenSecureChannelResponse response = openSecureChannel(ctx, header, request);

      binaryEncoder.setBuffer(messageBuffer);
      binaryEncoder.encodeMessage(null, response);

      checkMessageSize(messageBuffer);

      EncodedMessage encodedMessage =
          chunkEncoder.encodeAsymmetric(
              secureChannel, requestId, messageBuffer, MessageType.OpenSecureChannel);

      if (!symmetricHandlerAdded) {
        var symmetricHandler =
            new UascServerSymmetricHandler(
                config,
                application,
                transportProfile,
                channelParameters,
                chunkEncoder,
                chunkDecoder,
                secureChannel);

        ctx.pipeline().addBefore(ctx.name(), null, symmetricHandler);

        symmetricHandlerAdded = true;
      }

      CompositeByteBuf chunkComposite = BufferUtil.compositeBuffer();

      for (ByteBuf chunk : encodedMessage.getMessageChunks()) {
        chunkComposite.addComponent(chunk);
        chunkComposite.writerIndex(chunkComposite.writerIndex() + chunk.readableBytes());
      }

      ctx.writeAndFlush(chunkComposite, ctx.voidPromise());

      logger.debug("Sent OpenSecureChannelResponse.");
    } catch (MessageEncodeException e) {
      logger.error("Error encoding OpenSecureChannelResponse: {}", e.getMessage(), e);
      ctx.fireExceptionCaught(e);
    } catch (UaSerializationException e) {
      logger.error("Error serializing OpenSecureChannelResponse: {}", e.getMessage(), e);
      ctx.fireExceptionCaught(e);
    } catch (UaException e) {
      logger.error("Error installing security token: {}", e.getStatusCode(), e);
      ctx.close();
    } finally {
      messageBuffer.release();
    }
  }

  private OpenSecureChannelResponse openSecureChannel(
      ChannelHandlerContext ctx, AsymmetricSecurityHeader header, OpenSecureChannelRequest request)
      throws UaException {

    SecurityTokenRequestType requestType = request.getRequestType();

    if (requestType == SecurityTokenRequestType.Issue) {
      secureChannel.setMessageSecurityMode(request.getSecurityMode());

      String endpointUrl = ctx.channel().attr(UascServerHelloHandler.ENDPOINT_URL_KEY).get();

      EndpointDescription endpoint =
          application.getEndpointDescriptions().stream()
              .filter(
                  e -> {
                    boolean transportMatch =
                        Objects.equals(e.getTransportProfileUri(), transportProfile.getUri());

                    boolean pathMatch =
                        Objects.equals(
                            EndpointUtil.getPath(e.getEndpointUrl()),
                            EndpointUtil.getPath(endpointUrl));

                    boolean securityPolicyMatch =
                        Objects.equals(
                            e.getSecurityPolicyUri(), secureChannel.getSecurityPolicy().getUri());

                    boolean securityModeMatch =
                        Objects.equals(e.getSecurityMode(), request.getSecurityMode());

                    boolean thumbprintMatch = true;
                    if (!header.getReceiverThumbprint().isNullOrEmpty()) {
                      thumbprintMatch =
                          Arrays.equals(
                              DigestUtil.sha1(e.getServerCertificate().bytesOrEmpty()),
                              header.getReceiverThumbprint().bytesOrEmpty());
                    }

                    // allow a matched endpoint OR any unsecured connection, regardless of the
                    // endpoint security, so that the receiving ServerApplication can decide if
                    // it wants to allow unsecured Discovery services.
                    return transportMatch
                        && pathMatch
                        && thumbprintMatch
                        && (securityPolicyMatch && securityModeMatch
                            || secureChannel.getSecurityPolicy() == SecurityPolicy.None);
                  })
              .findFirst()
              .orElseThrow(
                  () -> {
                    String message =
                        String.format(
                            "no matching endpoint found: transportProfile=%s, endpointUrl=%s,"
                                + " thumbprint=%s, securityPolicy=%s, securityMode=%s",
                            transportProfile,
                            endpointUrl,
                            header.getReceiverThumbprint(),
                            secureChannel.getSecurityPolicy(),
                            request.getSecurityMode());

                    return new UaException(StatusCodes.Bad_SecurityChecksFailed, message);
                  });

      ctx.channel().attr(ENDPOINT_KEY).set(endpoint);
    } else if (requestType == SecurityTokenRequestType.Renew) {
      if (secureChannel.getMessageSecurityMode() != request.getSecurityMode()) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "secure channel renewal requested a different MessageSecurityMode.");
      }
    }

    long channelLifetime = request.getRequestedLifetime().longValue();

    channelLifetime =
        Math.min(channelLifetime, config.getMaximumSecureChannelLifetime().longValue());
    channelLifetime =
        Math.max(channelLifetime, config.getMinimumSecureChannelLifetime().longValue());

    ChannelSecurityToken newToken =
        new ChannelSecurityToken(
            uint(secureChannel.getChannelId()),
            uint(application.getNextSecureChannelTokenId()),
            DateTime.now(),
            uint(channelLifetime));

    SecurityKeys newKeys = null;

    if (secureChannel.isSymmetricSigningEnabled()) {
      // Validate the remote nonce; it must be non-null and the correct length for the security
      // algorithm.
      ByteString remoteNonce = request.getClientNonce();

      NonceUtil.validateNonce(remoteNonce, secureChannel.getSecurityPolicy());

      ByteString localNonce = generateNonce(secureChannel.getSecurityPolicy());

      secureChannel.setLocalNonce(localNonce);
      secureChannel.setRemoteNonce(remoteNonce);

      newKeys =
          ChannelSecurity.generateKeyPair(
              secureChannel, secureChannel.getRemoteNonce(), secureChannel.getLocalNonce());
    }

    ChannelSecurity oldSecrets = secureChannel.getChannelSecurity();
    SecurityKeys oldKeys = oldSecrets != null ? oldSecrets.getCurrentKeys() : null;
    ChannelSecurityToken oldToken = oldSecrets != null ? oldSecrets.getCurrentToken() : null;

    ChannelSecurity newSecrets = new ChannelSecurity(newKeys, newToken, oldKeys, oldToken);

    secureChannel.setChannelSecurity(newSecrets);

    /*
     * Cancel the previous timeout, if it exists, and start a new one.
     */
    if (secureChannelTimeout == null || secureChannelTimeout.cancel()) {
      final long lifetime = channelLifetime;
      secureChannelTimeout =
          Stack.sharedWheelTimer()
              .newTimeout(
                  timeout -> {
                    logger.debug(
                        "SecureChannel renewal timed out after {}ms. id={}, channel={}",
                        lifetime,
                        secureChannel.getChannelId(),
                        ctx.channel());

                    ctx.close();
                  },
                  channelLifetime,
                  TimeUnit.MILLISECONDS);
    }

    ResponseHeader responseHeader =
        new ResponseHeader(
            DateTime.now(),
            request.getRequestHeader().getRequestHandle(),
            StatusCode.GOOD,
            null,
            null,
            null);

    return new OpenSecureChannelResponse(
        responseHeader, uint(PROTOCOL_VERSION), newToken, secureChannel.getLocalNonce());
  }

  private void checkMessageSize(ByteBuf messageBuffer) throws UaSerializationException {
    int messageSize = messageBuffer.readableBytes();
    int remoteMaxMessageSize = channelParameters.getRemoteMaxMessageSize();

    if (remoteMaxMessageSize > 0 && messageSize > remoteMaxMessageSize) {
      throw new UaSerializationException(
          StatusCodes.Bad_ResponseTooLarge,
          "response exceeds remote max message size: "
              + messageSize
              + " > "
              + remoteMaxMessageSize);
    }
  }
}

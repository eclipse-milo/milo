/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.https;

import static java.util.Objects.requireNonNullElse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcClientHttpCodec extends MessageToMessageCodec<HttpResponse, UaRequestMessageType> {

  private static final String UABINARY_CONTENT_TYPE =
      HttpHeaderValues.APPLICATION_OCTET_STREAM.toString();

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final EndpointDescription endpoint;
  private final TransportProfile transportProfile;

  private final OpcClientTransportConfig config;
  private final ClientApplicationContext application;

  OpcClientHttpCodec(OpcClientTransportConfig config, ClientApplicationContext application) {
    this.config = config;
    this.application = application;

    endpoint = application.getEndpoint();
    transportProfile =
        TransportProfile.fromUri(requireNonNullElse(endpoint.getTransportProfileUri(), ""));
  }

  @Override
  protected void encode(
      ChannelHandlerContext ctx, UaRequestMessageType requestMessage, List<Object> out)
      throws Exception {

    logger.debug("encoding: {}", requestMessage);

    ByteBuf content = Unpooled.buffer();

    switch (transportProfile) {
      case HTTPS_UABINARY:
        {
          var encoder = new OpcUaBinaryEncoder(application.getEncodingContext());
          encoder.setBuffer(content);
          encoder.encodeMessage(null, requestMessage);
          break;
        }

      case HTTPS_UAXML:
        {
          // TODO put document into a SOAP message.
          throw new UaException(
              StatusCodes.Bad_InternalError, "no encoder for transport: " + transportProfile);
        }

      default:
        throw new UaException(
            StatusCodes.Bad_InternalError, "no encoder for transport: " + transportProfile);
    }

    String endpointUrl = endpoint.getEndpointUrl();

    FullHttpRequest httpRequest =
        new DefaultFullHttpRequest(
            HttpVersion.HTTP_1_1, HttpMethod.POST, EndpointUtil.getPath(endpointUrl), content);

    httpRequest.headers().set(HttpHeaderNames.HOST, EndpointUtil.getHost(endpointUrl));
    httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
    httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, UABINARY_CONTENT_TYPE);
    httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
    httpRequest
        .headers()
        .set("OPCUA-SecurityPolicy", application.getEndpoint().getSecurityPolicyUri());

    out.add(httpRequest);
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, HttpResponse httpResponse, List<Object> out)
      throws Exception {

    logger.trace("channelRead0: {}", httpResponse);

    if (httpResponse instanceof FullHttpResponse) {
      String contentType = httpResponse.headers().get(HttpHeaderNames.CONTENT_TYPE);

      FullHttpResponse fullHttpResponse = (FullHttpResponse) httpResponse;
      ByteBuf content = fullHttpResponse.content();

      UaResponseMessageType responseMessage;

      switch (transportProfile) {
        case HTTPS_UABINARY:
          {
            if (!UABINARY_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
              throw new UaException(
                  StatusCodes.Bad_DecodingError, "unexpected content-type: " + contentType);
            }

            var decoder = new OpcUaBinaryDecoder(application.getEncodingContext());
            decoder.setBuffer(content);
            responseMessage = (UaResponseMessageType) decoder.decodeMessage(null);
            break;
          }

        case HTTPS_UAXML:
          {
            // TODO extract document from SOAP message body
            throw new UaException(
                StatusCodes.Bad_InternalError, "no decoder for transport: " + transportProfile);
          }

        default:
          throw new UaException(
              StatusCodes.Bad_InternalError, "no decoder for transport: " + transportProfile);
      }

      out.add(responseMessage);
    } else {
      HttpResponseStatus status = httpResponse.status();

      if (status.equals(HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE)) {
        // TODO
        //                transportRequest.getFuture().completeExceptionally(
        //                    new UaException(StatusCodes.Bad_ResponseTooLarge));
      } else {
        // TODO
        //                transportRequest.getFuture().completeExceptionally(
        //                    new UaException(StatusCodes.Bad_UnexpectedError,
        //                        String.format("%s: %s", status.code(), status.reasonPhrase())));
      }
    }
  }
}

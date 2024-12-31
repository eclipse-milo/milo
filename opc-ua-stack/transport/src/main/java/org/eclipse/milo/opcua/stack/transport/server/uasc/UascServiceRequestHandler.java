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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;

public class UascServiceRequestHandler extends SimpleChannelInboundHandler<UascServiceRequest> {

  private final UascServerConfig config;
  private final ServerApplicationContext applicationContext;

  public UascServiceRequestHandler(
      UascServerConfig config, ServerApplicationContext applicationContext) {
    this.config = config;
    this.applicationContext = applicationContext;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, UascServiceRequest serviceRequest) {
    dispatchServiceRequest(serviceRequest);
  }

  private void dispatchServiceRequest(UascServiceRequest serviceRequest) {
    CompletableFuture<UaResponseMessageType> future =
        applicationContext.handleServiceRequest(serviceRequest, serviceRequest.getRequestMessage());

    future.whenComplete(
        ((response, ex) -> {
          if (response != null) {
            var serviceResponse = new UascServiceResponse(response, serviceRequest.getRequestId());

            serviceRequest.getChannel().pipeline().writeAndFlush(serviceResponse);
          } else {
            StatusCode serviceResult =
                UaException.extractStatusCode(ex)
                    .orElse(new StatusCode(StatusCodes.Bad_UnexpectedError));

            var header =
                new ResponseHeader(
                    DateTime.now(),
                    serviceRequest.getRequestMessage().getRequestHeader().getRequestHandle(),
                    serviceResult,
                    DiagnosticInfo.NULL_VALUE,
                    null,
                    null);

            var serviceResponse =
                new UascServiceResponse(new ServiceFault(header), serviceRequest.getRequestId());

            serviceRequest.getChannel().writeAndFlush(serviceResponse);
          }
        }));
  }
}

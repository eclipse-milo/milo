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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;

public abstract class InboundUascResponseHandler extends SimpleChannelInboundHandler<UascResponse>
    implements UascResponseHandler {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, UascResponse response) {
    if (response.isSuccess()) {
      UaResponseMessageType responseMessage = response.getResponseMessage();
      assert responseMessage != null;
      ResponseHeader responseHeader = responseMessage.getResponseHeader();

      if (responseHeader.getServiceResult().isGood()) {
        handleResponse(response.getRequestId(), responseMessage);
      } else {
        ServiceFault serviceFault;

        if (responseMessage instanceof ServiceFault) {
          serviceFault = (ServiceFault) responseMessage;
        } else {
          serviceFault = new ServiceFault(responseHeader);
        }

        handleReceiveFailure(response.getRequestId(), new UaServiceFaultException(serviceFault));
      }
    } else {
      handleReceiveFailure(response.getRequestId(), response.getException());
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof UascResponse) {
      UascResponse response = (UascResponse) evt;
      assert response.isFailure();

      handleSendFailure(response.getRequestId(), response.getException());
    } else if (evt instanceof ErrorMessage) {
      ErrorMessage errorMessage = (ErrorMessage) evt;
      StatusCode statusCode = errorMessage.getError();

      handleChannelError(new UaException(statusCode, errorMessage.getReason()));
    }

    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    handleChannelInactive();

    super.channelInactive(ctx);
  }

  public static class DelegatingUascResponseHandler extends InboundUascResponseHandler {

    private final UascResponseHandler delegate;

    public DelegatingUascResponseHandler(UascResponseHandler delegate) {
      this.delegate = delegate;
    }

    @Override
    public void handleResponse(long requestId, UaResponseMessageType responseMessage) {
      delegate.handleResponse(requestId, responseMessage);
    }

    @Override
    public void handleSendFailure(long requestId, UaException exception) {
      delegate.handleSendFailure(requestId, exception);
    }

    @Override
    public void handleReceiveFailure(long requestId, UaException exception) {
      delegate.handleReceiveFailure(requestId, exception);
    }

    @Override
    public void handleChannelError(UaException exception) {
      delegate.handleChannelError(exception);
    }

    @Override
    public void handleChannelInactive() {
      delegate.handleChannelInactive();
    }
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server;

import io.netty.channel.Channel;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;

/** Holds a received {@link UaRequestMessageType} with some additional transport layer details. */
public class ServiceRequest implements ServiceRequestContext {

  private final long receivedAtNanos = System.nanoTime();

  private final String endpointUrl;
  private final TransportProfile transportProfile;
  private final Channel channel;
  private final SecureChannel secureChannel;
  private final UaRequestMessageType requestMessage;

  public ServiceRequest(
      String endpointUrl,
      TransportProfile transportProfile,
      Channel channel,
      SecureChannel secureChannel,
      UaRequestMessageType requestMessage) {

    this.endpointUrl = endpointUrl;
    this.transportProfile = transportProfile;
    this.channel = channel;
    this.secureChannel = secureChannel;
    this.requestMessage = requestMessage;
  }

  @Override
  public String getEndpointUrl() {
    return endpointUrl;
  }

  @Override
  public TransportProfile getTransportProfile() {
    return transportProfile;
  }

  @Override
  public Channel getChannel() {
    return channel;
  }

  @Override
  public SecureChannel getSecureChannel() {
    return secureChannel;
  }

  @Override
  public Long receivedAtNanos() {
    return receivedAtNanos;
  }

  /**
   * Get the incoming {@link UaRequestMessageType}.
   *
   * @return the incoming {@link UaRequestMessageType}.
   */
  public UaRequestMessageType getRequestMessage() {
    return requestMessage;
  }
}

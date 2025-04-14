/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.transport;

import org.eclipse.milo.opcua.stack.core.Stack;

public enum TransportProfile {
  TCP_UASC_UABINARY,
  HTTPS_UABINARY,
  HTTPS_UAXML,
  HTTPS_UAJSON,
  WSS_UASC_UABINARY,
  WSS_UAJSON;

  public static TransportProfile fromUri(String uri) {
    return switch (uri) {
      case Stack.TCP_UASC_UABINARY_TRANSPORT_URI -> TCP_UASC_UABINARY;
      case Stack.HTTPS_UABINARY_TRANSPORT_URI -> HTTPS_UABINARY;
      case Stack.HTTPS_UAXML_TRANSPORT_URI -> HTTPS_UAXML;
      case Stack.HTTPS_UAJSON_TRANSPORT_URI -> HTTPS_UAJSON;
      case Stack.WSS_UASC_UABINARY_TRANSPORT_URI -> WSS_UASC_UABINARY;
      case Stack.WSS_UAJSON_TRANSPORT_URI -> WSS_UAJSON;
      default -> throw new IllegalArgumentException("unknown transport: " + uri);
    };
  }

  public String getUri() {
    return switch (this) {
      case TCP_UASC_UABINARY -> Stack.TCP_UASC_UABINARY_TRANSPORT_URI;
      case HTTPS_UABINARY -> Stack.HTTPS_UABINARY_TRANSPORT_URI;
      case HTTPS_UAXML -> Stack.HTTPS_UAXML_TRANSPORT_URI;
      case HTTPS_UAJSON -> Stack.HTTPS_UAJSON_TRANSPORT_URI;
      case WSS_UASC_UABINARY -> Stack.WSS_UASC_UABINARY_TRANSPORT_URI;
      case WSS_UAJSON -> Stack.WSS_UAJSON_TRANSPORT_URI;
    };
  }

  public String getScheme() {
    return switch (this) {
      case TCP_UASC_UABINARY -> "opc.tcp";
      case HTTPS_UABINARY, HTTPS_UAXML, HTTPS_UAJSON -> "https";
      case WSS_UASC_UABINARY, WSS_UAJSON -> "opc.wss";
    };
  }
}

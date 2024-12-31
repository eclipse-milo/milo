/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
    // @formatter:off
    switch (uri) {
      case Stack.TCP_UASC_UABINARY_TRANSPORT_URI:
        return TCP_UASC_UABINARY;
      case Stack.HTTPS_UABINARY_TRANSPORT_URI:
        return HTTPS_UABINARY;
      case Stack.HTTPS_UAXML_TRANSPORT_URI:
        return HTTPS_UAXML;
      case Stack.HTTPS_UAJSON_TRANSPORT_URI:
        return HTTPS_UAJSON;
      case Stack.WSS_UASC_UABINARY_TRANSPORT_URI:
        return WSS_UASC_UABINARY;
      case Stack.WSS_UAJSON_TRANSPORT_URI:
        return WSS_UAJSON;
      default:
        throw new IllegalArgumentException("unknown transport: " + uri);
    }
    // @formatter:on
  }

  public String getUri() {
    // @formatter:off
    switch (this) {
      case TCP_UASC_UABINARY:
        return Stack.TCP_UASC_UABINARY_TRANSPORT_URI;
      case HTTPS_UABINARY:
        return Stack.HTTPS_UABINARY_TRANSPORT_URI;
      case HTTPS_UAXML:
        return Stack.HTTPS_UAXML_TRANSPORT_URI;
      case HTTPS_UAJSON:
        return Stack.HTTPS_UAJSON_TRANSPORT_URI;
      case WSS_UASC_UABINARY:
        return Stack.WSS_UASC_UABINARY_TRANSPORT_URI;
      case WSS_UAJSON:
        return Stack.WSS_UAJSON_TRANSPORT_URI;
      default:
        throw new IllegalArgumentException("unhandled profile: " + this);
    }
    // @formatter:on
  }

  public String getScheme() {
    switch (this) {
      case TCP_UASC_UABINARY:
        return "opc.tcp";
      case HTTPS_UABINARY:
      case HTTPS_UAXML:
      case HTTPS_UAJSON:
        return "https";
      case WSS_UASC_UABINARY:
      case WSS_UAJSON:
        return "opc.wss";
      default:
        throw new IllegalArgumentException("unhandled profile: " + this);
    }
  }
}

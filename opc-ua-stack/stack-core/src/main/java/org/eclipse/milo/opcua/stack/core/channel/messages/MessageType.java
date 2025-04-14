/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.messages;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

public enum MessageType {
  Hello,
  Acknowledge,
  Error,
  OpenSecureChannel,
  CloseSecureChannel,
  SecureMessage;

  private static final int HEL = ('L' << 16) | ('E' << 8) | 'H';
  private static final int ACK = ('K' << 16) | ('C' << 8) | 'A';
  private static final int ERR = ('R' << 16) | ('R' << 8) | 'E';
  private static final int OPN = ('N' << 16) | ('P' << 8) | 'O';
  private static final int CLO = ('O' << 16) | ('L' << 8) | 'C';
  private static final int MSG = ('G' << 16) | ('S' << 8) | 'M';

  public static int toMediumInt(MessageType messageType) {
    return switch (messageType) {
      case Hello -> HEL;
      case Acknowledge -> ACK;
      case Error -> ERR;
      case OpenSecureChannel -> OPN;
      case CloseSecureChannel -> CLO;
      case SecureMessage -> MSG;
    };
  }

  public static MessageType fromMediumInt(int messageType) throws UaException {
    return switch (messageType) {
      case HEL -> Hello;
      case ACK -> Acknowledge;
      case ERR -> Error;
      case OPN -> OpenSecureChannel;
      case CLO -> CloseSecureChannel;
      case MSG -> SecureMessage;
      default ->
          throw new UaException(
              StatusCodes.Bad_TcpMessageTypeInvalid, "unknown message type: " + messageType);
    };
  }
}

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

/**
 * UA-TCP message type identifiers as they appear in the first three bytes of a message header.
 *
 * <p>The integer values exposed by this type are arranged for Netty's little-endian medium integer
 * encoding. For example, {@link #ReverseHello} is represented by the wire bytes {@code RHE}.
 */
public enum MessageType {
  Hello,
  Acknowledge,
  Error,
  OpenSecureChannel,
  CloseSecureChannel,
  SecureMessage,
  ReverseHello;

  private static final int HEL = ('L' << 16) | ('E' << 8) | 'H';
  private static final int RHE = ('E' << 16) | ('H' << 8) | 'R';
  private static final int ACK = ('K' << 16) | ('C' << 8) | 'A';
  private static final int ERR = ('R' << 16) | ('R' << 8) | 'E';
  private static final int OPN = ('N' << 16) | ('P' << 8) | 'O';
  private static final int CLO = ('O' << 16) | ('L' << 8) | 'C';
  private static final int MSG = ('G' << 16) | ('S' << 8) | 'M';

  /**
   * Convert a message type to the integer form written by {@code ByteBuf.writeMediumLE()}.
   *
   * @param messageType the message type to encode into a UA-TCP header.
   * @return the little-endian medium integer representation of {@code messageType}.
   */
  public static int toMediumInt(MessageType messageType) {
    return switch (messageType) {
      case Hello -> HEL;
      case ReverseHello -> RHE;
      case Acknowledge -> ACK;
      case Error -> ERR;
      case OpenSecureChannel -> OPN;
      case CloseSecureChannel -> CLO;
      case SecureMessage -> MSG;
    };
  }

  /**
   * Decode a UA-TCP message type from the integer form read by {@code ByteBuf.readMediumLE()}.
   *
   * @param messageType the little-endian medium integer representation read from the header.
   * @return the matching message type.
   * @throws UaException if the header contains an unknown message type.
   */
  public static MessageType fromMediumInt(int messageType) throws UaException {
    return switch (messageType) {
      case HEL -> Hello;
      case RHE -> ReverseHello;
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

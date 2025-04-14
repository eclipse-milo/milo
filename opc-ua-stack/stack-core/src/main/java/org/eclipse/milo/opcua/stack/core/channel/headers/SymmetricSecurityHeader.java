/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel.headers;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;

public class SymmetricSecurityHeader {

  public static final int SYMMETRIC_SECURITY_HEADER_SIZE = 4;

  private final long tokenId;

  /**
   * @param tokenId A unique identifier for the SecureChannel SecurityToken used to secure the
   *     Message.
   */
  public SymmetricSecurityHeader(long tokenId) {
    this.tokenId = tokenId;
  }

  public long getTokenId() {
    return tokenId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SymmetricSecurityHeader that = (SymmetricSecurityHeader) o;

    return tokenId == that.tokenId;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(tokenId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("tokenId", tokenId).toString();
  }

  public static ByteBuf encode(SymmetricSecurityHeader header, ByteBuf buffer) {
    buffer.writeIntLE((int) header.getTokenId());

    return buffer;
  }

  public static SymmetricSecurityHeader decode(ByteBuf buffer) {
    return new SymmetricSecurityHeader(buffer.readUnsignedIntLE());
  }
}

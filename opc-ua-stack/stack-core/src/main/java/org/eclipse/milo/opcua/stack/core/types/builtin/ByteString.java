/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import java.util.Arrays;
import java.util.HexFormat;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record ByteString(byte @Nullable [] bytes) {

  public static final ByteString NULL_VALUE = new ByteString(null);

  public int length() {
    return bytes != null ? bytes.length : 0;
  }

  public boolean isNull() {
    return bytes == null;
  }

  public boolean isNullOrEmpty() {
    return bytes == null || bytes.length == 0;
  }

  public boolean isNotNull() {
    return bytes != null;
  }

  public byte[] bytesOrElse(byte[] other) {
    return bytes != null ? bytes : other;
  }

  public byte[] bytesOrEmpty() {
    return bytesOrElse(new byte[0]);
  }

  public UByte @Nullable [] uBytes() {
    if (bytes == null) return null;

    UByte[] bs = new UByte[bytes.length];
    for (int i = 0; i < bytes.length; i++) {
      bs[i] = Unsigned.ubyte(bytes[i]);
    }
    return bs;
  }

  public UByte[] uBytesOrElse(UByte[] other) {
    UByte[] ubs = uBytes();

    return ubs != null ? ubs : other;
  }

  public UByte[] uBytesOrEmpty() {
    return uBytesOrElse(new UByte[0]);
  }

  public byte byteAt(int index) {
    if (bytes == null) throw new IndexOutOfBoundsException("index=" + index);

    return bytes[index];
  }

  public UByte uByteAt(int index) {
    return Unsigned.ubyte(byteAt(index));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ByteString that = (ByteString) o;

    return Arrays.equals(bytes, that.bytes) || (isNullOrEmpty() && that.isNullOrEmpty());
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(bytes);
  }

  @Override
  public String toString() {
    return "ByteString[bytes=" + (bytes == null ? "null" : HexFormat.of().formatHex(bytes)) + "]";
  }

  public static ByteString of(byte[] bs) {
    return new ByteString(bs);
  }
}

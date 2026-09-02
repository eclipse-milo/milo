/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.junit.jupiter.api.Test;

public class TrustListReaderDecodeTest {

  private static final EncodingContext CONTEXT = DefaultEncodingContext.INSTANCE;

  private static final TrustListDataType TRUST_LIST =
      new TrustListDataType(
          uint(
              TrustListMasks.TrustedCertificates.getValue() | TrustListMasks.IssuerCrls.getValue()),
          new ByteString[] {ByteString.of(new byte[] {1, 2, 3}), ByteString.of(new byte[] {4})},
          null,
          null,
          new ByteString[] {ByteString.of(new byte[] {5, 6})});

  /** Encode {@code trustList} the way a TrustList file body is encoded (Part 12 §7.8.2). */
  static byte[] bareBody(TrustListDataType trustList) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      var encoder = new OpcUaBinaryEncoder(CONTEXT).setBuffer(buffer);
      new TrustListDataType.Codec().encodeType(CONTEXT, encoder, trustList);
      return ByteBufUtil.getBytes(buffer);
    } finally {
      buffer.release();
    }
  }

  // Part 12 §7.8.2 says the file contains the structure itself, not an ExtensionObject; the
  // reference server and Milo-based Push servers both write it that way.
  @Test
  void decodeReadsBareTrustListDataTypeBody() throws Exception {
    TrustListDataType decoded = TrustListReader.decode(CONTEXT, bareBody(TRUST_LIST));

    assertEquals(TRUST_LIST, decoded);
  }

  // An ExtensionObject-wrapped body starts with the encoding NodeId, which would be mistaken for
  // SpecifiedLists; the reader must not silently accept that framing.
  @Test
  void decodeRejectsExtensionObjectWrappedBody() {
    ExtensionObject wrapped = ExtensionObject.encode(CONTEXT, TRUST_LIST);
    ByteBuf buffer = Unpooled.buffer();
    byte[] body;
    try {
      new OpcUaBinaryEncoder(CONTEXT).setBuffer(buffer).encodeExtensionObject(null, wrapped);
      body = ByteBufUtil.getBytes(buffer);
    } finally {
      buffer.release();
    }

    UaException e = assertThrows(UaException.class, () -> TrustListReader.decode(CONTEXT, body));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  // Leftover bytes mean the chunked read reassembled something other than one structure.
  @Test
  void decodeRejectsTrailingBytes() {
    byte[] full = bareBody(TRUST_LIST);
    byte[] body = Arrays.copyOf(full, full.length + 1);

    UaException e = assertThrows(UaException.class, () -> TrustListReader.decode(CONTEXT, body));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }

  @Test
  void decodeRejectsTruncatedBody() {
    byte[] full = bareBody(TRUST_LIST);
    byte[] body = Arrays.copyOf(full, full.length - 3);

    UaException e = assertThrows(UaException.class, () -> TrustListReader.decode(CONTEXT, body));

    assertEquals(StatusCodes.Bad_DecodingError, e.getStatusCode().value());
  }
}

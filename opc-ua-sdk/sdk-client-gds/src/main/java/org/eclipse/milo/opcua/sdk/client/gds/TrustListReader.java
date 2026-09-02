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

import static java.util.Objects.requireNonNullElse;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OpenFileMode;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads the contents of a {@code TrustListType} object into a {@link TrustListDataType}.
 *
 * <p>A TrustList is a {@code FileType} whose body is the OPC UA Binary encoding of a bare {@link
 * TrustListDataType} (Part 12 §7.8.2). {@link #read} opens the file for reading, reads it in
 * chunks, closes it, and decodes the body; the caller supplies the TrustList id, typically from
 * {@link GdsClient#getTrustList(NodeId, NodeId)}, and then hands the result to {@link
 * TrustListApplier}. The FileType methods are invoked through the {@code FileType} declaration ids
 * ({@code FileType_Open} and so on), which Part 4 §5.12.2.2 allows for any FileType subtype
 * instance.
 *
 * <p>Chunk size defaults to the object's optional {@code MaxByteStringLength} property, else {@link
 * #DEFAULT_CHUNK_SIZE}, in both cases capped so a chunk fits in a message the client is willing to
 * receive. Use the explicit-size overloads for servers that need something else.
 */
public final class TrustListReader {

  /** Chunk size used when the TrustList does not expose {@code MaxByteStringLength}: 64 KiB. */
  public static final int DEFAULT_CHUNK_SIZE = 64 * 1024;

  // Headroom for the response headers and the Read result wrapping a chunk of data.
  private static final int MESSAGE_OVERHEAD = 4096;

  private static final Logger LOGGER = LoggerFactory.getLogger(TrustListReader.class);

  private TrustListReader() {}

  /**
   * Read and decode a TrustList, choosing the chunk size from the object's {@code
   * MaxByteStringLength} property or {@link #DEFAULT_CHUNK_SIZE}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @param trustListId the {@link NodeId} of a {@code TrustListType} object.
   * @return the decoded {@link TrustListDataType}.
   * @throws UaException if opening, reading, or closing the file fails, or with {@link
   *     StatusCodes#Bad_DecodingError} if the body is not a valid {@link TrustListDataType}.
   */
  public static TrustListDataType read(OpcUaClient client, NodeId trustListId) throws UaException {
    return ClientCalls.await(readAsync(client, trustListId));
  }

  /**
   * Read and decode a TrustList using {@code chunkSize}-byte Read calls.
   *
   * @param client a connected {@link OpcUaClient}.
   * @param trustListId the {@link NodeId} of a {@code TrustListType} object.
   * @param chunkSize the number of bytes to request per Read call; must be positive.
   * @return the decoded {@link TrustListDataType}.
   * @throws UaException if opening, reading, or closing the file fails, or with {@link
   *     StatusCodes#Bad_DecodingError} if the body is not a valid {@link TrustListDataType}.
   */
  public static TrustListDataType read(OpcUaClient client, NodeId trustListId, int chunkSize)
      throws UaException {

    return ClientCalls.await(readAsync(client, trustListId, chunkSize));
  }

  /**
   * Asynchronous form of {@link #read(OpcUaClient, NodeId)}.
   *
   * @return a future completing with the decoded {@link TrustListDataType}.
   */
  public static CompletableFuture<TrustListDataType> readAsync(
      OpcUaClient client, NodeId trustListId) {

    return ClientCalls.readProperties(client, trustListId, List.of("MaxByteStringLength"))
        .thenCompose(values -> readAsync(client, trustListId, chunkSize(client, values.get(0))));
  }

  /**
   * Asynchronous form of {@link #read(OpcUaClient, NodeId, int)}.
   *
   * @return a future completing with the decoded {@link TrustListDataType}.
   */
  public static CompletableFuture<TrustListDataType> readAsync(
      OpcUaClient client, NodeId trustListId, int chunkSize) {

    if (chunkSize <= 0) {
      return failedFuture(new IllegalArgumentException("chunkSize must be positive: " + chunkSize));
    }

    return open(client, trustListId)
        .thenCompose(fileHandle -> readThenClose(client, trustListId, fileHandle, chunkSize))
        .thenCompose(
            body -> {
              try {
                return completedFuture(decode(client.getStaticEncodingContext(), body));
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  /**
   * Read the whole file, then close it whether or not the read succeeded. A read failure wins over
   * a close failure; a close failure after a successful read is logged, since the data is complete
   * and the server reclaims an unclosed handle on its own.
   */
  private static CompletableFuture<byte[]> readThenClose(
      OpcUaClient client, NodeId trustListId, UInteger fileHandle, int chunkSize) {

    var result = new CompletableFuture<byte[]>();

    readAll(client, trustListId, fileHandle, chunkSize, new ByteArrayOutputStream())
        .whenComplete(
            (body, readError) ->
                close(client, trustListId, fileHandle)
                    .whenComplete(
                        (ignored, closeError) -> {
                          if (readError != null) {
                            Throwable cause = unwrap(readError);
                            if (closeError != null) {
                              cause.addSuppressed(unwrap(closeError));
                            }
                            result.completeExceptionally(cause);
                          } else {
                            if (closeError != null) {
                              LOGGER.warn(
                                  "Close failed after reading TrustList {}: {}",
                                  trustListId,
                                  unwrap(closeError).toString());
                            }
                            result.complete(body);
                          }
                        }));

    return result;
  }

  private static Throwable unwrap(Throwable t) {
    return t instanceof CompletionException && t.getCause() != null ? t.getCause() : t;
  }

  /**
   * Decode the body of a TrustList file, which is a bare {@link TrustListDataType} in OPC UA Binary
   * encoding (Part 12 §7.8.2), not an ExtensionObject.
   *
   * @param context the {@link EncodingContext} to decode with.
   * @param body the file contents.
   * @return the decoded {@link TrustListDataType}.
   * @throws UaException {@link StatusCodes#Bad_DecodingError} if the body is malformed or has
   *     trailing bytes.
   */
  public static TrustListDataType decode(EncodingContext context, byte[] body) throws UaException {

    ByteBuf buffer = Unpooled.wrappedBuffer(body);

    try {
      var decoder = new OpcUaBinaryDecoder(context).setBuffer(buffer);

      TrustListDataType trustList = new TrustListDataType.Codec().decodeType(context, decoder);

      if (buffer.isReadable()) {
        throw new UaException(
            StatusCodes.Bad_DecodingError,
            "TrustList body has " + buffer.readableBytes() + " trailing bytes");
      }

      return trustList;
    } catch (RuntimeException e) {
      throw new UaException(StatusCodes.Bad_DecodingError, "TrustList body is malformed", e);
    } finally {
      buffer.release();
    }
  }

  private static int chunkSize(OpcUaClient client, @Nullable DataValue maxByteStringLength) {
    int cap = client.getConfig().getEncodingLimits().getMaxMessageSize() - MESSAGE_OVERHEAD;

    int preferred = DEFAULT_CHUNK_SIZE;

    if (maxByteStringLength != null
        && maxByteStringLength.getStatusCode().isGood()
        && maxByteStringLength.value().value() instanceof UInteger max
        && max.longValue() > 0) {
      preferred = (int) Math.min(max.longValue(), Integer.MAX_VALUE);
    }

    return Math.max(1, Math.min(preferred, cap));
  }

  private static CompletableFuture<UInteger> open(OpcUaClient client, NodeId trustListId) {
    return ClientCalls.call(
        client,
        trustListId,
        NodeIds.FileType_Open,
        "Open",
        new Variant[] {Variant.ofByte(ubyte(OpenFileMode.Read.getValue()))},
        1,
        outputs -> outputs.scalar(0, UInteger.class));
  }

  private static CompletableFuture<byte[]> readAll(
      OpcUaClient client,
      NodeId trustListId,
      UInteger fileHandle,
      int chunkSize,
      ByteArrayOutputStream body) {

    return ClientCalls.call(
            client,
            trustListId,
            NodeIds.FileType_Read,
            "Read",
            new Variant[] {Variant.ofUInt32(fileHandle), Variant.ofInt32(chunkSize)},
            1,
            outputs ->
                requireNonNullElse(
                    outputs.nullableScalar(0, ByteString.class), ByteString.NULL_VALUE))
        .thenCompose(
            data -> {
              byte[] chunk = data.bytesOrEmpty();

              body.writeBytes(chunk);

              if (chunk.length < chunkSize) {
                return completedFuture(body.toByteArray());
              } else {
                return readAll(client, trustListId, fileHandle, chunkSize, body);
              }
            });
  }

  private static CompletableFuture<Unit> close(
      OpcUaClient client, NodeId trustListId, UInteger fileHandle) {

    return ClientCalls.call(
        client,
        trustListId,
        NodeIds.FileType_Close,
        "Close",
        new Variant[] {Variant.ofUInt32(fileHandle)},
        0,
        outputs -> Unit.VALUE);
  }
}

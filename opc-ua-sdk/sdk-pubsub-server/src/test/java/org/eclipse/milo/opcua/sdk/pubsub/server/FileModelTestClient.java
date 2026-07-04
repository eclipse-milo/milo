/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.jspecify.annotations.Nullable;

/**
 * Client-side driver for the {@code i=25451} file model: a thin wrapper over a connected {@link
 * OpcUaClient} exposing the eight Part 14 §9.1.3.7 methods as typed calls over the REAL wire. One
 * instance per {@link OpcUaClient} (per session — handles are session-scoped).
 *
 * <p>Every call goes through {@code client.call(...)} against the loader-built ns0 method nodes, so
 * the whole request/response — including the {@code ConfigurationReferences} struct-array input,
 * packed exactly as a real configuration tool packs it: {@code new
 * Variant(ExtensionObject.encodeArray(...))} — crosses the wire and the sdk-server argument
 * decoding path. This deliberately exercises struct-array decoding end-to-end (the pre-fix {@code
 * AbstractMethodInvocationHandler} cast a {@code ExtensionObject[]} value to scalar {@code
 * ExtensionObject} and answered {@code Bad_InternalError}).
 *
 * <p>Shared test fixture (no {@code @Test} methods), precedented by {@link TestPubSubServer}/{@link
 * SksTestServer}; the per-class helper-duplication convention does not apply here.
 */
final class FileModelTestClient {

  /** A method result: the method-level status and the (possibly empty) output arguments. */
  record CallResult(StatusCode status, Variant[] outputs) {}

  /** Read chunk/write chunk size; far below the served MaxByteStringLength (1 MiB default). */
  private static final int CHUNK_SIZE = 64 * 1024;

  private final OpcUaClient client;

  FileModelTestClient(OpcUaClient client) {
    this.client = client;
  }

  OpcUaClient client() {
    return client;
  }

  // region the eight methods

  CallResult open(UByte mode) throws UaException {
    return call(NodeIds.PublishSubscribe_PubSubConfiguration_Open, new Variant(mode));
  }

  CallResult close(UInteger fileHandle) throws UaException {
    return call(NodeIds.PublishSubscribe_PubSubConfiguration_Close, new Variant(fileHandle));
  }

  CallResult read(UInteger fileHandle, int length) throws UaException {
    return call(
        NodeIds.PublishSubscribe_PubSubConfiguration_Read,
        new Variant(fileHandle),
        new Variant(length));
  }

  CallResult write(UInteger fileHandle, ByteString data) throws UaException {
    return call(
        NodeIds.PublishSubscribe_PubSubConfiguration_Write,
        new Variant(fileHandle),
        new Variant(data));
  }

  CallResult getPosition(UInteger fileHandle) throws UaException {
    return call(NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition, new Variant(fileHandle));
  }

  CallResult setPosition(UInteger fileHandle, ULong position) throws UaException {
    return call(
        NodeIds.PublishSubscribe_PubSubConfiguration_SetPosition,
        new Variant(fileHandle),
        new Variant(position));
  }

  CallResult reserveIds(String transportProfileUri, UShort numWriterGroupIds, UShort numWriterIds)
      throws UaException {

    return call(
        NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
        new Variant(transportProfileUri),
        new Variant(numWriterGroupIds),
        new Variant(numWriterIds));
  }

  /**
   * CloseAndUpdate with the references packed as {@code ExtensionObject[]} — the on-the-wire shape
   * a real tool sends (never the typed-invoke shortcut; see the class Javadoc).
   */
  CallResult closeAndUpdate(
      UInteger fileHandle,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType @Nullable [] references)
      throws UaException {

    Variant referencesVariant =
        references == null
            ? Variant.NULL_VALUE
            : new Variant(
                ExtensionObject.encodeArray(client.getStaticEncodingContext(), references));

    return call(
        NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate,
        new Variant(fileHandle),
        new Variant(requireCompleteUpdate),
        referencesVariant);
  }

  // endregion

  // region helpers

  /** Open asserting Good; returns the handle. */
  UInteger openOk(UByte mode) throws UaException {
    CallResult result = open(mode);
    if (!result.status().isGood()) {
      throw new AssertionError("Open(0x%02X) failed: %s".formatted(mode.intValue(), result));
    }
    return (UInteger) result.outputs()[0].getValue();
  }

  /** Close asserting Good. */
  void closeOk(UInteger fileHandle) throws UaException {
    CallResult result = close(fileHandle);
    if (!result.status().isGood()) {
      throw new AssertionError("Close(" + fileHandle + ") failed: " + result);
    }
  }

  /** Write all of {@code data} in chunks, asserting Good on each chunk. */
  void writeAll(UInteger fileHandle, byte[] data) throws UaException {
    for (int offset = 0; offset < data.length; offset += CHUNK_SIZE) {
      int count = Math.min(CHUNK_SIZE, data.length - offset);
      byte[] chunk = new byte[count];
      System.arraycopy(data, offset, chunk, 0, count);
      CallResult result = write(fileHandle, ByteString.of(chunk));
      if (!result.status().isGood()) {
        throw new AssertionError("Write failed at offset " + offset + ": " + result);
      }
    }
  }

  /** Read the remaining stream, looping until the Good + empty-ByteString EOF signal. */
  byte[] readAll(UInteger fileHandle) throws UaException {
    var assembled = new ByteArrayOutputStream();
    while (true) {
      CallResult result = read(fileHandle, CHUNK_SIZE);
      if (!result.status().isGood()) {
        throw new AssertionError("Read failed: " + result);
      }
      ByteString chunk = (ByteString) result.outputs()[0].getValue();
      if (chunk == null || chunk.length() == 0) {
        return assembled.toByteArray();
      }
      assembled.writeBytes(chunk.bytesOrEmpty());
    }
  }

  /** Open a fresh read handle, read the whole file, close the handle. */
  byte[] readWholeFile() throws UaException {
    UInteger handle = openOk(UByte.valueOf(0x01));
    try {
      return readAll(handle);
    } finally {
      closeOk(handle);
    }
  }

  /** Read a node's Value attribute over the wire; the returned {@link DataValue} carries status. */
  DataValue readValue(NodeId nodeId) throws UaException {
    return client.readValues(0.0, TimestampsToReturn.Both, List.of(nodeId)).get(0);
  }

  // endregion

  // region CloseAndUpdate output unpackers

  static boolean changesApplied(CallResult result) {
    return Boolean.TRUE.equals(result.outputs()[0].getValue());
  }

  static StatusCode[] referencesResults(CallResult result) {
    Object value = result.outputs()[1].getValue();
    return value != null ? (StatusCode[]) value : new StatusCode[0];
  }

  /** Decode the ConfigurationValues output (arrives as {@code ExtensionObject[]} on the wire). */
  List<PubSubConfigurationValueDataType> configurationValues(CallResult result) {
    Object value = result.outputs()[2].getValue();

    var decoded = new ArrayList<PubSubConfigurationValueDataType>();
    if (value instanceof ExtensionObject[] xos) {
      for (ExtensionObject xo : xos) {
        decoded.add(
            (PubSubConfigurationValueDataType) xo.decode(client.getStaticEncodingContext()));
      }
    } else if (value instanceof PubSubConfigurationValueDataType[] values) {
      decoded.addAll(List.of(values));
    }
    return decoded;
  }

  static NodeId @Nullable [] configurationObjects(CallResult result) {
    return (NodeId[]) result.outputs()[3].getValue();
  }

  // endregion

  private CallResult call(NodeId methodId, Variant... inputs) throws UaException {
    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.PublishSubscribe_PubSubConfiguration, methodId, inputs)));

    CallMethodResult[] results = response.getResults();
    if (results == null || results.length != 1) {
      throw new AssertionError("expected exactly one call result");
    }

    Variant[] outputs = results[0].getOutputArguments();
    return new CallResult(results[0].getStatusCode(), outputs != null ? outputs : new Variant[0]);
  }
}

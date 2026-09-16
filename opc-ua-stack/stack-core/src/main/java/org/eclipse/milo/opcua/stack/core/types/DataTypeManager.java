/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types;

import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public interface DataTypeManager {

  /**
   * Register an application-owned codec, overwriting the type lookup and supplied encoding lookups.
   *
   * <p>Null and OPC UA null encoding ids leave existing mappings unchanged. These registrations
   * have no handle-managed lifetime. Use {@link #acquireType} to reject collisions and release
   * temporary registrations.
   */
  void registerType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId);

  /**
   * Acquire a codec registration without overwriting any existing lookup.
   *
   * <p>An existing registration can be shared only when the type id, codec instance, and all
   * encoding mappings match. Null and OPC UA null encoding ids both mean no encoding. A conflict
   * changes nothing. Each successful call returns an independent handle. Keep it while the codec is
   * needed, then close it to release this caller's use:
   *
   * <pre>{@code
   * try (var registration = manager.acquireType(typeId, codec, binaryId, null, null)) {
   *   // Encode or decode values while the registration is retained.
   * }
   * }</pre>
   *
   * <p>The last release removes a registration created by this method. Registrations installed by
   * {@link #registerType} are borrowed and are never removed by releasing handles. Later ordinary
   * registrations survive release of older handles, even if they reuse the same codec instance.
   * Acquisition, release, and ordinary registration must coordinate atomically with lookups.
   * Separate lookup calls do not form a snapshot.
   *
   * @return a thread-safe handle whose {@code close()} is idempotent and throws no checked
   *     exception.
   * @throws IllegalStateException if any requested id or mapping conflicts with an existing
   *     registration.
   * @throws UnsupportedOperationException if this manager does not support registration lifetime.
   */
  default RegistrationHandle acquireType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {
    throw new UnsupportedOperationException("Registration lifetime is not supported");
  }

  /** One caller's use of a codec registration. */
  interface RegistrationHandle extends AutoCloseable {
    /** Release this use once; subsequent calls have no effect. */
    @Override
    void close();
  }

  /**
   * Get the {@link DataTypeCodec} for the given {@link NodeId}.
   *
   * <p>The {@link NodeId} may be either the datatype or encoding id.
   *
   * @param id the {@link NodeId} of either the datatype or encoding.
   * @return the {@link DataTypeCodec} for the given {@link NodeId}.
   */
  @Nullable DataTypeCodec getCodec(NodeId id);

  /**
   * Resolves a registered data type or encoding id to its data type id without decoding a value.
   *
   * <p>Implementations that do not support identity lookup return {@code null}. Callers that need
   * to translate an opaque value between encodings must reject an unresolved identity rather than
   * assume the supplied id is a data type id.
   *
   * @param id the data type or encoding id to resolve.
   * @return the registered data type id, or {@code null} if unknown or unsupported.
   */
  default @Nullable NodeId getDataTypeId(NodeId id) {
    return null;
  }

  @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId);

  @Nullable NodeId getXmlEncodingId(NodeId dataTypeId);

  @Nullable NodeId getJsonEncodingId(NodeId dataTypeId);

  @Nullable DataTypeDictionary getTypeDictionary(String namespaceUri);

  void registerTypeDictionary(DataTypeDictionary dictionary);
}

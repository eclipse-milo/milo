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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/** A thread-safe registry of codecs and their encoding ids. */
public class DefaultDataTypeManager implements DataTypeManager {

  // All registration maps and handle state are guarded by this manager's monitor.
  private final Map<NodeId, Registration> types = new HashMap<>();
  private final Map<NodeId, Registration> encodings = new HashMap<>();
  private final Map<NodeId, Registration> binaryEncodings = new HashMap<>();
  private final Map<NodeId, Registration> xmlEncodings = new HashMap<>();
  private final Map<NodeId, Registration> jsonEncodings = new HashMap<>();
  private final Map<String, DataTypeDictionary> dataTypeDictionaries = new ConcurrentHashMap<>();

  @Override
  public synchronized void registerType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {
    install(
        new Registration(
            dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId, false));
  }

  @Override
  public synchronized RegistrationHandle acquireType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {
    var requested =
        new Registration(dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId, true);
    Registration existing = types.get(dataTypeId);
    if (existing != null) {
      if (existing.codec != codec
          || !Objects.equals(existing.binaryId, requested.binaryId)
          || !Objects.equals(existing.xmlId, requested.xmlId)
          || !Objects.equals(existing.jsonId, requested.jsonId)
          || encodings.containsKey(dataTypeId)
          || !matchesEncoding(existing, requested.binaryId, binaryEncodings)
          || !matchesEncoding(existing, requested.xmlId, xmlEncodings)
          || !matchesEncoding(existing, requested.jsonId, jsonEncodings)) {
        throw new IllegalStateException("Conflicting registration for " + dataTypeId);
      }
    } else {
      checkAvailable(dataTypeId);
      for (NodeId encodingId : requested.encodingIds()) {
        if (encodingId.equals(dataTypeId)) {
          throw new IllegalStateException("Encoding id is also the data type id: " + encodingId);
        }
        checkAvailable(encodingId);
      }
      // Legacy registrations can leave encoding mappings after their type lookup is replaced.
      if (binaryEncodings.containsKey(dataTypeId)
          || xmlEncodings.containsKey(dataTypeId)
          || jsonEncodings.containsKey(dataTypeId)) {
        throw new IllegalStateException("Conflicting encoding mappings for " + dataTypeId);
      }
      existing = requested;
      install(existing);
    }
    existing.users++;
    return new Handle(existing);
  }

  private boolean matchesEncoding(
      Registration registration, @Nullable NodeId id, Map<NodeId, Registration> byType) {
    return id == null
        ? !byType.containsKey(registration.typeId)
        : byType.get(registration.typeId) == registration
            && encodings.get(id) == registration
            && !types.containsKey(id);
  }

  private void checkAvailable(NodeId id) {
    if (types.containsKey(id) || encodings.containsKey(id)) {
      throw new IllegalStateException("Id is already registered: " + id);
    }
  }

  private void install(Registration registration) {
    types.put(registration.typeId, registration);
    putEncoding(registration, registration.binaryId, binaryEncodings);
    putEncoding(registration, registration.xmlId, xmlEncodings);
    putEncoding(registration, registration.jsonId, jsonEncodings);
  }

  private void putEncoding(
      Registration registration, @Nullable NodeId id, Map<NodeId, Registration> byType) {
    if (id != null) {
      encodings.put(id, registration);
      byType.put(registration.typeId, registration);
    }
  }

  @Override
  public synchronized @Nullable DataTypeCodec getCodec(NodeId id) {
    Registration registration = encodings.getOrDefault(id, types.get(id));
    return registration == null ? null : registration.codec;
  }

  @Override
  public synchronized @Nullable NodeId getDataTypeId(NodeId id) {
    Registration registration = encodings.get(id);
    if (registration == null) {
      registration = types.get(id);
      return registration == null ? null : registration.typeId;
    }
    // Ordinary replacement retains old codec lookups, but identity conversion must agree with
    // the current forward mapping or an opaque body could acquire a different encoding id.
    boolean current =
        (id.equals(registration.binaryId)
                && binaryEncodings.get(registration.typeId) == registration)
            || (id.equals(registration.xmlId)
                && xmlEncodings.get(registration.typeId) == registration)
            || (id.equals(registration.jsonId)
                && jsonEncodings.get(registration.typeId) == registration);
    return current ? registration.typeId : null;
  }

  @Override
  public synchronized @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    Registration registration = binaryEncodings.get(dataTypeId);
    return registration == null ? null : registration.binaryId;
  }

  @Override
  public synchronized @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    Registration registration = xmlEncodings.get(dataTypeId);
    return registration == null ? null : registration.xmlId;
  }

  @Override
  public synchronized @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    Registration registration = jsonEncodings.get(dataTypeId);
    return registration == null ? null : registration.jsonId;
  }

  @Override
  public @Nullable DataTypeDictionary getTypeDictionary(String namespaceUri) {
    return dataTypeDictionaries.get(namespaceUri);
  }

  @Override
  public void registerTypeDictionary(DataTypeDictionary dictionary) {
    dataTypeDictionaries.put(dictionary.getNamespaceUri(), dictionary);
  }

  private final class Handle implements RegistrationHandle {
    private final Registration registration;
    private boolean closed;

    private Handle(Registration registration) {
      this.registration = registration;
    }

    @Override
    public void close() {
      synchronized (DefaultDataTypeManager.this) {
        if (closed) {
          return;
        }
        closed = true;
        if (--registration.users == 0 && registration.managed) {
          types.remove(registration.typeId, registration);
          binaryEncodings.remove(registration.typeId, registration);
          xmlEncodings.remove(registration.typeId, registration);
          jsonEncodings.remove(registration.typeId, registration);
          for (NodeId id : registration.encodingIds()) {
            encodings.remove(id, registration);
          }
        }
      }
    }
  }

  // Object identity distinguishes ordinary registration calls, even for the same codec instance.
  private static final class Registration {
    final NodeId typeId;
    final DataTypeCodec codec;
    final @Nullable NodeId binaryId;
    final @Nullable NodeId xmlId;
    final @Nullable NodeId jsonId;
    final boolean managed;
    int users;

    Registration(
        NodeId typeId,
        DataTypeCodec codec,
        @Nullable NodeId binaryId,
        @Nullable NodeId xmlId,
        @Nullable NodeId jsonId,
        boolean managed) {
      this.typeId = Objects.requireNonNull(typeId);
      this.codec = Objects.requireNonNull(codec);
      this.binaryId = normalize(binaryId);
      this.xmlId = normalize(xmlId);
      this.jsonId = normalize(jsonId);
      this.managed = managed;
    }

    private static @Nullable NodeId normalize(@Nullable NodeId id) {
      return id == null || id.isNull() ? null : id;
    }

    List<NodeId> encodingIds() {
      var ids = new ArrayList<NodeId>(3);
      if (binaryId != null) ids.add(binaryId);
      if (xmlId != null) ids.add(xmlId);
      if (jsonId != null) ids.add(jsonId);
      return ids;
    }
  }

  /**
   * Create a {@link DefaultDataTypeManager} and initialize it by registering all built-in
   * DataTypes.
   *
   * @param namespaceTable a {@link NamespaceTable}.
   * @return a {@link DataTypeManager} pre-initialized wth the built-in DataTypes.
   */
  public static DataTypeManager createAndInitialize(NamespaceTable namespaceTable) {
    DefaultDataTypeManager dataTypeManager = new DefaultDataTypeManager();

    new DataTypeInitializer().initialize(namespaceTable, dataTypeManager);

    return dataTypeManager;
  }
}

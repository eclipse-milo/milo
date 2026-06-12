/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubComponents;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.jspecify.annotations.Nullable;

/**
 * Registry mapping component paths to handles and handles to runtime components; the {@link
 * PubSubComponents} implementation.
 *
 * <p>Handle identity is object identity: components replaced by reconfiguration get new handles,
 * and unregistering a component invalidates its handle (lookups return empty / null).
 */
final class HandleRegistry implements PubSubComponents {

  private final ConcurrentMap<String, PubSubHandle> handlesByPath = new ConcurrentHashMap<>();
  private final ConcurrentMap<PubSubHandle, AbstractComponentRuntime> componentsByHandle =
      new ConcurrentHashMap<>();

  void register(AbstractComponentRuntime component) {
    handlesByPath.put(component.path(), component.handle());
    componentsByHandle.put(component.handle(), component);
  }

  void unregister(AbstractComponentRuntime component) {
    handlesByPath.remove(component.path(), component.handle());
    componentsByHandle.remove(component.handle());
  }

  /** Look up the runtime component for {@code handle}, or null if it has been invalidated. */
  @Nullable AbstractComponentRuntime get(PubSubHandle handle) {
    return componentsByHandle.get(handle);
  }

  @Override
  public Optional<PubSubHandle> connection(String connectionName) {
    return find(connectionName, ComponentType.CONNECTION);
  }

  @Override
  public Optional<PubSubHandle> writerGroup(String connectionName, String writerGroupName) {
    return find(connectionName + "/" + writerGroupName, ComponentType.WRITER_GROUP);
  }

  @Override
  public Optional<PubSubHandle> dataSetWriter(
      String connectionName, String groupName, String writerName) {

    return find(connectionName + "/" + groupName + "/" + writerName, ComponentType.DATA_SET_WRITER);
  }

  @Override
  public Optional<PubSubHandle> readerGroup(String connectionName, String readerGroupName) {
    return find(connectionName + "/" + readerGroupName, ComponentType.READER_GROUP);
  }

  @Override
  public Optional<PubSubHandle> dataSetReader(
      String connectionName, String groupName, String readerName) {

    return find(connectionName + "/" + groupName + "/" + readerName, ComponentType.DATA_SET_READER);
  }

  private Optional<PubSubHandle> find(String path, ComponentType componentType) {
    PubSubHandle handle = handlesByPath.get(path);

    if (handle != null && handle.componentType() == componentType) {
      return Optional.of(handle);
    } else {
      return Optional.empty();
    }
  }
}

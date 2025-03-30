/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.jspecify.annotations.Nullable;

/** Manages an array of URIs and provides bidirectional access to the URIs their indices. */
public abstract class UriArray<I extends UNumber> {

  private final BiMap<I, String> uriMap = HashBiMap.create();

  /**
   * Add a URI and return the index.
   *
   * <p>If the URI is not already present the next available index is assigned, otherwise the
   * current index is returned.
   *
   * @param uri the namespace URI to add.
   * @return the index assigned to the URI.
   */
  public synchronized I add(String uri) {
    if (uriMap.containsValue(uri)) {
      return uriMap.inverse().get(uri);
    } else {
      I index = create(0);

      while (uriMap.containsKey(index)) {
        index = create(index.intValue() + 1);
        if (index.intValue() == 65535) {
          throw new UaRuntimeException(StatusCodes.Bad_InternalError, "uri table full");
        }
      }
      uriMap.put(index, uri);

      return index;
    }
  }

  /**
   * Get the URI at {@code index}, or {@code null} if there isn't one.
   *
   * @param index the index of the URI to get.
   * @return the URI at {@code index}, or {@code null} if there isn't one.
   */
  public synchronized String get(Number index) {
    return get(create(index));
  }

  /**
   * Get the URI at {@code index}, or {@code null} if there isn't one.
   *
   * @param index the index of the URI to get.
   * @return the URI at {@code index}, or {@code null} if there isn't one.
   */
  public synchronized String get(I index) {
    return uriMap.get(index);
  }

  /**
   * @param uri the URI to look up.
   * @return the index of the URI, or {@code null} if it is not present.
   */
  public synchronized @Nullable I getIndex(String uri) {
    return uriMap.inverse().getOrDefault(uri, null);
  }

  /**
   * Replace the URI at {@code index} with {@code uri}.
   *
   * @param index the index to replace.
   * @param uri the URI to replace with.
   * @return the previous URI at {@code index}, or {@code null} if there was none.
   */
  public synchronized String set(Number index, String uri) {
    return uriMap.put(create(index), uri);
  }

  /**
   * Replace the URI at {@code index} with {@code uri}.
   *
   * @param index the index to replace.
   * @param uri the URI to replace with.
   * @return the previous URI at {@code index}, or {@code null} if there was none.
   */
  public synchronized String set(I index, String uri) {
    return uriMap.put(index, uri);
  }

  /**
   * Update the underlying {@link BiMap}.
   *
   * @param uriTableConsumer the underlying {@link BiMap} instance.
   */
  public synchronized void update(Consumer<BiMap<I, String>> uriTableConsumer) {
    uriTableConsumer.accept(uriMap);
  }

  /**
   * @return an array of registered URIs, ordered by index ascending.
   */
  public synchronized String[] toArray() {
    return uriMap.entrySet().stream()
        .sorted(Comparator.comparingInt(e -> e.getKey().intValue()))
        .map(Map.Entry::getValue)
        .toArray(String[]::new);
  }

  /**
   * Create a new index of type {@link I} from {@code index}.
   *
   * @param index the index to create.
   * @return the new index.
   */
  protected abstract I create(Number index);
}

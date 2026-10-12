/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class GroupMapCollate {

  /**
   * Group {@code items} by key, map each group to results with the mapper for its key, and collate
   * the results back into the order of {@code items}.
   *
   * <p>Each mapper is called once, with a new mutable list holding its group's items in their
   * original order, and must return one result per item in the same order.
   *
   * @param items the items to group, map, and collate.
   * @param grouper returns the non-null group key for an item.
   * @param mappers returns the mapper for a group key.
   * @param <T> the item type.
   * @param <K> the group key type.
   * @param <R> the result type.
   * @return the results, in the order of {@code items}.
   * @throws NullPointerException if {@code grouper} returns {@code null} for an item.
   * @throws RuntimeException if a mapper returns a different number of results than items.
   */
  public static <T, K, R> List<R> groupMapCollate(
      List<T> items, Function<T, K> grouper, Function<K, Function<List<T>, List<R>>> mappers) {

    int size = items.size();
    var groups = new LinkedHashMap<K, Group<T>>();

    for (int i = 0; i < size; i++) {
      T item = items.get(i);
      K key = Objects.requireNonNull(grouper.apply(item), "element cannot be mapped to a null key");

      groups.computeIfAbsent(key, k -> new Group<>()).add(item, i);
    }

    var results = new Object[size];

    for (Map.Entry<K, Group<T>> entry : groups.entrySet()) {
      Group<T> group = entry.getValue();
      Function<List<T>, List<R>> mapper = mappers.apply(entry.getKey());

      // The mapper may change the list it is given, so the group's size is taken beforehand.
      int groupSize = group.items.size();
      List<R> groupResults = mapper.apply(group.items);

      if (groupResults.size() != groupSize) {
        String message =
            String.format(
                "result size (%s) does not match pending size (%s)",
                groupResults.size(), groupSize);
        throw new RuntimeException(message);
      }

      for (int i = 0; i < groupResults.size(); i++) {
        results[group.indices[i]] = groupResults.get(i);
      }
    }

    var collated = new ArrayList<R>(size);
    for (Object result : results) {
      @SuppressWarnings("unchecked")
      R r = (R) result;
      collated.add(r);
    }
    return collated;
  }

  /** The items that share a key, and each item's index in the original list. */
  private static final class Group<T> {
    final List<T> items = new ArrayList<>();
    int[] indices = new int[4];

    void add(T item, int index) {
      if (items.size() == indices.length) {
        indices = Arrays.copyOf(indices, indices.length * 2);
      }
      indices[items.size()] = index;
      items.add(item);
    }
  }
}

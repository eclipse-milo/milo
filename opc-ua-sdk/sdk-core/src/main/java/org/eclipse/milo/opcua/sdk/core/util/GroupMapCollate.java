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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupMapCollate {

  public static <T, K, R> List<R> groupMapCollate(
      List<T> items, Function<T, K> grouper, Function<K, Function<List<T>, List<R>>> mappers) {

    var pending = new ArrayList<Pending<T, R>>(items.size());

    for (int i = 0; i < items.size(); i++) {
      pending.add(new Pending<>(items.get(i), i));
    }

    Map<K, List<Pending<T, R>>> grouped =
        pending.stream().collect(Collectors.groupingBy(p -> grouper.apply(p.item)));

    grouped.forEach(
        (key, pendingForKey) -> {
          Function<List<T>, List<R>> mapper = mappers.apply(key);

          List<R> results =
              mapper.apply(pendingForKey.stream().map(p -> p.item).collect(Collectors.toList()));

          if (results.size() != pendingForKey.size()) {
            String message =
                String.format(
                    "result size (%s) does not match pending size (%s)",
                    results.size(), pendingForKey.size());
            throw new RuntimeException(message);
          }

          for (int i = 0; i < results.size(); i++) {
            pendingForKey.get(i).result = results.get(i);
          }
        });

    return pending.stream().map(p -> p.result).collect(Collectors.toList());
  }

  private static class Pending<T, R> {
    volatile R result;
    final T item;
    final int index;

    public Pending(T item, int index) {
      this.item = item;
      this.index = index;
    }
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.sdk.core.util.AsyncGroupMapCollate.groupMapCollate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class AsyncGroupMapCollateTest {

  @Test
  public void testGroupMapCollate() throws ExecutionException, InterruptedException {
    int N = 10;
    List<Integer> items = new ArrayList<>();
    IntStream.range(0, N).forEach(items::add);

    for (int i = 1; i <= N; i++) {
      final int mod = i;
      CompletableFuture<List<String>> stringsFuture =
          groupMapCollate(
              items,
              item -> item % mod,
              remainder ->
                  group -> {
                    System.out.println(
                        "mod=" + mod + " remainder=" + remainder + " group=" + group);

                    CompletableFuture<List<String>> future = new CompletableFuture<>();

                    future.complete(group.stream().map(Object::toString).collect(toList()));

                    return future;
                  });

      List<String> strings = stringsFuture.get();

      for (int j = 0; j < strings.size(); j++) {
        assertEquals(String.valueOf(j), strings.get(j));
      }
      System.out.println("--");
    }
  }
}

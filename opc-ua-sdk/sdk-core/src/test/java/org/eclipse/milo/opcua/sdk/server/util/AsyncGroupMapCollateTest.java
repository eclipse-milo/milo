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

import static org.eclipse.milo.opcua.sdk.core.util.AsyncGroupMapCollate.groupMapCollate;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AsyncGroupMapCollateTest {

  // Completion order must not change input order or publish a partial result.
  @ParameterizedTest(name = "{0}")
  @CsvSource({"single group, 1", "interleaved groups, 3", "one group per item, 10"})
  void collatesAllResultsAfterEveryGroupCompletes(String name, int partitions) throws Exception {
    List<Integer> items = IntStream.range(0, 10).boxed().toList();
    var futures = new ArrayList<CompletableFuture<List<String>>>();
    var results = new ArrayList<List<String>>();
    CompletableFuture<List<String>> aggregate =
        groupMapCollate(
            items,
            item -> item % partitions,
            key ->
                group -> {
                  var future = new CompletableFuture<List<String>>();
                  futures.add(future);
                  results.add(group.stream().map(Object::toString).toList());
                  return future;
                });

    for (int i = futures.size() - 1; i >= 0; i--) {
      assertFalse(aggregate.isDone(), "aggregate must wait for every group");
      futures.get(i).complete(results.get(i));
    }
    assertEquals(items.stream().map(Object::toString).toList(), aggregate.get(5, TimeUnit.SECONDS));
  }

  @Test
  void propagatesMapperFailure() {
    var failure = new IllegalStateException("mapper failed");
    CompletableFuture<List<String>> aggregate =
        groupMapCollate(
            List.of(1), item -> item, key -> group -> CompletableFuture.failedFuture(failure));
    var thrown = assertThrows(ExecutionException.class, () -> aggregate.get(5, TimeUnit.SECONDS));
    assertSame(failure, thrown.getCause());
  }

  @Test
  void rejectsMissingMappedResults() {
    CompletableFuture<List<String>> aggregate =
        groupMapCollate(
            List.of(1, 2),
            item -> 0,
            key -> group -> CompletableFuture.completedFuture(List.of("1")));
    var thrown = assertThrows(ExecutionException.class, () -> aggregate.get(5, TimeUnit.SECONDS));
    assertEquals("result size (1) does not match pending size (2)", thrown.getCause().getMessage());
  }
}

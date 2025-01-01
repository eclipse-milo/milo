/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

public final class Lists {

  private Lists() {}

  /**
   * Return an unmodifiable list containing the given elements.
   *
   * <p>This is a null-safe variation of {@link List#of()}.
   *
   * @param elements the elements to include in the list.
   * @return an unmodifiable list containing the given elements.
   */
  @SafeVarargs
  public static <T> List<T> ofNullable(@Nullable T... elements) {
    if (elements == null) {
      return List.of();
    } else {
      return List.of(elements);
    }
  }

  /**
   * Partition {@code source} into one or more {@link List}s of maximum size indicated {@code
   * partitionSize}.
   *
   * @param source the {@link List} to partition.
   * @param partitionSize the maximum size of each partition.
   * @return a {@link Stream} of {@link List}s, each no larger than {@code partitionSize}.
   */
  public static <T> Stream<List<T>> partition(List<T> source, int partitionSize) {
    int size = source.size();

    if (size == 0) {
      return Stream.empty();
    }

    int fullChunks = (size - 1) / partitionSize;

    return IntStream.range(0, fullChunks + 1)
        .mapToObj(
            n -> {
              int fromIndex = n * partitionSize;
              int toIndex = n == fullChunks ? size : (n + 1) * partitionSize;

              return source.subList(fromIndex, toIndex);
            });
  }
}

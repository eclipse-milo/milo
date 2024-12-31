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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

public class FutureUtils {

  public static <T> CompletionStage<T> supplyAsyncCompose(
      Supplier<CompletionStage<T>> supplier, Executor executor) {
    return CompletableFuture.supplyAsync(supplier, executor).thenCompose(Function.identity());
  }

  public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> stream) {
    return sequence(stream.collect(Collectors.toList()));
  }

  public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
    if (futures.isEmpty()) {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }

    CompletableFuture[] fa = futures.toArray(new CompletableFuture[0]);

    return CompletableFuture.allOf(fa)
        .thenApply(
            v -> {
              List<T> results = new ArrayList<>(futures.size());

              for (CompletableFuture<T> cf : futures) {
                results.add(cf.join());
              }

              return results;
            });
  }

  public static <T> CompletableFuture<List<T>> sequence(CompletableFuture<T>... futures) {
    if (futures.length == 0) {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }

    return CompletableFuture.allOf(futures)
        .thenApply(
            v -> {
              List<T> results = new ArrayList<>(futures.length);

              for (CompletableFuture<T> cf : futures) {
                results.add(cf.join());
              }

              return results;
            });
  }

  /**
   * Return a {@link CompletableFuture} that has been completed exceptionally using the provided
   * {@link Throwable}.
   *
   * @param ex the {@link Throwable} to complete with.
   * @return a {@link CompletableFuture} that has been completed exceptionally using the provided
   *     {@link Throwable}.
   */
  public static <T> CompletableFuture<T> failedFuture(Throwable ex) {
    CompletableFuture<T> f = new CompletableFuture<>();
    f.completeExceptionally(ex);
    return f;
  }

  /**
   * Return a {@link CompletableFuture} that has been completed exceptionally with a {@link
   * UaException} built from {@code statusCode}.
   *
   * @param statusCode the status code to build the {@link UaException} with.
   * @return a {@link CompletableFuture} that has been completed exceptionally with a {@link
   *     UaException} built from {@code statusCode}.
   */
  public static <T> CompletableFuture<T> failedUaFuture(long statusCode) {
    return failedUaFuture(new StatusCode(statusCode));
  }

  /**
   * Return a {@link CompletableFuture} that has been completed exceptionally with a {@link
   * UaException} built from {@code statusCode}.
   *
   * @param statusCode the {@link StatusCode} to build the {@link UaException} with.
   * @return a {@link CompletableFuture} that has been completed exceptionally with a {@link
   *     UaException} built from {@code statusCode}.
   */
  public static <T> CompletableFuture<T> failedUaFuture(StatusCode statusCode) {
    return failedFuture(new UaException(statusCode));
  }

  /**
   * Return a {@link CompletableFuture} that has been completed exceptionally with a {@link
   * UaException} built from {@code statusCode}.
   *
   * @param cause the inner {@link Exception}.
   * @param statusCode the status code to build the {@link UaException} with.
   * @return a {@link CompletableFuture} that has been completed exceptionally with a {@link
   *     UaException} built from {@code statusCode}.
   */
  public static <T> CompletableFuture<T> failedUaFuture(Throwable cause, long statusCode) {
    return failedFuture(new UaException(statusCode, cause));
  }

  /**
   * Complete {@code future} with the result of the {@link CompletableFuture} that is provided to
   * the returned {@link CompletionBuilder}.
   *
   * @param future the future to complete.
   * @return a {@link CompletionBuilder}.
   */
  public static <T> CompletionBuilder<T> complete(CompletableFuture<T> future) {
    return new CompletionBuilder<>(future);
  }

  /**
   * Complete {@code future} asynchronously with the result of the {@link CompletableFuture} that is
   * provided to the returned {@link CompletionBuilder}.
   *
   * @param future the future to complete.
   * @param executor the {@link Executor} to use.
   * @return a {@link CompletionBuilder}.
   */
  public static <T> CompletionBuilder<T> completeAsync(
      CompletableFuture<T> future, Executor executor) {
    return new AsyncCompletionBuilder<>(future, executor);
  }

  /**
   * Flatten a List of future Lists into a CompletableFuture of List
   *
   * @param futures a List of future Lists
   * @param <T> the type of item the List holds
   * @return a flattened CompletableFuture of List.
   */
  public static <T> CompletableFuture<List<T>> flatSequence(
      List<CompletableFuture<List<T>>> futures) {
    return FutureUtils.sequence(futures)
        .thenApply(
            listOfListOfT ->
                listOfListOfT.stream().flatMap(Collection::stream).collect(Collectors.toList()));
  }

  /**
   * Unwrap a CompletableFuture of CompletableFuture of {@code T} into a CompletableFuture of {@code
   * T}.
   *
   * @param future a CompletableFuture of CompletableFuture of {@code T}.
   * @return an unwrapped CompletableFuture of {@code T}.
   */
  public static <T> CompletableFuture<T> unwrap(CompletableFuture<CompletableFuture<T>> future) {
    return future.thenCompose(Function.identity());
  }

  public static class CompletionBuilder<T> {

    final CompletableFuture<T> toComplete;

    private CompletionBuilder(CompletableFuture<T> toComplete) {
      this.toComplete = toComplete;
    }

    /**
     * Turn this {@link CompletionBuilder} into an {@link AsyncCompletionBuilder}.
     *
     * @param executor the {@link Executor} to use for the async completion.
     * @return an {@link AsyncCompletionBuilder}.
     */
    public CompletionBuilder<T> async(Executor executor) {
      return new AsyncCompletionBuilder<>(toComplete, executor);
    }

    /**
     * Complete the contained to-be-completed {@link CompletableFuture} using the result of {@code
     * future}.
     *
     * @param future the {@link CompletableFuture} to use as the result for the contained future.
     * @return the original, to-be-completed future provided to this {@link CompletionBuilder}.
     */
    public CompletableFuture<T> with(CompletableFuture<T> future) {
      future.whenComplete(
          (v, ex) -> {
            if (ex != null) toComplete.completeExceptionally(ex);
            else toComplete.complete(v);
          });

      return toComplete;
    }
  }

  private static final class AsyncCompletionBuilder<T> extends CompletionBuilder<T> {

    private final Executor executor;

    AsyncCompletionBuilder(CompletableFuture<T> toComplete, Executor executor) {
      super(toComplete);

      this.executor = executor;
    }

    @Override
    public CompletableFuture<T> with(CompletableFuture<T> future) {
      future.whenCompleteAsync(
          (v, ex) -> {
            if (ex != null) toComplete.completeExceptionally(ex);
            else toComplete.complete(v);
          },
          executor);

      return toComplete;
    }
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

/**
 * Limits applied to alias lookup and mutation calls.
 *
 * <p>All limits are enforced before any expensive work happens: patterns are length-checked before
 * parsing, array arguments are length-checked before validation, and searches stop as soon as the
 * result cap is exceeded.
 *
 * @param maxResults the maximum number of result entries a single {@code FindAlias} or {@code
 *     FindAliasVerbose} call may produce. Exceeding it fails the call with {@code
 *     Bad_ResponseTooLarge}; Part 17 defines no paging, so the Client must narrow its pattern.
 * @param maxPatternLength the maximum length, in {@code char}s, of a search pattern. Longer
 *     patterns fail the call with {@code Bad_InvalidArgument}.
 * @param maxOperationsPerCall the maximum number of entries in the array arguments of a single
 *     {@code AddAliasesToCategory} or {@code DeleteAliasesFromCategory} call. Longer arrays fail
 *     the call with {@code Bad_TooManyOperations}. Each entry locates aliases by scanning the
 *     category's directly organized members, so a call costs O(entries × category size) under the
 *     manager lock; servers with very large categories should size this limit accordingly.
 */
public record AliasLimits(int maxResults, int maxPatternLength, int maxOperationsPerCall) {

  /**
   * Create an {@link AliasLimits}.
   *
   * @throws IllegalArgumentException if any limit is not positive.
   */
  public AliasLimits {
    if (maxResults <= 0) {
      throw new IllegalArgumentException("maxResults must be positive: " + maxResults);
    }
    if (maxPatternLength <= 0) {
      throw new IllegalArgumentException("maxPatternLength must be positive: " + maxPatternLength);
    }
    if (maxOperationsPerCall <= 0) {
      throw new IllegalArgumentException(
          "maxOperationsPerCall must be positive: " + maxOperationsPerCall);
    }
  }

  /**
   * The default limits: 1000 results, 512 pattern chars, 1000 operations per call.
   *
   * @return the default {@link AliasLimits}.
   */
  public static AliasLimits defaults() {
    return new AliasLimits(1000, 512, 1000);
  }
}

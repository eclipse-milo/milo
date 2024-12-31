/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import com.google.common.base.Preconditions;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

public class EncodingLimits {

  /** An {@link EncodingLimits} that uses the default settings. */
  public static final EncodingLimits DEFAULT = new EncodingLimits();

  /** The default maximum size of a message; 2MB by default. */
  public static final int DEFAULT_MAX_MESSAGE_SIZE = 2 * 1024 * 1024;

  /** The default maximum size of a single chunk. */
  public static final int DEFAULT_MAX_CHUNK_SIZE = 65535;

  /**
   * The default maximum number of chunks that a message can break down into.
   *
   * <p>More than chunks than constitute {@link #DEFAULT_MAX_MESSAGE_SIZE} are needed because of
   * overhead when constructing chunks; not all of the chunk size is dedicated to message bytes.
   */
  public static final int DEFAULT_MAX_CHUNK_COUNT =
      (DEFAULT_MAX_MESSAGE_SIZE / DEFAULT_MAX_CHUNK_SIZE) * 2;

  /**
   * Max recursion depth allowed when decoding structures that can nest indefinitely, such as {@link
   * DiagnosticInfo} and {@link Variant}.
   */
  public static final int DEFAULT_MAX_RECURSION_DEPTH = 128;

  private final int maxChunkSize;
  private final int maxChunkCount;
  private final int maxMessageSize;
  private final int maxRecursionDepth;

  /**
   * Create a {@link EncodingLimits} using the default parameters.
   *
   * @see EncodingLimits#DEFAULT_MAX_CHUNK_SIZE
   * @see EncodingLimits#DEFAULT_MAX_CHUNK_COUNT
   * @see EncodingLimits#DEFAULT_MAX_MESSAGE_SIZE
   */
  public EncodingLimits() {
    this(
        DEFAULT_MAX_CHUNK_SIZE,
        DEFAULT_MAX_CHUNK_COUNT,
        DEFAULT_MAX_MESSAGE_SIZE,
        DEFAULT_MAX_RECURSION_DEPTH);
  }

  public EncodingLimits(
      int maxChunkSize, int maxChunkCount, int maxMessageSize, int maxRecursionDepth) {

    Preconditions.checkArgument(
        maxChunkSize >= 8196, "maxChunkSize must be greater than or equal to 8196");

    this.maxChunkSize = maxChunkSize;
    this.maxChunkCount = maxChunkCount;
    this.maxMessageSize = maxMessageSize;
    this.maxRecursionDepth = maxRecursionDepth;
  }

  public int getMaxChunkSize() {
    return maxChunkSize;
  }

  public int getMaxChunkCount() {
    return maxChunkCount;
  }

  public int getMaxMessageSize() {
    return maxMessageSize;
  }

  public int getMaxRecursionDepth() {
    return maxRecursionDepth;
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.jspecify.annotations.Nullable;

/**
 * Continuation point used to store excess references from a Browse service call for use in
 * subsequent BrowseNext service call.
 *
 * @param id a unique identifier for this continuation point.
 * @param references the references that were not returned in the initial Browse service call.
 * @param max the maximum number of references specified in the original Browse service call.
 */
public record ContinuationPoint(ByteString id, List<ReferenceDescription> references, int max) {

  public ContinuationPoint(List<ReferenceDescription> references, int max) {
    this(NonceUtil.generateNonce(16), references, max);
  }

  /**
   * Create a continuation point backed by a random-access slice of {@code references} starting at
   * {@code offset}, avoiding copying the remaining tail.
   *
   * @param references the full list of references from the initial Browse operation.
   * @param offset the starting index of the remaining references.
   * @param max the maximum number of references per page.
   * @return a new {@link ContinuationPoint} positioned at {@code offset}.
   */
  public static ContinuationPoint ofRemaining(
      List<ReferenceDescription> references, int offset, int max) {

    return new ContinuationPoint(slice(references, offset), max);
  }

  static List<ReferenceDescription> slice(List<ReferenceDescription> references, int offset) {
    Objects.checkFromIndexSize(offset, 0, references.size());

    if (references instanceof ReferenceSlice existing) {
      return existing.advance(offset);
    } else {
      return new ReferenceSlice(List.copyOf(references), offset);
    }
  }

  /**
   * One page of references produced when advancing a {@link ContinuationPoint} during BrowseNext.
   *
   * @param continuationPoint the continuation point identifier to return to the client if more
   *     references remain, or {@code null} when this page exhausts the continuation point.
   * @param references the references to return in this page.
   */
  public record Page(
      @Nullable ByteString continuationPoint, List<ReferenceDescription> references) {}

  /**
   * Unmodifiable random-access view of a backing reference list starting at an offset. Advancing
   * creates a flat slice against the same backing list so repeated BrowseNext calls stay O(page
   * size) instead of copying the remaining tail or nesting subList views.
   */
  private static final class ReferenceSlice extends AbstractList<ReferenceDescription>
      implements RandomAccess {

    private final List<ReferenceDescription> backing;
    private final int offset;

    ReferenceSlice(List<ReferenceDescription> backing, int offset) {
      this.backing = backing;
      this.offset = offset;
    }

    @Override
    public ReferenceDescription get(int index) {
      Objects.checkIndex(index, size());
      return backing.get(offset + index);
    }

    @Override
    public int size() {
      return backing.size() - offset;
    }

    ReferenceSlice advance(int count) {
      Objects.checkFromIndexSize(count, 0, size());
      return new ReferenceSlice(backing, offset + count);
    }
  }
}

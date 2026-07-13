/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayList;
import java.util.List;

final class ChunkBufferAccumulator {

  private final int initialCapacity;

  private List<ByteBuf> chunkBuffers;

  ChunkBufferAccumulator() {
    this(0);
  }

  ChunkBufferAccumulator(int initialCapacity) {
    this.initialCapacity = initialCapacity;

    chunkBuffers = new ArrayList<>(initialCapacity);
  }

  void add(ByteBuf buffer) {
    chunkBuffers.add(buffer.retain());
  }

  int size() {
    return chunkBuffers.size();
  }

  List<ByteBuf> takeAll() {
    List<ByteBuf> buffers = chunkBuffers;
    chunkBuffers = new ArrayList<>(initialCapacity);
    return buffers;
  }

  void releaseAll() {
    List<ByteBuf> buffers = takeAll();
    buffers.forEach(ReferenceCountUtil::safeRelease);
    buffers.clear();
  }
}

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

import java.util.concurrent.ExecutorService;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;

public class SerializationQueue {

  private static final int MAX_QUEUE_SIZE =
      Integer.getInteger("milo.stack.serialization.maxQueueSize", 256);

  private final OpcUaBinaryEncoder binaryEncoder;
  private final OpcUaBinaryDecoder binaryDecoder;

  private final ChunkEncoder chunkEncoder;
  private final ChunkDecoder chunkDecoder;

  private final TaskQueue encodingQueue;
  private final TaskQueue decodingQueue;

  private final ChannelParameters parameters;

  public SerializationQueue(
      ExecutorService executor, ChannelParameters parameters, EncodingContext context) {

    this.parameters = parameters;

    chunkEncoder = new ChunkEncoder(parameters);
    chunkDecoder = new ChunkDecoder(parameters, context.getEncodingLimits());

    binaryEncoder = new OpcUaBinaryEncoder(context);
    binaryDecoder = new OpcUaBinaryDecoder(context);

    encodingQueue = new TaskQueue(executor);

    decodingQueue =
        TaskQueue.newBuilder().setExecutor(executor).setMaxQueueSize(MAX_QUEUE_SIZE).build();
  }

  public boolean encode(Encoder encoder) {
    return encodingQueue.execute(() -> encoder.encode(binaryEncoder, chunkEncoder));
  }

  public boolean decode(Decoder decoder) {
    return decodingQueue.execute(() -> decoder.decode(binaryDecoder, chunkDecoder));
  }

  public void pause() {
    encodingQueue.pause();
    decodingQueue.pause();
  }

  public ChannelParameters getParameters() {
    return parameters;
  }

  @FunctionalInterface
  public interface Decoder {
    void decode(OpcUaBinaryDecoder binaryDecoder, ChunkDecoder chunkDecoder);
  }

  @FunctionalInterface
  public interface Encoder {
    void encode(OpcUaBinaryEncoder binaryEncoder, ChunkEncoder chunkEncoder);
  }
}

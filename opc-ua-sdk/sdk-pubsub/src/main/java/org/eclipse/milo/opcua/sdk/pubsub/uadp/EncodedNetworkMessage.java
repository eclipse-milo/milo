/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import io.netty.buffer.ByteBuf;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * An encoded NetworkMessage, ready to be sent by a transport channel, attributed to the
 * DataSetWriters whose DataSetMessages (or DataSetMetaData) it carries.
 *
 * <p>The attribution lets the engine and broker transports route per-writer queues: a
 * NetworkMessage carrying a single writer's messages can be published to that writer's queue.
 *
 * @param data the buffer containing the encoded NetworkMessage; the receiver of this record assumes
 *     ownership and is responsible for releasing it.
 * @param writers the contributing DataSetWriters, in payload order; empty when the message has no
 *     writer attribution (e.g. discovery probes).
 * @apiNote Create instances via {@link #of(ByteBuf, List)} rather than the canonical constructor;
 *     the factory methods are stable while the canonical constructor is not.
 */
public record EncodedNetworkMessage(ByteBuf data, List<Writer> writers) {

  /**
   * Create a new {@link EncodedNetworkMessage}.
   *
   * @param data the buffer containing the encoded NetworkMessage; the receiver of this record
   *     assumes ownership and is responsible for releasing it.
   * @param writers the contributing DataSetWriters, in payload order; empty when the message has no
   *     writer attribution.
   */
  public EncodedNetworkMessage {
    writers = List.copyOf(writers);
  }

  /**
   * Create a new {@link EncodedNetworkMessage} without writer attribution.
   *
   * @param data the buffer containing the encoded NetworkMessage; the receiver of this record
   *     assumes ownership and is responsible for releasing it.
   */
  public EncodedNetworkMessage(ByteBuf data) {
    this(data, List.of());
  }

  /**
   * Create an {@link EncodedNetworkMessage}.
   *
   * @param data the buffer containing the encoded NetworkMessage; the receiver of this record
   *     assumes ownership and is responsible for releasing it.
   * @param writers the contributing DataSetWriters, in payload order; empty when the message has no
   *     writer attribution.
   * @return a new {@link EncodedNetworkMessage}.
   */
  public static EncodedNetworkMessage of(ByteBuf data, List<Writer> writers) {
    return new EncodedNetworkMessage(data, writers);
  }

  /**
   * Attribution of an encoded NetworkMessage to one contributing DataSetWriter.
   *
   * @param name the DataSetWriter name.
   * @param dataSetWriterId the DataSetWriterId.
   */
  public record Writer(String name, UShort dataSetWriterId) {}
}

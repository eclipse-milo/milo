/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;

/**
 * An async MPSC (multi-producer, single-consumer) implementation of {@link SampleSink}.
 *
 * <p>Uses an {@link ExecutionQueue} to ensure values are applied serially to UaNodes while allowing
 * concurrent producers.
 *
 * <p>Provides:
 *
 * <ul>
 *   <li><b>Near-immediate processing</b> - values applied as soon as posted
 *   <li><b>Serial execution</b> - single-threaded access to UaNodes (no concurrent modification)
 *   <li><b>No dedicated thread</b> - uses the server's executor pool
 *   <li><b>Inline optimization</b> - reduces context switches when tasks complete and more are
 *       queued
 * </ul>
 *
 * <p><b>Silent failure behavior</b>: Values are silently skipped (not applied) if:
 *
 * <ul>
 *   <li>The node does not exist in the {@link UaNodeManager}
 *   <li>The {@link org.eclipse.milo.opcua.stack.core.AttributeId AttributeId} is invalid (null)
 * </ul>
 *
 * <p>This is intentional - nodes may be deleted between sampling and value application, and logging
 * every skip would be noisy.
 *
 * @see BlockingValueChannel
 * @see SampleSink
 */
public class AsyncValueChannel implements SampleSink {

  private final Queue<Sample> queue = new ConcurrentLinkedQueue<>();
  private final ExecutionQueue applyQueue;
  private final UaNodeManager nodeManager;

  /**
   * Create a new AsyncValueChannel.
   *
   * @param executor the executor to use for processing values.
   * @param nodeManager the UaNodeManager used to look up nodes.
   */
  public AsyncValueChannel(Executor executor, UaNodeManager nodeManager) {
    this.applyQueue = new ExecutionQueue(executor);
    this.nodeManager = nodeManager;
  }

  @Override
  public void post(Sample sample) {
    queue.add(sample);
    applyQueue.submit(this::drainAndApply);
  }

  private void drainAndApply() {
    Sample sample;
    while ((sample = queue.poll()) != null) {
      for (SampledValue sv : sample.values()) {
        SampledItem item = sv.item();
        AttributeId attributeId = item.getAttributeId();
        if (attributeId == null) {
          continue;
        }
        UaServerNode node = nodeManager.get(item.getNodeId());
        if (node != null) {
          if (attributeId == AttributeId.Value) {
            StatusCode status = sv.statusCode() != null ? sv.statusCode() : StatusCode.GOOD;
            DateTime time = sv.sourceTime() != null ? sv.sourceTime() : sample.timestamp();
            DataValue dataValue = new DataValue(sv.value(), status, time, null);
            node.setAttribute(AccessContext.INTERNAL, attributeId, dataValue);
          } else {
            node.setAttribute(AccessContext.INTERNAL, attributeId, sv.value());
          }
        }
      }
    }
  }
}

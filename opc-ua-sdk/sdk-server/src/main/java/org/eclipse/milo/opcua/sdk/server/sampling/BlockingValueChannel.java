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

import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * A blocking implementation of {@link SampleSink} that applies values directly to UaNodes without
 * queueing.
 *
 * <p>Used for immediate sampling where values must be applied to UaNodes before internal sampling
 * reads from them.
 */
public class BlockingValueChannel implements SampleSink {

  private final UaNodeManager nodeManager;

  /**
   * Create a new BlockingValueChannel.
   *
   * @param nodeManager the UaNodeManager used to look up nodes.
   */
  public BlockingValueChannel(UaNodeManager nodeManager) {
    this.nodeManager = nodeManager;
  }

  @Override
  public void post(Sample sample) {
    for (SampledValue sv : sample.values()) {
      SampledItem item = sv.item();

      UaServerNode node = nodeManager.get(item.getNodeId());
      if (node != null) {
        AttributeId attributeId = item.getAttributeId();
        if (attributeId != null) {
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

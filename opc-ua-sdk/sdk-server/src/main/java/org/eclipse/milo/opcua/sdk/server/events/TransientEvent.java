/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import org.eclipse.milo.opcua.sdk.server.EventNotifier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * A transient Event instance whose backing Event Node tree lives in a NodeManager that is never
 * registered with the Server's AddressSpaceManager, and is deleted when this instance is closed.
 *
 * <p>Typical usage, with the Node tree deleted on close even if an exception is thrown:
 *
 * <pre>{@code
 * try (TransientEvent e = server.newEvent(NodeIds.SystemOffNormalAlarmType)) {
 *   // populate fields via e.getNode()...
 *   e.fire();
 * }
 * }</pre>
 *
 * @see OpcUaServer#newEvent(NodeId)
 */
public final class TransientEvent implements AutoCloseable {

  private boolean closed = false;

  private final OpcUaServer server;
  private final BaseEventTypeNode eventNode;

  public TransientEvent(OpcUaServer server, BaseEventTypeNode eventNode) {
    this.server = server;
    this.eventNode = eventNode;
  }

  /**
   * Get the backing Event Node, used to populate Event fields before calling {@link #fire()}.
   *
   * @return the backing {@link BaseEventTypeNode}.
   */
  public BaseEventTypeNode getNode() {
    return eventNode;
  }

  /**
   * Fire this Event through the Server's {@link EventNotifier}.
   *
   * @throws IllegalStateException if this instance has been closed.
   */
  public synchronized void fire() {
    if (closed) {
      throw new IllegalStateException("TransientEvent is closed");
    }

    server.getEventNotifier().fire(eventNode);
  }

  /**
   * Delete the backing Event Node tree.
   *
   * <p>Closing more than once is a no-op.
   */
  @Override
  public synchronized void close() {
    if (!closed) {
      closed = true;

      eventNode.delete();
    }
  }
}

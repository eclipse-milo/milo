/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;

/**
 * Observes the info model fragment building and removing per-component subtrees, on the initial
 * build and on incremental reconfiguration rebuilds. Diagnostics exposure uses these callbacks to
 * attach and remove per-component objects alongside the fragment's own component subtree lifecycle.
 *
 * <p>Fired for connections, writer/reader groups, and dataset writers/readers only — never for
 * published datasets (no diagnostics type attaches to them). {@link #onComponentBuilt} fires after
 * the component's subtree (including its Status object and any Enable/Disable Methods) is complete;
 * nested components report before their container. {@link #onComponentRemoving} fires before a
 * reconfigure-driven subtree delete, once per component in the subtree, children before parents.
 *
 * <p><b>Asymmetry:</b> {@code onComponentRemoving} is NOT fired at fragment shutdown — the
 * fragment's whole node manager is unregistered wholesale and implementations handle that through
 * their own lifecycle. Only reconfiguration-driven removals (and the removal half of a restart) are
 * observed.
 *
 * <p>Callbacks run on the fragment's building thread: the startup thread for the initial build, or
 * the reconfiguring caller inside {@link ManagedPubSubService}'s critical section for rebuilds.
 * Implementations must not call {@code reconfigure}/{@code update} (the reentrant critical section
 * would nest an apply inside a hook pass) and must attach any children they create to {@code
 * componentNode} via HasChild-subtype references so the recursive subtree delete cascades over
 * them. A callback failure is logged and isolated; it never fails the build or rebuild.
 *
 * <p>Registration via {@link PubSubInfoModelFragment#addComponentNodeListener} must complete before
 * {@code ServerPubSub.startup()}; the listener list is not synchronized against concurrent
 * registration.
 */
interface ComponentNodeListener {

  /**
   * Notified after the fragment finished building the subtree of a component.
   *
   * @param type the component's type; one of {@link ComponentType#CONNECTION}, {@link
   *     ComponentType#WRITER_GROUP}, {@link ComponentType#READER_GROUP}, {@link
   *     ComponentType#DATA_SET_WRITER}, or {@link ComponentType#DATA_SET_READER}.
   * @param namePath the component's {@code "/"}-joined name path, e.g. {@code "conn/group/writer"}.
   * @param componentNode the component's object node, already added to the fragment's node manager.
   */
  void onComponentBuilt(ComponentType type, String namePath, UaObjectNode componentNode);

  /**
   * Notified before the fragment deletes the subtree containing the component, on
   * reconfigure-driven removals and restarts (never at fragment shutdown).
   *
   * @param type the component's type (same set as {@link #onComponentBuilt}).
   * @param namePath the component's {@code "/"}-joined name path.
   */
  void onComponentRemoving(ComponentType type, String namePath);
}

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

import java.util.Comparator;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * A single target of an alias: the Node an alias name resolves to, and the ReferenceType that
 * associates the alias with it.
 *
 * <p>An alias may have any number of targets; Clients treat the order they are returned in as an
 * order of preference. Target ordering is controlled by {@link
 * AliasManagerConfig#getTargetOrdering()}.
 *
 * @param nodeId the NodeId of the target Node. May identify a Node on a remote Server, in which
 *     case {@code serverUri} identifies that Server.
 * @param serverUri the URI of the Server the target Node resides on, or {@code null} if the target
 *     is on the local Server.
 * @param referenceTypeId the NodeId of the ReferenceType associating the alias with the target.
 *     Must be {@code AliasFor} or a subtype; this is validated where the target is applied, not at
 *     construction, because subtype checks require the Server's ReferenceType hierarchy.
 */
public record AliasTarget(
    ExpandedNodeId nodeId, @Nullable String serverUri, NodeId referenceTypeId) {

  /**
   * The default target ordering: local targets before remote ones, then by the target NodeId's
   * parseable string form, so results are deterministic.
   *
   * <p>This is the single definition of the default, applied by {@link AliasManagerConfig.Builder}
   * when no ordering is configured.
   */
  public static final Comparator<AliasTarget> DEFAULT_ORDERING =
      Comparator.comparing((AliasTarget target) -> !target.isLocal())
          .thenComparing(target -> target.nodeId().toParseableString());

  /**
   * Whether this target resides on the local Server.
   *
   * @return {@code true} if this target resides on the local Server, i.e. {@link #serverUri()} is
   *     {@code null}.
   */
  public boolean isLocal() {
    return serverUri == null;
  }
}

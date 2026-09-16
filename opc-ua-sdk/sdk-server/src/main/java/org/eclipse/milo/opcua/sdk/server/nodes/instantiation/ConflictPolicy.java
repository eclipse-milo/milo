/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

/**
 * How a plan treats a planned {@link org.eclipse.milo.opcua.stack.core.types.builtin.NodeId} that
 * already exists in the request's target {@link org.eclipse.milo.opcua.sdk.server.NodeManager}.
 *
 * <p>There is deliberately no silent-replace option: the legacy factory's unconditional replacement
 * destroyed existing nodes and left their stale reference rows behind.
 */
public enum ConflictPolicy {

  /**
   * Any collision with an existing Node is a plan error ({@link
   * InstantiationDiagnostic.Code#NODE_ID_COLLISION}). The default.
   */
  FAIL,

  /**
   * A colliding existing Node is adopted (planned as {@link PlannedNode.Materialization#REUSE},
   * never modified, never deleted by cleanup) if it is compatible: equal namespace-qualified
   * BrowseName, equal NodeClass, and a TypeDefinition that is the planned type or a subtype. An
   * incompatible existing Node is a plan error ({@link
   * InstantiationDiagnostic.Code#INCOMPATIBLE_REUSE}).
   */
  REUSE_COMPATIBLE
}

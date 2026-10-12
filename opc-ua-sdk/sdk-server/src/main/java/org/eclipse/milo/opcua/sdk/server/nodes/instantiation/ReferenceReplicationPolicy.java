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
 * How declaration reference rows ({@link ReferenceRow}) are replicated onto instances. The spec
 * makes non-hierarchical replication server-specific (Part 3 §6.4.4.2), so both defaults are
 * overridable as a named policy.
 *
 * <p>The policy governs only the model's non-hierarchy rows. Hierarchy edges ({@link
 * DeclarationEdge}) and each instance's {@code HasTypeDefinition} reference are structural and
 * always materialized; {@code HasModellingRule} replication is governed by {@link
 * InstantiationPurpose}, not by this policy.
 *
 * @param internal how rows whose both ends are declarations of the model are treated.
 * @param external how rows targeting nodes outside the model are treated.
 */
public record ReferenceReplicationPolicy(InternalReferences internal, ExternalReferences external) {

  /**
   * The default policy: internal rows re-mapped onto the corresponding instances ({@link
   * InternalReferences#REMAP}), external rows copied verbatim ({@link ExternalReferences#COPY}).
   */
  public static final ReferenceReplicationPolicy DEFAULT =
      new ReferenceReplicationPolicy(InternalReferences.REMAP, ExternalReferences.COPY);

  /** Treatment of rows whose source and target are both declarations of the model. */
  public enum InternalReferences {

    /**
     * Rewrite the row onto the corresponding instance pair, exactly once, preserving the
     * ReferenceType and direction — direct-connection consistency per Part 3 §6.4.3 (the
     * state-machine {@code HasCause}/{@code HasEffect} workload).
     */
    REMAP,

    /** Do not replicate internal non-hierarchical rows (spec-legal per Part 3 §6.4.4.2). */
    OMIT
  }

  /** Treatment of rows targeting nodes outside the model. */
  public enum ExternalReferences {

    /** Copy the row verbatim: same ReferenceType, direction, and absolute target. */
    COPY,

    /** Do not replicate external rows. */
    OMIT
  }
}

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
 * How Method declarations are realized on instances.
 *
 * <p>Sharing is limited to Methods: they are stateless invocation points, the one node kind the
 * spec community commonly shares (Part 3 §6.4.3 permits it), and the only sharing with observed
 * user demand. {@code bindMethod} works under either mode.
 */
public enum MethodInstantiation {

  /**
   * Each instance gets its own Method node (and argument Property copies). The default; naive
   * per-instance handler binding works without surprises.
   */
  COPY,

  /**
   * Instances reference the type's Method node instead of copying it. The Method (and its argument
   * Properties) are not constructed; the hierarchy reference targets the declaration node itself. A
   * handler bound to a shared Method serves every instance.
   */
  SHARE
}

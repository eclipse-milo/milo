/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.jspecify.annotations.Nullable;

/** Options applied while attaching behavior to a complete, pre-existing Condition instance. */
public final class AttachOptions {

  private @Nullable UaNode conditionSource;

  /**
   * Associate the Condition with {@code conditionSource}.
   *
   * <p>The source fields and references are wired idempotently. Existing unrelated references are
   * left unchanged.
   *
   * @param conditionSource the condition source node.
   * @return these options.
   */
  public AttachOptions conditionSource(UaNode conditionSource) {
    this.conditionSource = conditionSource;
    return this;
  }

  @Nullable UaNode conditionSource() {
    return conditionSource;
  }
}

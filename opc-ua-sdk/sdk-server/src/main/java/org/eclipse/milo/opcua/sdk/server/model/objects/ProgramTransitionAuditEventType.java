/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ProgramTransitionAuditEventType extends AuditUpdateStateEventType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteTransitionVariableType getTransitionNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTransition();

  /** Sets the existing node's local value. */
  void setTransition(@Nullable LocalizedText value);
}

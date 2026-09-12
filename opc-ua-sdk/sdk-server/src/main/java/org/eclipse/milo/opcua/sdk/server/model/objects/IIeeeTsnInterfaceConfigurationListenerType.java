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

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.12">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.12</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeTsnInterfaceConfigurationListenerType
    extends IIeeeTsnInterfaceConfigurationType {
  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getReceiveOffsetNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getReceiveOffset();

  /** Sets the existing node's local value. */
  void setReceiveOffset(@Nullable UInteger value);
}

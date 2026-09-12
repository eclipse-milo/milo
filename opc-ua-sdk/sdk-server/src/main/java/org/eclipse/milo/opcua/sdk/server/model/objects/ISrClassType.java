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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ISrClassType extends BaseInterfaceType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getIdNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getId();

  /** Sets the existing node's local value. */
  void setId(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPriorityNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriority();

  /** Sets the existing node's local value. */
  void setPriority(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getVidNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getVid();

  /** Sets the existing node's local value. */
  void setVid(@Nullable UShort value);
}

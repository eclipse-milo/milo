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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseTsnTrafficSpecificationType extends BaseInterfaceType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxIntervalFramesNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxIntervalFrames();

  /** Sets the existing node's local value. */
  void setMaxIntervalFrames(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxFrameSizeNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxFrameSize();

  /** Sets the existing node's local value. */
  void setMaxFrameSize(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable UnsignedRationalNumber getInterval();

  /** Sets the existing node's local value. */
  void setInterval(@Nullable UnsignedRationalNumber value);
}

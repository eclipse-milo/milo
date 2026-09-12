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

import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseEthernetPortType extends BaseInterfaceType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AnalogUnitType getSpeedNode();

  /** Gets the existing node's local value. */
  @Nullable ULong getSpeed();

  /** Sets the existing node's local value. */
  void setSpeed(@Nullable ULong value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDuplexNode();

  /** Gets the existing node's local value. */
  @Nullable Duplex getDuplex();

  /** Sets the existing node's local value. */
  void setDuplex(@Nullable Duplex value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxFrameLengthNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxFrameLength();

  /** Sets the existing node's local value. */
  void setMaxFrameLength(@Nullable UShort value);
}

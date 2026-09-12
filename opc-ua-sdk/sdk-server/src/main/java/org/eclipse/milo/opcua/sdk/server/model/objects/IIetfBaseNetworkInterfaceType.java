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
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIetfBaseNetworkInterfaceType extends BaseInterfaceType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAdminStatusNode();

  /** Gets the existing node's local value. */
  @Nullable InterfaceAdminStatus getAdminStatus();

  /** Sets the existing node's local value. */
  void setAdminStatus(@Nullable InterfaceAdminStatus value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getOperStatusNode();

  /** Gets the existing node's local value. */
  @Nullable InterfaceOperStatus getOperStatus();

  /** Sets the existing node's local value. */
  void setOperStatus(@Nullable InterfaceOperStatus value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPhysAddressNode();

  /** Gets the existing node's local value. */
  @Nullable String getPhysAddress();

  /** Sets the existing node's local value. */
  void setPhysAddress(@Nullable String value);

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
}

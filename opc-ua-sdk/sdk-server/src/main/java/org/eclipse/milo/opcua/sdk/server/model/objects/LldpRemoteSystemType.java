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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LldpRemoteSystemType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getTimeMarkNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTimeMark();

  /** Sets the existing node's local value. */
  void setTimeMark(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteIndexNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteIndex();

  /** Sets the existing node's local value. */
  void setRemoteIndex(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getChassisIdSubtypeNode();

  /** Gets the existing node's local value. */
  @Nullable ChassisIdSubtype getChassisIdSubtype();

  /** Sets the existing node's local value. */
  void setChassisIdSubtype(@Nullable ChassisIdSubtype value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getChassisIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getChassisId();

  /** Sets the existing node's local value. */
  void setChassisId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPortIdSubtypeNode();

  /** Gets the existing node's local value. */
  @Nullable PortIdSubtype getPortIdSubtype();

  /** Sets the existing node's local value. */
  void setPortIdSubtype(@Nullable PortIdSubtype value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getPortIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getPortId();

  /** Sets the existing node's local value. */
  void setPortId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPortDescriptionNode();

  /** Gets the existing node's local value. */
  @Nullable String getPortDescription();

  /** Sets the existing node's local value. */
  void setPortDescription(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemNameNode();

  /** Gets the existing node's local value. */
  @Nullable String getSystemName();

  /** Sets the existing node's local value. */
  void setSystemName(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemDescriptionNode();

  /** Gets the existing node's local value. */
  @Nullable String getSystemDescription();

  /** Sets the existing node's local value. */
  void setSystemDescription(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemCapabilitiesSupportedNode();

  /** Gets the existing node's local value. */
  @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesSupported();

  /** Sets the existing node's local value. */
  void setSystemCapabilitiesSupported(@Nullable LldpSystemCapabilitiesMap value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSystemCapabilitiesEnabledNode();

  /** Gets the existing node's local value. */
  @Nullable LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled();

  /** Sets the existing node's local value. */
  void setSystemCapabilitiesEnabled(@Nullable LldpSystemCapabilitiesMap value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getRemoteChangesNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRemoteChanges();

  /** Sets the existing node's local value. */
  void setRemoteChanges(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getRemoteTooManyNeighborsNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getRemoteTooManyNeighbors();

  /** Sets the existing node's local value. */
  void setRemoteTooManyNeighbors(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getManagementAddressNode();

  /** Gets the existing node's local value. */
  @Nullable LldpManagementAddressType @Nullable [] getManagementAddress();

  /** Sets the existing node's local value. */
  void setManagementAddress(@Nullable LldpManagementAddressType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getRemoteUnknownTlvNode();

  /** Gets the existing node's local value. */
  @Nullable LldpTlvType @Nullable [] getRemoteUnknownTlv();

  /** Sets the existing node's local value. */
  void setRemoteUnknownTlv(@Nullable LldpTlvType @Nullable [] value);
}

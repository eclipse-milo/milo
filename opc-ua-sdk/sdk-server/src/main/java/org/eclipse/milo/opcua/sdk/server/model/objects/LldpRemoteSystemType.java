/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.6</a>
 */
public interface LldpRemoteSystemType extends BaseObjectType {
  BaseDataVariableType getTimeMarkNode();

  UInteger getTimeMark();

  void setTimeMark(UInteger value);

  BaseDataVariableType getRemoteIndexNode();

  UInteger getRemoteIndex();

  void setRemoteIndex(UInteger value);

  BaseDataVariableType getChassisIdSubtypeNode();

  ChassisIdSubtype getChassisIdSubtype();

  void setChassisIdSubtype(ChassisIdSubtype value);

  BaseDataVariableType getChassisIdNode();

  String getChassisId();

  void setChassisId(String value);

  BaseDataVariableType getPortIdSubtypeNode();

  PortIdSubtype getPortIdSubtype();

  void setPortIdSubtype(PortIdSubtype value);

  BaseDataVariableType getPortIdNode();

  String getPortId();

  void setPortId(String value);

  BaseDataVariableType getPortDescriptionNode();

  String getPortDescription();

  void setPortDescription(String value);

  BaseDataVariableType getSystemNameNode();

  String getSystemName();

  void setSystemName(String value);

  BaseDataVariableType getSystemDescriptionNode();

  String getSystemDescription();

  void setSystemDescription(String value);

  BaseDataVariableType getSystemCapabilitiesSupportedNode();

  LldpSystemCapabilitiesMap getSystemCapabilitiesSupported();

  void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value);

  BaseDataVariableType getSystemCapabilitiesEnabledNode();

  LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled();

  void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value);

  BaseDataVariableType getRemoteChangesNode();

  Boolean getRemoteChanges();

  void setRemoteChanges(Boolean value);

  BaseDataVariableType getRemoteTooManyNeighborsNode();

  Boolean getRemoteTooManyNeighbors();

  void setRemoteTooManyNeighbors(Boolean value);

  BaseDataVariableType getManagementAddressNode();

  LldpManagementAddressType[] getManagementAddress();

  void setManagementAddress(LldpManagementAddressType[] value);

  BaseDataVariableType getRemoteUnknownTlvNode();

  LldpTlvType[] getRemoteUnknownTlv();

  void setRemoteUnknownTlv(LldpTlvType[] value);
}

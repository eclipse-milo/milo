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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpSystemCapabilitiesMap;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
 */
public interface LldpLocalSystemType extends BaseObjectType {
  QualifiedProperty<ChassisIdSubtype> CHASSIS_ID_SUBTYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ChassisIdSubtype",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18947"),
          -1,
          ChassisIdSubtype.class);

  QualifiedProperty<String> CHASSIS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ChassisId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> SYSTEM_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> SYSTEM_DESCRIPTION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemDescription",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<LldpSystemCapabilitiesMap> SYSTEM_CAPABILITIES_SUPPORTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemCapabilitiesSupported",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18956"),
          -1,
          LldpSystemCapabilitiesMap.class);

  QualifiedProperty<LldpSystemCapabilitiesMap> SYSTEM_CAPABILITIES_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemCapabilitiesEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18956"),
          -1,
          LldpSystemCapabilitiesMap.class);

  ChassisIdSubtype getChassisIdSubtype();

  void setChassisIdSubtype(ChassisIdSubtype value);

  PropertyType getChassisIdSubtypeNode();

  String getChassisId();

  void setChassisId(String value);

  PropertyType getChassisIdNode();

  String getSystemName();

  void setSystemName(String value);

  PropertyType getSystemNameNode();

  String getSystemDescription();

  void setSystemDescription(String value);

  PropertyType getSystemDescriptionNode();

  LldpSystemCapabilitiesMap getSystemCapabilitiesSupported();

  void setSystemCapabilitiesSupported(LldpSystemCapabilitiesMap value);

  PropertyType getSystemCapabilitiesSupportedNode();

  LldpSystemCapabilitiesMap getSystemCapabilitiesEnabled();

  void setSystemCapabilitiesEnabled(LldpSystemCapabilitiesMap value);

  PropertyType getSystemCapabilitiesEnabledNode();
}

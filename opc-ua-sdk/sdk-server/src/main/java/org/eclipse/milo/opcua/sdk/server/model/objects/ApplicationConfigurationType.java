/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.6">https://reference.opcfoundation.org/v105/Core/docs/Part21/9.3.6</a>
 */
public interface ApplicationConfigurationType extends ServerConfigurationType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<String> PRODUCT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProductUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> APPLICATION_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=307"),
          -1,
          ApplicationType.class);

  QualifiedProperty<Boolean> ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Enabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  String getApplicationUri();

  void setApplicationUri(String value);

  PropertyType getApplicationUriNode();

  String getProductUri();

  void setProductUri(String value);

  PropertyType getProductUriNode();

  ApplicationType getApplicationType();

  void setApplicationType(ApplicationType value);

  PropertyType getApplicationTypeNode();

  Boolean getEnabled();

  void setEnabled(Boolean value);

  PropertyType getEnabledNode();
}

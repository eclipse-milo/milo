/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19</a>
 */
public interface AudioVariableType extends BaseDataVariableType {
  QualifiedProperty<String> LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AGENCY_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AgencyId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> VERSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "VersionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  String getListId();

  void setListId(String value);

  PropertyType getListIdNode();

  String getAgencyId();

  void setAgencyId(String value);

  PropertyType getAgencyIdNode();

  String getVersionId();

  void setVersionId(String value);

  PropertyType getVersionIdNode();
}

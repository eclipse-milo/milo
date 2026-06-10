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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.8">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.8</a>
 */
public interface ConfigurationUpdatedAuditEventType extends AuditEventType {
  QualifiedProperty<UInteger> OLD_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> NEW_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  UInteger getOldVersion();

  void setOldVersion(UInteger value);

  PropertyType getOldVersionNode();

  UInteger getNewVersion();

  void setNewVersion(UInteger value);

  PropertyType getNewVersionNode();
}

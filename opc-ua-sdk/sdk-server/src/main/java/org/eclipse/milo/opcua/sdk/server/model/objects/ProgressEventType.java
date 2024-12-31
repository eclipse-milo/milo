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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.35">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.35</a>
 */
public interface ProgressEventType extends BaseEventType {
  QualifiedProperty<Object> CONTEXT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Context",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<UShort> PROGRESS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Progress",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  Object getContext();

  void setContext(Object value);

  PropertyType getContextNode();

  UShort getProgress();

  void setProgress(UShort value);

  PropertyType getProgressNode();
}

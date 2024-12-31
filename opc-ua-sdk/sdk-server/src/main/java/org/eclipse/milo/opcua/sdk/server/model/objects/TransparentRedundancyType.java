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
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.8</a>
 */
public interface TransparentRedundancyType extends ServerRedundancyType {
  QualifiedProperty<String> CURRENT_SERVER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentServerId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<RedundantServerDataType[]> REDUNDANT_SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundantServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853"),
          1,
          RedundantServerDataType[].class);

  String getCurrentServerId();

  void setCurrentServerId(String value);

  PropertyType getCurrentServerIdNode();

  RedundantServerDataType[] getRedundantServerArray();

  void setRedundantServerArray(RedundantServerDataType[] value);

  PropertyType getRedundantServerArrayNode();
}

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
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.1</a>
 */
public interface JsonWriterGroupMessageType extends WriterGroupMessageType {
  QualifiedProperty<JsonNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15654"),
          -1,
          JsonNetworkMessageContentMask.class);

  JsonNetworkMessageContentMask getNetworkMessageContentMask();

  void setNetworkMessageContentMask(JsonNetworkMessageContentMask value);

  PropertyType getNetworkMessageContentMaskNode();
}

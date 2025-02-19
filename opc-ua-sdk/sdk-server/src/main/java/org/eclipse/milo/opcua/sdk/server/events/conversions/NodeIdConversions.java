/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class NodeIdConversions {

  private NodeIdConversions() {}

  @NonNull
  static ExpandedNodeId nodeIdToExpandedNodeId(@NonNull NodeId nodeId) {
    return nodeId.expanded();
  }

  @NonNull
  static String nodeIdToString(@NonNull NodeId nodeId) {
    return nodeId.toParseableString();
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof NodeId) {
      NodeId nodeId = (NodeId) o;

      return implicit
          ? implicitConversion(nodeId, targetType)
          : explicitConversion(nodeId, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull NodeId nodeId, OpcUaDataType targetType) {
    return implicitConversion(nodeId, targetType);
  }

  @Nullable
  static Object implicitConversion(@NonNull NodeId nodeId, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case ExpandedNodeId:
        return nodeIdToExpandedNodeId(nodeId);
      case String:
        return nodeIdToString(nodeId);
      default:
        return null;
    }
    // @formatter:on
  }
}

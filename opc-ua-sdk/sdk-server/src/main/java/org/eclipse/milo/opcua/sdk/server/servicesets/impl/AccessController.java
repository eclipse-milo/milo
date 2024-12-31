/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;

public interface AccessController {

  /**
   * Check if the current Session has read access to the Nodes and Attributes identified by {@code
   * readValueIds}.
   *
   * @param session the Session to check access for.
   * @param readValueIds the Nodes and Attributes to check access for.
   * @return a Map containing the {@link AccessResult} for each {@link ReadValueId}.
   */
  Map<ReadValueId, AccessResult> checkReadAccess(Session session, List<ReadValueId> readValueIds);

  /**
   * Check if the current Session has write access to the Nodes and Attributes identified by {@code
   * writeValues}.
   *
   * @param session the Session to check access for.
   * @param writeValues the Nodes and Attributes to check access for.
   * @return a Map containing the {@link AccessResult} for each {@link WriteValue}.
   */
  Map<WriteValue, AccessResult> checkWriteAccess(Session session, List<WriteValue> writeValues);

  /**
   * Check if the current Session has permission to browse the Nodes identified by {@code nodeIds}.
   *
   * @param session the Session to check access for.
   * @param nodeIds the {@link NodeId}s of Nodes to check access for.
   * @return a Map containing the {@link AccessResult} for each {@link NodeId}.
   */
  Map<NodeId, AccessResult> checkBrowseAccess(Session session, List<NodeId> nodeIds);

  /**
   * Check if the current Session has permission to call Methods on the Objects or ObjectTypes
   * identified in each {@link CallMethodRequest}.
   *
   * @param session the Session to check access for.
   * @param requests the {@link CallMethodRequest}s to check access for.
   * @return a Map containing the {@link AccessResult} for each {@link CallMethodRequest}.
   */
  Map<CallMethodRequest, AccessResult> checkCallAccess(
      Session session, List<CallMethodRequest> requests);

  /**
   * Check if the current Session has permission to add References to the Nodes identified by {@code
   * referenceToAdd}.
   *
   * @param session the Session to check access for.
   * @param referencesToAdd the {@link AddReferencesItem}s identifying Nodes to check access for.
   * @return a Map containing the {@link AccessResult} for each {@link AddReferencesItem}.
   */
  Map<AddReferencesItem, AccessResult> checkAddReferencesAccess(
      Session session, List<AddReferencesItem> referencesToAdd);

  /**
   * Check if the current Session has permission to delete Nodes identified by {@code
   * nodesToDelete}.
   *
   * @param session the Session to check access for.
   * @param nodesToDelete the {@link DeleteNodesItem}s identifying Nodes to check access for.
   * @return a Map containing the {@link AccessResult} for each {@link DeleteNodesItem}.
   */
  Map<DeleteNodesItem, AccessResult> checkDeleteNodesAccess(
      Session session, List<DeleteNodesItem> nodesToDelete);

  /**
   * Check if the current Session has permission to delete References from the Nodes identified by
   * {@code referencesToDelete}.
   *
   * @param session the Session to check access for.
   * @param referencesToDelete the {@link DeleteReferencesItem}s identifying the Nodes to check
   *     access for.
   * @return a Map containing the {@link AccessResult} for each {@link DeleteReferencesItem}.
   */
  Map<DeleteReferencesItem, AccessResult> checkDeleteReferencesAccess(
      Session session, List<DeleteReferencesItem> referencesToDelete);

  /** The result of an access control check. */
  sealed interface AccessResult {

    AccessResult ALLOWED = new Allowed();
    AccessResult DENIED_USER_ACCESS = new Denied(StatusCodes.Bad_UserAccessDenied);
    AccessResult DENIED_SECURITY_MODE = new Denied(StatusCodes.Bad_SecurityModeInsufficient);

    /** Access is allowed. */
    record Allowed() implements AccessResult {}

    /**
     * Access is denied for the reason described by {@code statusCode}.
     *
     * @param statusCode the {@link StatusCode} describing the reason access is denied.
     */
    record Denied(StatusCode statusCode) implements AccessResult {
      public Denied(long code) {
        this(new StatusCode(code));
      }
    }

    /**
     * @return {@code true} if access is allowed.
     */
    default boolean isAllowed() {
      return this instanceof Allowed;
    }

    /**
     * @return {@code true} if access is denied.
     */
    default boolean isDenied() {
      return this instanceof Denied;
    }
  }
}

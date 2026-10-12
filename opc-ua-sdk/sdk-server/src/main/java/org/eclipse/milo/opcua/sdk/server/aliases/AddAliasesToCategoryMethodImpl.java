/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * Network-facing {@code AddAliasesToCategory} implementation: authorizes the calling session
 * through the {@link AliasAuthorizationPolicy}, then delegates to the {@link AliasManager}'s
 * per-entry mutation path, targeting the category Object the Method was called on.
 *
 * <p>The category is re-resolved from the call's Object NodeId on every invocation, so a call
 * racing a category removal fails with {@code Bad_NodeIdUnknown} instead of observing stale state.
 *
 * <p>Call-level failures (invalid array shapes, an invalid {@code TargetReferenceType}, operation
 * count over the configured limit, denied authorization) fail the whole call; everything else is
 * reported per entry through the {@code ErrorCodes} output, with one StatusCode per input entry.
 */
class AddAliasesToCategoryMethodImpl extends AliasNameCategoryType.AddAliasesToCategoryMethod {

  private final AliasManager aliasManager;
  private final AliasAuthorizationPolicy policy;

  AddAliasesToCategoryMethodImpl(
      UaMethodNode node, AliasManager aliasManager, AliasAuthorizationPolicy policy) {

    super(node);

    this.aliasManager = aliasManager;
    this.policy = policy;
  }

  @Override
  protected void invoke(
      InvocationContext context,
      String[] aliasNames,
      ExpandedNodeId[] targetNodes,
      String[] targetServers,
      NodeId targetReferenceType,
      Out<StatusCode[]> errorCodes)
      throws UaException {

    Session session = context.getSession().orElse(null);
    NodeId categoryId = context.getObjectId();

    if (!policy.checkMutate(session, categoryId)) {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }

    errorCodes.set(
        aliasManager.addAliasEntries(
            categoryId, aliasNames, targetNodes, targetServers, targetReferenceType));
  }
}

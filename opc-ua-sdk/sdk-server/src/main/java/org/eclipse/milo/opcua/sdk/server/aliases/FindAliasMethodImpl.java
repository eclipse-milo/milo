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

import java.util.List;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.jspecify.annotations.Nullable;

/**
 * Network-facing {@code FindAlias} implementation: authorizes the calling session through the
 * {@link AliasAuthorizationPolicy}, then delegates to the {@link AliasSearchEngine}, searching from
 * the category Object the Method was called on.
 *
 * <p>The category is re-resolved from the call's Object NodeId on every invocation, so a call
 * racing a category removal fails with {@code Bad_NodeIdUnknown} instead of observing stale state.
 */
class FindAliasMethodImpl extends AliasNameCategoryType.FindAliasMethod {

  private final AliasSearchEngine engine;
  private final AliasAuthorizationPolicy policy;

  FindAliasMethodImpl(
      UaMethodNode node, AliasSearchEngine engine, AliasAuthorizationPolicy policy) {

    super(node);

    this.engine = engine;
    this.policy = policy;
  }

  @Override
  protected void invoke(
      InvocationContext context,
      @Nullable String aliasNameSearchPattern,
      @Nullable NodeId referenceTypeFilter,
      Out<AliasNameDataType[]> aliasNodeList)
      throws UaException {

    Session session = context.getSession().orElse(null);
    NodeId categoryId = context.getObjectId();

    String pattern =
        FindMethodSupport.checkFindCall(policy, session, categoryId, aliasNameSearchPattern);

    List<AliasNameDataType> results =
        engine.findAlias(
            categoryId,
            pattern,
            referenceTypeFilter,
            aliasNodeId -> policy.includeResult(session, aliasNodeId));

    aliasNodeList.set(results.toArray(new AliasNameDataType[0]));
  }
}

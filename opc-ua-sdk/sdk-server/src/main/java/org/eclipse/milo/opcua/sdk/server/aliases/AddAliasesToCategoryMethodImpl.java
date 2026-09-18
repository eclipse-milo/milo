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

import org.eclipse.milo.opcua.sdk.core.model.methods.AliasNameCategoryTypeAddAliasesToCategory;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

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
class AddAliasesToCategoryMethodImpl extends AbstractMethodInvocationHandler {

  private final Argument[] inputArguments;
  private final Argument[] outputArguments;
  private final AliasManager aliasManager;
  private final AliasAuthorizationPolicy policy;

  AddAliasesToCategoryMethodImpl(
      UaMethodNode node, AliasManager aliasManager, AliasAuthorizationPolicy policy) {

    super(node);

    NamespaceTable namespaceTable = node.getNodeContext().getServer().getNamespaceTable();
    inputArguments = AliasNameCategoryTypeAddAliasesToCategory.inputArguments(namespaceTable);
    outputArguments = AliasNameCategoryTypeAddAliasesToCategory.outputArguments(namespaceTable);

    this.aliasManager = aliasManager;
    this.policy = policy;
  }

  @Override
  public Argument[] getInputArguments() {
    return inputArguments;
  }

  @Override
  public Argument[] getOutputArguments() {
    return outputArguments;
  }

  @Override
  protected Variant[] invoke(InvocationContext context, Variant[] values) throws UaException {
    AliasNameCategoryTypeAddAliasesToCategory.Inputs input =
        AliasNameCategoryTypeAddAliasesToCategory.Inputs.fromVariants(
            context.getServer().getStaticEncodingContext(), values);
    String[] aliasNames = input.aliasNames();
    ExpandedNodeId[] targetNodes = input.targetNodes();
    String[] targetServers = input.targetServers();
    NodeId targetReferenceType = input.targetReferenceType();

    Session session = context.getSession().orElse(null);
    NodeId categoryId = context.getObjectId();

    if (!policy.checkMutate(session, categoryId)) {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }

    return new AliasNameCategoryTypeAddAliasesToCategory.Outputs(
            aliasManager.addAliasEntries(
                categoryId, aliasNames, targetNodes, targetServers, targetReferenceType))
        .toVariants(context.getServer().getStaticEncodingContext());
  }
}

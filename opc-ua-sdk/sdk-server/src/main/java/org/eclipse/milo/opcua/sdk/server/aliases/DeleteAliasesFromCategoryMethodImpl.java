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
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * Network-facing {@code DeleteAliasesFromCategory} implementation: authorizes the calling session
 * through the {@link AliasAuthorizationPolicy}, then delegates to the {@link AliasManager}'s
 * per-entry mutation path, targeting the category Object the Method was called on.
 *
 * <p>The category is re-resolved from the call's Object NodeId on every invocation, so a call
 * racing a category removal fails with {@code Bad_NodeIdUnknown} instead of observing stale state.
 *
 * <p>Call-level failures (a null {@code AliasNames} array, a {@code TargetNodes} array of a
 * different length, operation count over the configured limit, denied authorization) fail the whole
 * call; everything else is reported per entry through the {@code ErrorCodes} output, with one
 * StatusCode per input entry.
 */
class DeleteAliasesFromCategoryMethodImpl extends AbstractMethodInvocationHandler {

  private final AliasManager aliasManager;
  private final AliasAuthorizationPolicy policy;

  DeleteAliasesFromCategoryMethodImpl(
      UaMethodNode node, AliasManager aliasManager, AliasAuthorizationPolicy policy) {

    super(node);

    this.aliasManager = aliasManager;
    this.policy = policy;
  }

  @Override
  public Argument[] getInputArguments() {
    return new Argument[] {
      new Argument(
          "AliasNames",
          NodeIds.String,
          1,
          new UInteger[] {UInteger.valueOf(0)},
          new LocalizedText("", "")),
      new Argument(
          "TargetNodes",
          NodeIds.ExpandedNodeId,
          1,
          new UInteger[] {UInteger.valueOf(0)},
          new LocalizedText("", ""))
    };
  }

  @Override
  public Argument[] getOutputArguments() {
    return new Argument[] {
      new Argument(
          "ErrorCodes",
          NodeIds.StatusCode,
          1,
          new UInteger[] {UInteger.valueOf(0)},
          new LocalizedText("", ""))
    };
  }

  @Override
  protected Variant[] invoke(InvocationContext context, Variant[] inputValues) throws UaException {
    String[] aliasNames = (String[]) inputValues[0].value();
    ExpandedNodeId[] targetNodes = (ExpandedNodeId[]) inputValues[1].value();

    Session session = context.getSession().orElse(null);
    NodeId categoryId = context.getObjectId();

    if (!policy.checkMutate(session, categoryId)) {
      throw new UaException(StatusCodes.Bad_UserAccessDenied);
    }

    StatusCode[] errorCodes = aliasManager.deleteAliasEntries(categoryId, aliasNames, targetNodes);
    return new Variant[] {new Variant(errorCodes)};
  }
}

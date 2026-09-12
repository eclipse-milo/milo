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
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;

/**
 * Network-facing {@code FindAliasVerbose} implementation: authorizes the calling session through
 * the {@link AliasAuthorizationPolicy}, then delegates to the {@link AliasSearchEngine}, searching
 * from the category Object the Method was called on.
 *
 * <p>The category is re-resolved from the call's Object NodeId on every invocation, so a call
 * racing a category removal fails with {@code Bad_NodeIdUnknown} instead of observing stale state.
 */
class FindAliasVerboseMethodImpl extends AbstractMethodInvocationHandler {

  private final AliasSearchEngine engine;
  private final AliasAuthorizationPolicy policy;

  FindAliasVerboseMethodImpl(
      UaMethodNode node, AliasSearchEngine engine, AliasAuthorizationPolicy policy) {

    super(node);

    this.engine = engine;
    this.policy = policy;
  }

  @Override
  public Argument[] getInputArguments() {
    return new Argument[] {
      new Argument("AliasNameSearchPattern", NodeIds.String, -1, null, new LocalizedText("", "")),
      new Argument("ReferenceTypeFilter", NodeIds.NodeId, -1, null, new LocalizedText("", ""))
    };
  }

  @Override
  public Argument[] getOutputArguments() {
    return new Argument[] {
      new Argument(
          "AliasNodeList",
          NodeIds.AliasNameVerboseDataType,
          1,
          new UInteger[] {UInteger.valueOf(0)},
          new LocalizedText("", ""))
    };
  }

  @Override
  protected Variant[] invoke(InvocationContext context, Variant[] inputValues) throws UaException {
    String aliasNameSearchPattern = (String) inputValues[0].value();
    NodeId referenceTypeFilter = (NodeId) inputValues[1].value();

    Session session = context.getSession().orElse(null);
    NodeId categoryId = context.getObjectId();

    String pattern =
        FindMethodSupport.checkFindCall(policy, session, categoryId, aliasNameSearchPattern);

    List<AliasNameVerboseDataType> results =
        engine.findAliasVerbose(
            categoryId,
            pattern,
            referenceTypeFilter,
            aliasNodeId -> policy.includeResult(session, aliasNodeId));

    return new Variant[] {new Variant(results.toArray(new AliasNameVerboseDataType[0]))};
  }
}

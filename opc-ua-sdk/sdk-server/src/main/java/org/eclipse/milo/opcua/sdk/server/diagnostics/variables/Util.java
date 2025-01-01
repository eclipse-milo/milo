/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

class Util {

  private Util() {}

  static String buildBrowseNamePath(UaNode node) {
    return buildBrowseNamePath(node, new ArrayList<>());
  }

  private static String buildBrowseNamePath(UaNode node, List<String> browseNames) {
    if (node == null || node.getNodeId().equals(NodeIds.ObjectsFolder)) {
      Collections.reverse(browseNames);

      return String.join(".", browseNames);
    }

    ReferenceTypeTree referenceTypeTree = node.getNodeContext().getServer().getReferenceTypeTree();

    browseNames.add(node.getBrowseName().toParseableString());

    Optional<Reference> referenceToParent =
        node.getReferences().stream()
            .filter(
                r ->
                    r.isInverse()
                        && referenceTypeTree.isSubtypeOf(
                            r.getReferenceTypeId(), NodeIds.HierarchicalReferences))
            .findFirst();

    Optional<UaNode> parentNode =
        referenceToParent.flatMap(
            r ->
                node.getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(r.getTargetNodeId()));

    return buildBrowseNamePath(parentNode.orElse(null), browseNames);
  }

  static AttributeFilter diagnosticValueFilter(
      AtomicBoolean diagnosticsEnabled, Function<AttributeFilterContext, DataValue> get) {

    return AttributeFilters.getValue(
        ctx -> {
          if (diagnosticsEnabled.get()) {
            return get.apply(ctx);
          } else {
            return new DataValue(new StatusCode(StatusCodes.Bad_NotReadable));
          }
        });
  }
}

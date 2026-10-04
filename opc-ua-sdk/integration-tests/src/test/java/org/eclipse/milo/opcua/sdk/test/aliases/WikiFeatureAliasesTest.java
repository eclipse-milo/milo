/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test.aliases;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class WikiFeatureAliasesTest extends AbstractClientServerTest {
  // An alias must resolve over the wire, expose a usable target, and disappear when deleted.
  @Test
  void aliasResolvesReadsAndDeletesWithMonotonicVersion() throws Exception {
    var manager =
        new AliasManager(
            server,
            AliasManagerConfig.builder()
                .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                .build());
    manager.startup();
    try {
      var before = AliasTestSupport.requireLastChange(server, NodeIds.Aliases);
      NodeId aliasId = publishCounterAlias(manager, newNodeId("TestInt32"));
      assertEquals(aliasId, publishCounterAlias(manager, newNodeId("TestInt32")));
      AliasTestSupport.assertStrictlyGreater(
          before, AliasTestSupport.requireLastChange(server, NodeIds.Aliases));
      List<AliasNameDataType> found = findAliases(client, "Demo.%");
      assertEquals(1, found.size());
      NodeId target =
          requireNonNull(found.get(0).getReferencedNodes())[0]
              .toNodeId(client.getNamespaceTable())
              .orElseThrow();
      assertEquals(0, client.readValue(0.0, TimestampsToReturn.Neither, target).value().value());
      assertTrue(findAliases(client, "Missing.%").isEmpty());
      manager.deleteAlias(NodeIds.TagVariables, "Demo.Counter", null);
      assertTrue(findAliases(client, "Demo.%").isEmpty());
      UaException wrongTarget =
          assertThrows(
              UaException.class, () -> publishCounterAlias(manager, NodeIds.ObjectsFolder));
      assertEquals(StatusCodes.Bad_InvalidArgument, wrongTarget.getStatusCode().value());
    } finally {
      manager.shutdown();
    }
    assertThrows(
        IllegalStateException.class, () -> publishCounterAlias(manager, newNodeId("TestInt32")));
  }

  // snippet:alias_publish:start
  static NodeId publishCounterAlias(AliasManager aliases, NodeId targetId) throws UaException {
    return aliases.addAlias(
        NodeIds.TagVariables,
        "Demo.Counter",
        List.of(new AliasTarget(targetId.expanded(), null, NodeIds.AliasFor)));
  }

  // snippet:alias_publish:end

  // snippet:alias_find:start
  static List<AliasNameDataType> findAliases(OpcUaClient client, String pattern)
      throws UaException {
    var request =
        new CallMethodRequest(
            NodeIds.Aliases,
            NodeIds.Aliases_FindAlias,
            new Variant[] {new Variant(pattern), new Variant(NodeId.NULL_VALUE)});
    CallMethodResult[] results = client.call(List.of(request)).getResults();
    if (results == null || results.length != 1)
      throw new IllegalStateException("Missing method result");
    CallMethodResult result = results[0];
    if (!result.getStatusCode().isGood()) throw new UaException(result.getStatusCode());
    Variant[] outputs = result.getOutputArguments();
    if (outputs == null || outputs.length != 1)
      throw new IllegalStateException("Missing aliases output");
    List<AliasNameDataType> aliases = new ArrayList<>();
    if (outputs[0].value() == null) return aliases;
    if (!(outputs[0].value() instanceof ExtensionObject[] encoded)) {
      throw new IllegalStateException("Expected encoded AliasNameDataType array");
    }
    for (ExtensionObject entry : encoded) {
      Object decoded = entry.decode(client.getStaticEncodingContext());
      if (!(decoded instanceof AliasNameDataType alias)) {
        throw new IllegalStateException("Expected AliasNameDataType");
      }
      aliases.add(alias);
    }
    return aliases;
  }
  // snippet:alias_find:end
}

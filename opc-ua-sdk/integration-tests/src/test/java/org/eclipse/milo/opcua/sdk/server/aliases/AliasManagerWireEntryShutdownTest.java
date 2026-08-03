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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;

/**
 * Tests that {@link AliasManager}'s wire entry points reject calls once the manager is no longer
 * running.
 *
 * <p>Deliberately located in the manager's package: the guarded state is only reachable over the
 * wire in the narrow race where a dispatched Call loses the manager-lock race to {@code shutdown()}
 * (afterwards the Method Nodes are deleted, so a fresh Call never resolves them), which cannot be
 * produced deterministically from a client. Calling the package-private entry points on a shut-down
 * manager exercises exactly the state that race leaves behind.
 */
class AliasManagerWireEntryShutdownTest extends AbstractClientServerTest {

  // WHY: a Call dispatched just before shutdown() can pass authorization and then wait on the
  // manager lock; if shutdown wins the lock first, the AddressSpace fragment is unregistered by
  // the time the handler proceeds, so mutating would create ghost Nodes and persist spurious
  // LastChange values. The entry point must fail with a defined code instead.
  @Test
  void addAliasEntriesOnShutDownManagerFailsWithBadInvalidState() {
    AliasManager manager = newStartedManager();
    manager.shutdown();

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                manager.addAliasEntries(
                    NodeIds.Aliases,
                    new String[] {"PostShutdownAddAlias"},
                    new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
                    new String[0],
                    NodeId.NULL_VALUE));

    assertEquals(StatusCode.of(StatusCodes.Bad_InvalidState), e.getStatusCode());
  }

  // WHY: the same mid-dispatch-vs-shutdown race exists for DeleteAliasesFromCategory, and the
  // Delete path additionally touches application NodeManagers, so a post-shutdown call must fail
  // with the same defined code instead of mutating ghost state.
  @Test
  void deleteAliasEntriesOnShutDownManagerFailsWithBadInvalidState() {
    AliasManager manager = newStartedManager();
    manager.shutdown();

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                manager.deleteAliasEntries(
                    NodeIds.Aliases, new String[] {"PostShutdownDeleteAlias"}, null));

    assertEquals(StatusCode.of(StatusCodes.Bad_InvalidState), e.getStatusCode());
  }

  private AliasManager newStartedManager() {
    AliasManager manager =
        new AliasManager(
            server,
            AliasManagerConfig.builder()
                .configurationEnabled(true)
                .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                .build());
    manager.startup();
    return manager;
  }
}

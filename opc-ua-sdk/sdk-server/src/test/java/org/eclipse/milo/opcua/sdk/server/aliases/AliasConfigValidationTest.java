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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the validation and default-value contracts of the alias configuration surface: {@link
 * AliasLimits}, {@link AliasTarget}, {@link AliasCategoryConfig}, and {@link
 * AliasManagerConfig.Builder}.
 */
class AliasConfigValidationTest {

  @Nested
  class AliasLimitsValidation {

    // The documented defaults gate Bad_ResponseTooLarge / Bad_InvalidArgument /
    // Bad_TooManyOperations for every application that never customizes limits;
    // changing them silently changes observable service behavior.
    @Test
    void defaultsAreTheDocumentedValues() {
      assertEquals(new AliasLimits(1000, 512, 1000), AliasLimits.defaults());
    }

    // A zero or negative limit would make every FindAlias call fail (or disable the
    // guard entirely, depending on comparison direction); such configs must be
    // rejected at construction, not discovered at call time.
    @ParameterizedTest
    @MethodSource("nonPositiveLimits")
    void nonPositiveLimitIsRejectedAtConstruction(
        int maxResults, int maxPatternLength, int maxOperationsPerCall) {
      assertThrows(
          IllegalArgumentException.class,
          () -> new AliasLimits(maxResults, maxPatternLength, maxOperationsPerCall));
    }

    static Stream<Arguments> nonPositiveLimits() {
      return Stream.of(
          Arguments.of(0, 512, 1000),
          Arguments.of(-1, 512, 1000),
          Arguments.of(1000, 0, 1000),
          Arguments.of(1000, -1, 1000),
          Arguments.of(1000, 512, 0),
          Arguments.of(1000, 512, -1));
    }
  }

  @Nested
  class AliasTargetValidation {

    // Part 17 FindAliasVerbose semantics: a target with no serverUri resolves on the
    // local Server; isLocal() drives both result classification and default ordering.
    @Test
    void targetWithoutServerUriIsLocalAndTargetWithServerUriIsRemote() {
      var local = new AliasTarget(new NodeId(0, 1).expanded(), null, NodeIds.AliasFor);
      var remote =
          new AliasTarget(new NodeId(0, 1).expanded(), "urn:remote:server", NodeIds.AliasFor);

      assertTrue(local.isLocal());
      assertFalse(remote.isLocal());
    }

    // Design invariant: default result ordering is deterministic — local targets
    // before remote ones, then by the NodeId's parseable *string* form (lexicographic,
    // not numeric), so repeated calls yield byte-identical output.
    @Test
    void defaultOrderingPutsLocalBeforeRemoteThenSortsByParseableNodeIdString() {
      var localI10 = new AliasTarget(new NodeId(0, 10).expanded(), null, NodeIds.AliasFor);
      var localI2 = new AliasTarget(new NodeId(0, 2).expanded(), null, NodeIds.AliasFor);
      var remoteI1 =
          new AliasTarget(new NodeId(0, 1).expanded(), "urn:remote:server", NodeIds.AliasFor);

      var targets = new ArrayList<>(List.of(remoteI1, localI2, localI10));
      targets.sort(AliasTarget.DEFAULT_ORDERING);

      // "i=10" sorts before "i=2" lexicographically; the remote target sorts last
      // despite having the smallest numeric identifier.
      assertEquals(List.of(localI10, localI2, remoteI1), targets);
    }
  }

  @Nested
  class AliasManagerConfigBuilderDefaults {

    private final AliasManagerConfig config = AliasManagerConfig.builder().build();

    // Search-allowed is the documented default because FindAlias reveals exactly what
    // Browse on the alias hierarchy already reveals; both session-bearing and
    // internal (null session) calls must be allowed.
    @Test
    void defaultPolicyAllowsFindForSessionAndForInternalCalls() {
      AliasAuthorizationPolicy policy = config.getAuthorizationPolicy();

      assertTrue(policy.checkFind(mock(Session.class), NodeIds.Aliases));
      assertTrue(policy.checkFind(null, NodeIds.Aliases));
    }

    // Deny-by-default mutation is a security invariant: enabling network mutation
    // must require an explicit policy grant, and even internal callers go through
    // the programmatic API rather than the network policy.
    @Test
    void defaultPolicyDeniesMutateForSessionAndForInternalCalls() {
      AliasAuthorizationPolicy policy = config.getAuthorizationPolicy();

      assertFalse(policy.checkMutate(mock(Session.class), NodeIds.Aliases));
      assertFalse(policy.checkMutate(null, NodeIds.Aliases));
    }

    // Design decision: no per-target authorization filtering by default — alias
    // visibility is delegated to the Server's general permission model.
    @Test
    void defaultPolicyIncludesEveryMatchedAliasInResults() {
      AliasAuthorizationPolicy policy = config.getAuthorizationPolicy();

      assertTrue(policy.includeResult(mock(Session.class), new NodeId(1, "alias")));
      assertTrue(policy.includeResult(null, new NodeId(1, "alias")));
    }

    // The Builder and the search engine must share the single DEFAULT_ORDERING
    // definition so default output stays byte-identical across both paths.
    @Test
    void defaultTargetOrderingIsTheSharedDefaultOrderingInstance() {
      assertSame(AliasTarget.DEFAULT_ORDERING, config.getTargetOrdering());
    }

    @Test
    void defaultLimitsAreTheDocumentedDefaults() {
      assertEquals(AliasLimits.defaults(), config.getLimits());
    }

    // The zero-configuration default store is deliberately in-memory: usable for
    // tests and demos, while the Javadoc warns it does not satisfy Part 17 §6.3.1.
    @Test
    void defaultVersionStoreIsInMemory() {
      assertInstanceOf(InMemoryAliasVersionStore.class, config.getVersionStore());
    }

    // Materialized Method Nodes must not claim ns=0 identifiers, which are reserved
    // for the standard NodeSet; the default allocates from namespace index 1.
    @Test
    void defaultNodeNamespaceIndexIsOne() {
      assertEquals(UShort.valueOf(1), config.getNodeNamespaceIndex());
    }

    // FindAliasVerbose and the mutation Methods are Optional per Part 17; they must
    // only appear in the AddressSpace when the application explicitly enables them.
    @Test
    void optionalMethodBehaviorsAreDisabledByDefault() {
      assertFalse(config.isFindAliasVerboseEnabled());
      assertFalse(config.isConfigurationEnabled());
    }
  }
}

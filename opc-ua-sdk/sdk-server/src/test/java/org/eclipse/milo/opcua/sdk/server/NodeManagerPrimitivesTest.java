/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.server.NodeManager.CommitResult;
import org.eclipse.milo.opcua.sdk.server.NodeManager.Generation;
import org.eclipse.milo.opcua.sdk.server.NodeManager.StorageGuarantee;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link NodeManager} storage primitives: conditional add, batch commit, generation
 * handle, and the capability probe.
 *
 * <p>The atomic overrides in {@link AbstractNodeManager} are the guarantee node-instantiation
 * rollback rests on; the default emulations are the documented single-writer boundary for
 * third-party managers that override nothing new.
 */
public class NodeManagerPrimitivesTest {

  private static Node node(NodeId nodeId) {
    Node node = mock(Node.class);
    when(node.getNodeId()).thenReturn(nodeId);
    return node;
  }

  private static Reference reference(NodeId sourceNodeId, NodeId targetNodeId) {
    return new Reference(sourceNodeId, NodeIds.HasComponent, targetNodeId.expanded(), true);
  }

  private static long occurrencesOf(NodeManager<Node> manager, Reference reference) {
    return manager.getReferences(reference.getSourceNodeId()).stream()
        .filter(reference::equals)
        .count();
  }

  @Nested
  class ConditionalAdd {

    private final AbstractNodeManager<Node> manager = new AbstractNodeManager<>();

    @Test
    void addNodeIfAbsentAddsWhenAbsent() {
      Node node = node(new NodeId(1, "a"));

      assertTrue(manager.addNodeIfAbsent(node));
      assertSame(node, manager.get(new NodeId(1, "a")));
    }

    // Fail-on-collision conflict policies are impossible to implement race-free on top of an
    // unconditional replacing add; the conditional add must never replace.
    @Test
    void addNodeIfAbsentRefusesToReplace() {
      Node original = node(new NodeId(1, "a"));
      Node replacement = node(new NodeId(1, "a"));

      manager.addNode(original);

      assertFalse(manager.addNodeIfAbsent(replacement));
      assertSame(original, manager.get(new NodeId(1, "a")), "existing Node must be retained");
    }

    // Pins that the pre-existing API contract is unchanged: addNode replaces.
    @Test
    void plainAddNodeStillReplaces() {
      Node original = node(new NodeId(1, "a"));
      Node replacement = node(new NodeId(1, "a"));

      manager.addNode(original);
      Optional<Node> previous = manager.addNode(replacement);

      assertSame(original, previous.orElse(null));
      assertSame(replacement, manager.get(new NodeId(1, "a")));
    }

    // The default emulation must provide the same observable semantics as the atomic override.
    @Test
    void defaultEmulationRefusesToReplace() {
      ThirdPartyNodeManager thirdParty = new ThirdPartyNodeManager();
      Node original = node(new NodeId(1, "a"));

      assertTrue(thirdParty.addNodeIfAbsent(original));
      assertFalse(thirdParty.addNodeIfAbsent(node(new NodeId(1, "a"))));
      assertSame(original, thirdParty.get(new NodeId(1, "a")));
    }
  }

  @Nested
  class AtomicCommit {

    private final AbstractNodeManager<Node> manager = new AbstractNodeManager<>();

    private final NodeId aId = new NodeId(1, "a");
    private final NodeId bId = new NodeId(1, "b");

    @Test
    void commitAppliesNodesAndReferencesAsOneUnit() throws Exception {
      Node existing = node(new NodeId(1, "existing"));
      manager.addNode(existing);

      Node a = node(aId);
      Node b = node(bId);
      Reference aToB = reference(aId, bId);

      CommitResult result =
          manager.commit(
              NodeManagerBatch.<Node>builder()
                  .addNode(a)
                  .addNode(b)
                  .reuseNode(existing.getNodeId())
                  .addReference(aToB)
                  .build());

      assertSame(a, manager.get(aId));
      assertSame(b, manager.get(bId));
      assertEquals(1, occurrencesOf(manager, aToB));
      assertSame(existing, manager.get(existing.getNodeId()), "reused Node must not be touched");
      assertEquals(List.of(aId, bId), result.addedNodes());
      assertEquals(List.of(aToB), result.addedReferences());
    }

    // References are stored as a multiset, so the commit must skip References that already
    // logically exist — a duplicate occurrence could never be safely attributed to its owner
    // again. The journal must exclude the skipped Reference so rollback won't remove the
    // pre-existing occurrence.
    @Test
    void commitSkipsLogicallyPresentReferencesAndJournalsOnlyNewOnes() throws Exception {
      manager.addNode(node(aId));
      manager.addNode(node(bId));

      Reference alreadyPresent = reference(aId, bId);
      manager.addReference(alreadyPresent);

      Reference newReference = reference(bId, aId);

      CommitResult result =
          manager.commit(
              NodeManagerBatch.<Node>builder()
                  .addReference(alreadyPresent)
                  .addReference(newReference)
                  .build());

      assertEquals(1, occurrencesOf(manager, alreadyPresent), "no duplicate occurrence");
      assertEquals(1, occurrencesOf(manager, newReference), "control: new Reference added");
      assertEquals(List.of(newReference), result.addedReferences());
    }

    @Test
    void builderCollapsesEqualReferencesWithinOneBatch() throws Exception {
      Reference reference = reference(aId, bId);

      CommitResult result =
          manager.commit(
              NodeManagerBatch.<Node>builder()
                  .addReference(reference)
                  .addReference(reference(aId, bId))
                  .build());

      assertEquals(1, occurrencesOf(manager, reference));
      assertEquals(List.of(reference), result.addedReferences());
    }

    // The all-or-nothing guarantee: a collision anywhere in the batch means *nothing* is
    // applied — earlier additions in the same batch must not remain.
    @Test
    void collisionFailureLeavesNoResidue() {
      Node existing = node(bId);
      manager.addNode(existing);

      NodeManagerBatch<Node> batch =
          NodeManagerBatch.<Node>builder()
              .addNode(node(aId))
              .addNode(node(bId))
              .addReference(reference(aId, bId))
              .build();

      NodeManagerBatchException e =
          assertThrows(NodeManagerBatchException.class, () -> manager.commit(batch));

      assertEquals(StatusCodes.Bad_NodeIdExists, e.getStatusCode().getValue());
      assertTrue(e.getApplied().addedNodes().isEmpty(), "atomic commit applied nothing");
      assertTrue(e.getApplied().addedReferences().isEmpty(), "atomic commit applied nothing");
      assertFalse(manager.containsNode(aId), "earlier batch addition must not remain");
      assertTrue(manager.getReferences(aId).isEmpty(), "no Reference residue");
      assertSame(existing, manager.get(bId), "colliding Node must be untouched");
      assertEquals(List.of(bId), manager.getNodeIds());
    }

    // A batch that reuses an existing Node depends on it being there; committing against a
    // manager where it vanished would materialize a dangling subtree.
    @Test
    void missingReusedNodeFailsWithNoResidue() {
      NodeManagerBatch<Node> batch =
          NodeManagerBatch.<Node>builder()
              .addNode(node(aId))
              .reuseNode(new NodeId(1, "missing"))
              .build();

      NodeManagerBatchException e =
          assertThrows(NodeManagerBatchException.class, () -> manager.commit(batch));

      assertEquals(StatusCodes.Bad_NodeIdUnknown, e.getStatusCode().getValue());
      assertFalse(manager.containsNode(aId));
      assertTrue(manager.getNodeIds().isEmpty());
    }

    // Staleness detection: a generation captured before a concurrent direct write must fail the
    // commit before anything is applied — this closes the plan-then-commit race window.
    @Test
    void staleExpectedGenerationFailsWithNoResidue() {
      long expected = manager.getGeneration().value();

      manager.addNode(node(new NodeId(1, "concurrent"))); // a direct write in between

      NodeManagerBatch<Node> batch =
          NodeManagerBatch.<Node>builder().addNode(node(aId)).expectGeneration(expected).build();

      NodeManagerBatchException e =
          assertThrows(NodeManagerBatchException.class, () -> manager.commit(batch));

      assertEquals(StatusCodes.Bad_InvalidState, e.getStatusCode().getValue());
      assertFalse(manager.containsNode(aId));
    }

    // Control for staleness detection: an up-to-date expected generation commits normally.
    @Test
    void matchingExpectedGenerationCommits() throws Exception {
      long expected = manager.getGeneration().value();

      manager.commit(
          NodeManagerBatch.<Node>builder().addNode(node(aId)).expectGeneration(expected).build());

      assertTrue(manager.containsNode(aId));
    }

    @Test
    void probeReportsAtomic() {
      assertEquals(StorageGuarantee.ATOMIC, manager.getStorageGuarantee());
    }
  }

  @Nested
  class EmulatedCommit {

    private final ThirdPartyNodeManager manager = new ThirdPartyNodeManager();

    private final NodeId aId = new NodeId(1, "a");
    private final NodeId bId = new NodeId(1, "b");
    private final NodeId cId = new NodeId(1, "c");

    // The default commit is a sequential composition of the pre-existing methods; in the
    // single-writer case it must produce the same end state and journal as the atomic override.
    @Test
    void emulatedCommitAppliesSequentially() throws Exception {
      Reference alreadyPresent = reference(aId, bId);
      manager.addReference(alreadyPresent);

      Reference newReference = reference(bId, aId);

      CommitResult result =
          manager.commit(
              NodeManagerBatch.<Node>builder()
                  .addNode(node(aId))
                  .addNode(node(bId))
                  .addReference(alreadyPresent)
                  .addReference(newReference)
                  .build());

      assertTrue(manager.containsNode(aId));
      assertTrue(manager.containsNode(bId));
      assertEquals(1, occurrencesOf(manager, alreadyPresent), "logical dedup applies here too");
      assertEquals(1, occurrencesOf(manager, newReference));
      assertEquals(List.of(aId, bId), result.addedNodes());
      assertEquals(List.of(newReference), result.addedReferences());
    }

    // The documented emulation boundary: failure mid-batch leaves the additions applied so far
    // in place, and the exception journals them so a caller can attempt best-effort removal.
    @Test
    void emulatedCommitFailureLeavesPartialResidueAndReportsIt() {
      manager.addNode(node(bId)); // will collide

      NodeManagerBatch<Node> batch =
          NodeManagerBatch.<Node>builder()
              .addNode(node(aId))
              .addNode(node(bId))
              .addNode(node(cId))
              .build();

      NodeManagerBatchException e =
          assertThrows(NodeManagerBatchException.class, () -> manager.commit(batch));

      assertEquals(StatusCodes.Bad_NodeIdExists, e.getStatusCode().getValue());
      assertTrue(manager.containsNode(aId), "sequential emulation leaves earlier additions");
      assertFalse(manager.containsNode(cId), "additions after the failure are not applied");
      assertEquals(List.of(aId), e.getApplied().addedNodes(), "partial journal reported");
    }

    // Callers must be able to tell they are on the emulated path so they can report a
    // best-effort guarantee instead of silently claiming atomicity.
    @Test
    void probeReportsBestEffort() {
      assertEquals(StorageGuarantee.BEST_EFFORT, manager.getStorageGuarantee());
    }

    // Documents the boundary rather than a feature: the default generation handle performs no
    // tracking, so it cannot detect concurrent mutation. Callers see this via the probe.
    @Test
    void emulatedGenerationCannotDetectMutations() {
      Generation generation = manager.getGeneration();

      manager.addNode(node(aId));

      assertEquals(0L, generation.value());
      assertTrue(generation.isCurrent(), "no tracking: emulated handle never reports stale");
    }
  }

  @Nested
  class GenerationAndExclusion {

    private final AbstractNodeManager<Node> manager = new AbstractNodeManager<>();

    private final NodeId aId = new NodeId(1, "a");
    private final NodeId bId = new NodeId(1, "b");

    // Staleness detection is only trustworthy if every mutation path bumps the generation.
    @Test
    void everyMutationKindBumpsTheGeneration() throws Exception {
      Generation g0 = manager.getGeneration();
      manager.addNode(node(aId));
      assertFalse(g0.isCurrent(), "addNode must bump the generation");

      Generation g1 = manager.getGeneration();
      manager.addNodeIfAbsent(node(bId));
      assertFalse(g1.isCurrent(), "addNodeIfAbsent must bump the generation");

      Reference reference = reference(aId, bId);
      Generation g2 = manager.getGeneration();
      manager.addReference(reference);
      assertFalse(g2.isCurrent(), "addReference must bump the generation");

      Generation g3 = manager.getGeneration();
      manager.removeReference(reference);
      assertFalse(g3.isCurrent(), "removeReference must bump the generation");

      Generation g4 = manager.getGeneration();
      manager.removeNode(bId);
      assertFalse(g4.isCurrent(), "removeNode must bump the generation");

      Generation g5 = manager.getGeneration();
      manager.commit(NodeManagerBatch.<Node>builder().addNode(node(bId)).build());
      assertFalse(g5.isCurrent(), "commit must bump the generation");
    }

    // Control: a handle must not go stale spuriously, or staleness-checked commits would fail
    // for callers that only read.
    @Test
    void readsAndNoOpMutationsKeepTheGenerationCurrent() {
      manager.addNode(node(aId));

      Generation generation = manager.getGeneration();

      manager.containsNode(aId);
      manager.getNode(aId);
      manager.getReferences(aId);
      manager.removeNode(new NodeId(1, "missing"));
      manager.removeReference(reference(aId, bId));
      assertFalse(manager.addNodeIfAbsent(node(aId)), "refused add is a no-op");

      assertTrue(generation.isCurrent(), "reads and no-op mutations must not bump");
    }

    // A commit that changes nothing must behave like any other no-op mutation and leave
    // outstanding handles current, or it would spuriously fail other callers' staleness-checked
    // commits.
    @Test
    void noOpCommitKeepsTheGenerationCurrent() throws Exception {
      manager.addNode(node(aId));
      Reference alreadyPresent = reference(aId, bId);
      manager.addReference(alreadyPresent);

      Generation generation = manager.getGeneration();

      // Reuse-only plus a Reference that is already logically present: nothing is applied.
      CommitResult result =
          manager.commit(
              NodeManagerBatch.<Node>builder().reuseNode(aId).addReference(alreadyPresent).build());

      assertTrue(result.addedNodes().isEmpty());
      assertTrue(result.addedReferences().isEmpty());
      assertTrue(generation.isCurrent(), "a no-op commit must not bump the generation");
    }

    // The atomic commit shares the instance monitor with the pre-existing synchronized mutation
    // methods; a commit must not interleave with them.
    @Test
    void commitIsExclusiveWithTheInstanceMonitor() throws Exception {
      CountDownLatch monitorHeld = new CountDownLatch(1);
      CountDownLatch releaseMonitor = new CountDownLatch(1);

      Thread holder =
          new Thread(
              () -> {
                synchronized (manager) {
                  monitorHeld.countDown();
                  try {
                    releaseMonitor.await();
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                  }
                }
              });
      holder.start();
      assertTrue(monitorHeld.await(5, TimeUnit.SECONDS));

      NodeManagerBatch<Node> batch = NodeManagerBatch.<Node>builder().addNode(node(aId)).build();
      CompletableFuture<CommitResult> committed = new CompletableFuture<>();
      Thread committer =
          new Thread(
              () -> {
                try {
                  committed.complete(manager.commit(batch));
                } catch (Exception e) {
                  committed.completeExceptionally(e);
                }
              });
      committer.start();

      // The committer must park on the monitor; wait deterministically for BLOCKED.
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (committer.getState() != Thread.State.BLOCKED && System.nanoTime() < deadline) {
        Thread.onSpinWait();
      }
      assertEquals(Thread.State.BLOCKED, committer.getState(), "commit must wait for the monitor");
      assertFalse(manager.containsNode(aId), "nothing applied while the monitor is held");

      releaseMonitor.countDown();
      committed.get(5, TimeUnit.SECONDS);
      assertTrue(manager.containsNode(aId));

      holder.join(5_000);
      committer.join(5_000);
    }
  }

  @Nested
  class CompileCompatibility {

    // Third-party NodeManager implementations must keep compiling unchanged: every method added
    // by the storage primitives work must be a default method. This pins the full abstract
    // method set so any future abstract addition fails loudly.
    @Test
    void interfaceGainedNoAbstractMethods() {
      Set<String> abstractMethods =
          Arrays.stream(NodeManager.class.getMethods())
              .filter(m -> Modifier.isAbstract(m.getModifiers()))
              .map(
                  m ->
                      m.getName()
                          + Arrays.stream(m.getParameterTypes())
                              .map(Class::getSimpleName)
                              .collect(Collectors.joining(",", "(", ")")))
              .collect(Collectors.toSet());

      Set<String> preExisting =
          Set.of(
              "containsNode(NodeId)",
              "containsNode(ExpandedNodeId,NamespaceTable)",
              "addNode(Node)",
              "getNode(NodeId)",
              "getNode(ExpandedNodeId,NamespaceTable)",
              "removeNode(NodeId)",
              "removeNode(ExpandedNodeId,NamespaceTable)",
              "addReference(Reference)",
              "addReferences(Reference,NamespaceTable)",
              "removeReference(Reference)",
              "removeReferences(Reference,NamespaceTable)",
              "getReferences(NodeId)",
              "getReferences(NodeId,Predicate)");

      assertEquals(preExisting, abstractMethods);
    }
  }

  /**
   * A third-party-style {@link NodeManager} implementing only the method set that existed before
   * the storage primitives were added, overriding none of the new default methods.
   *
   * <p>That this class compiles at all is the source-compatibility proof; it also exercises the
   * sequential default emulations of the primitives.
   */
  private static class ThirdPartyNodeManager implements NodeManager<Node> {

    private final Map<NodeId, Node> nodes = new HashMap<>();
    private final List<Reference> references = new ArrayList<>();

    @Override
    public boolean containsNode(NodeId nodeId) {
      return nodes.containsKey(nodeId);
    }

    @Override
    public boolean containsNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
      return nodeId.toNodeId(namespaceTable).map(this::containsNode).orElse(false);
    }

    @Override
    public Optional<Node> addNode(Node node) {
      return Optional.ofNullable(nodes.put(node.getNodeId(), node));
    }

    @Override
    public Optional<Node> getNode(NodeId nodeId) {
      return Optional.ofNullable(nodes.get(nodeId));
    }

    @Override
    public Optional<Node> getNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
      return nodeId.toNodeId(namespaceTable).flatMap(this::getNode);
    }

    @Override
    public Optional<Node> removeNode(NodeId nodeId) {
      return Optional.ofNullable(nodes.remove(nodeId));
    }

    @Override
    public Optional<Node> removeNode(ExpandedNodeId nodeId, NamespaceTable namespaceTable) {
      return nodeId.toNodeId(namespaceTable).flatMap(this::removeNode);
    }

    @Override
    public void addReference(Reference reference) {
      references.add(reference);
    }

    @Override
    public void addReferences(Reference reference, NamespaceTable namespaceTable) {
      addReference(reference);
      reference.invert(namespaceTable).ifPresent(this::addReference);
    }

    @Override
    public void removeReference(Reference reference) {
      references.remove(reference);
    }

    @Override
    public void removeReferences(Reference reference, NamespaceTable namespaceTable) {
      removeReference(reference);
      reference.invert(namespaceTable).ifPresent(this::removeReference);
    }

    @Override
    public List<Reference> getReferences(NodeId nodeId) {
      return references.stream().filter(r -> r.getSourceNodeId().equals(nodeId)).toList();
    }

    @Override
    public List<Reference> getReferences(NodeId nodeId, Predicate<Reference> filter) {
      return references.stream()
          .filter(r -> r.getSourceNodeId().equals(nodeId))
          .filter(filter)
          .toList();
    }
  }
}

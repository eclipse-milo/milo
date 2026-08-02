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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategoryConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasLimits;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * FindAlias/FindAliasVerbose search semantics (Part 17 §6.3.2/§6.3.3) exercised against a real
 * server, through both the {@link AliasManager} programmatic API and over-the-wire Call requests.
 *
 * <p>All fixture categories are built once per class under the standard {@code Aliases} Object;
 * every search is scoped to its own category subtree so the per-category fixtures stay independent
 * of each other (over-the-wire searches from the {@code Aliases} root use literal patterns that
 * match exactly one fixture alias).
 */
// Fields below are assigned in @BeforeAll, which the nullability inspection does not model.
@SuppressWarnings("NotNullFieldNotInitialized")
public class AliasFindTest extends AbstractClientServerTest {

  private static final int MAX_RESULTS = 10;
  private static final int MAX_PATTERN_LENGTH = 64;

  private AliasManager aliasManager;

  private UaNodeContext nodeContext;
  private UaNodeManager nodeManager;

  /** A ReferenceType registered as a subtype of {@code AliasFor} for filter tests. */
  private NodeId customRefTypeId;

  private NodeId testInt32Id;

  private NodeId patternCatId;
  private NodeId wireCatId;
  private NodeId recursiveParentId;
  private NodeId recursiveChildId;
  private NodeId cycleAId;
  private NodeId dedupParentId;
  private NodeId dedupChildId;
  private NodeId filterCatId;
  private NodeId orderCatId;
  private NodeId nsCatId;
  private NodeId bulkCatId;
  private NodeId remoteCatId;

  /** The remote target of the mixed local/remote alias in {@link #remoteCatId}. */
  private ExpandedNodeId remoteTargetId;

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, uaNodeManager) -> {
          nodeContext = context;
          nodeManager = uaNodeManager;

          // The standard model ships no concrete subtype of AliasFor, so the subtype-filter
          // behavior can only be exercised against a custom ReferenceType.
          customRefTypeId = newNodeId("HasTestAliasTarget");

          var refTypeNode =
              new UaReferenceTypeNode(
                  context,
                  customRefTypeId,
                  newQualifiedName("HasTestAliasTarget"),
                  LocalizedText.english("HasTestAliasTarget"),
                  LocalizedText.NULL_VALUE,
                  UInteger.valueOf(0),
                  UInteger.valueOf(0),
                  false,
                  false,
                  LocalizedText.english("TestAliasTargetOf"));

          uaNodeManager.addNode(refTypeNode);

          refTypeNode.addReference(
              new Reference(
                  customRefTypeId,
                  NodeIds.HasSubtype,
                  NodeIds.AliasFor.expanded(),
                  Reference.Direction.INVERSE));
        });

    // The ReferenceTypeTree is built lazily and cached; rebuild it so the custom subtype is
    // visible to the search engine's filter validation and subtype checks.
    server.updateReferenceTypeTree();
  }

  @BeforeAll
  void startAliasManagerAndBuildFixtures() throws Exception {
    aliasManager =
        new AliasManager(
            server,
            AliasManagerConfig.builder()
                .limits(new AliasLimits(MAX_RESULTS, MAX_PATTERN_LENGTH, 10))
                .findAliasVerboseEnabled(true)
                .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                .build());

    aliasManager.startup();

    testInt32Id = newNodeId("TestInt32");

    // Pattern semantics: four aliases whose names exercise wildcard, class, and case matching.
    patternCatId = addCategory("PatternCat", NodeIds.Aliases);
    for (String name : List.of("Apple", "Apricot", "Banana", "apple")) {
      aliasManager.addAlias(patternCatId, name, aliasForTargets(testInt32Id));
    }

    // Over-the-wire cases search from the Aliases root with the literal pattern "WireAlias".
    wireCatId = addCategory("WireCat", NodeIds.Aliases);
    aliasManager.addAlias(wireCatId, "WireAlias", aliasForTargets(testInt32Id));

    // Recursive scope: an alias organized only by a subcategory of the searched category.
    recursiveParentId = addCategory("RecursiveParent", NodeIds.Aliases);
    recursiveChildId = addCategory("RecursiveChild", recursiveParentId);
    aliasManager.addAlias(recursiveChildId, "DeepAlias", aliasForTargets(testInt32Id));

    // Cycle: CycleA organizes CycleB (via addCategory) and a raw Organizes Reference makes
    // CycleB organize CycleA right back. The manager API cannot create this, so it is wired
    // with a raw Reference after the alias is added.
    cycleAId = addCategory("CycleA", NodeIds.Aliases);
    NodeId cycleBId = addCategory("CycleB", cycleAId);
    aliasManager.addAlias(cycleBId, "CycleAlias", aliasForTargets(testInt32Id));
    UaNode cycleBNode = getNode(cycleBId);
    cycleBNode.addReference(
        new Reference(
            cycleBId, NodeIds.Organizes, cycleAId.expanded(), Reference.Direction.FORWARD));

    // Dedup + verbose category selection: one alias organized by both a parent category and its
    // subcategory, so two traversal paths reach it from the parent.
    dedupParentId = addCategory("DedupParent", NodeIds.Aliases);
    dedupChildId = addCategory("DedupChild", dedupParentId);
    NodeId sharedAliasId =
        aliasManager.addAlias(dedupChildId, "SharedAlias", aliasForTargets(testInt32Id));
    UaNode sharedAliasNode = getNode(sharedAliasId);
    sharedAliasNode.addReference(
        new Reference(
            sharedAliasId,
            NodeIds.Organizes,
            dedupParentId.expanded(),
            Reference.Direction.INVERSE));

    // ReferenceType filtering: one alias with an AliasFor target and a custom-subtype target.
    filterCatId = addCategory("FilterCat", NodeIds.Aliases);
    aliasManager.addAlias(
        filterCatId,
        "FilteredAlias",
        List.of(
            new AliasTarget(testInt32Id.expanded(), null, NodeIds.AliasFor),
            new AliasTarget(NodeIds.Server.expanded(), null, customRefTypeId)));

    // Ordering: two identically named aliases with different NodeIds plus a third name. Raw
    // Nodes because addAlias treats a same-named alias as the same alias and extends it.
    orderCatId = addCategory("OrderCat", NodeIds.Aliases);
    addRawAlias(orderCatId, newNodeId("OrderCat/a1"), newQualifiedName("OrderA"));
    addRawAlias(orderCatId, newNodeId("OrderCat/a2"), new QualifiedName(0, "OrderA"));
    addRawAlias(orderCatId, newNodeId("OrderCat/b1"), newQualifiedName("OrderB"));

    // BrowseName namespace: same name text in two different BrowseName namespaces.
    nsCatId = addCategory("NsCat", NodeIds.Aliases);
    aliasManager.addAlias(nsCatId, "NsAlias", aliasForTargets(testInt32Id));
    addRawAlias(nsCatId, newNodeId("NsCat/NsAlias-ns0"), new QualifiedName(0, "NsAlias"));

    // Verbose ServerUris: an alias with a local target plus a remote AliasFor Reference whose
    // target ExpandedNodeId carries its Server URI directly (no ServerTable entry needed). The
    // manager rejects remote targets (Bad_NotSupported), so the Reference is wired raw.
    remoteCatId = addCategory("RemoteCat", NodeIds.Aliases);
    NodeId remoteAliasId =
        aliasManager.addAlias(remoteCatId, "RemoteMixAlias", aliasForTargets(testInt32Id));
    remoteTargetId =
        new ExpandedNodeId(
            ExpandedNodeId.ServerReference.of("urn:remote:server"),
            ExpandedNodeId.NamespaceReference.of(testInt32Id.getNamespaceIndex()),
            "RemoteCat/RemoteTarget");
    getNode(remoteAliasId)
        .addReference(
            new Reference(
                remoteAliasId, NodeIds.AliasFor, remoteTargetId, Reference.Direction.FORWARD));

    // Result cap: one more matching alias than the configured maxResults.
    bulkCatId = addCategory("BulkCat", NodeIds.Aliases);
    for (int i = 0; i <= MAX_RESULTS; i++) {
      aliasManager.addAlias(bulkCatId, String.format("Bulk%02d", i), aliasForTargets(testInt32Id));
    }
  }

  @AfterAll
  void shutdownAliasManager() {
    aliasManager.shutdown();
  }

  @Nested
  class PatternSemantics {

    // Part 17 §6.3.2 defines the search string as a Part 4 §7.7.3 (Table 120) Like pattern:
    // '%' matches any run, '_' exactly one character, '[...]' a character list/range,
    // '[^...]' its negation, and everything else matches literally.
    @ParameterizedTest
    @MethodSource("patternCases")
    void patternMatchingFollowsPart4LikeGrammar(String pattern, List<String> expectedNames)
        throws UaException {

      assertEquals(expectedNames, findNames(patternCatId, pattern));
    }

    static Stream<Arguments> patternCases() {
      return Stream.of(
          Arguments.of("Apple", List.of("Apple")),
          Arguments.of("Grape", List.of()),
          Arguments.of("Ap%", List.of("Apple", "Apricot")),
          Arguments.of("%", List.of("Apple", "Apricot", "Banana", "apple")),
          Arguments.of("Appl_", List.of("Apple")),
          Arguments.of("_pple", List.of("Apple", "apple")),
          Arguments.of("[AB]%", List.of("Apple", "Apricot", "Banana")),
          Arguments.of("[A-C]%", List.of("Apple", "Apricot", "Banana")),
          Arguments.of("[^A]%", List.of("Banana", "apple")));
    }

    // Part 4 §7.7.3 Like matching is case-sensitive; the alias design binds this with
    // no Unicode folding, so "apple" and "Apple" are distinct names.
    @Test
    void matchingIsCaseSensitive() throws UaException {
      assertEquals(List.of("apple"), findNames(patternCatId, "apple"));
      assertEquals(List.of(), findNames(patternCatId, "APPLE"));
    }
  }

  @Nested
  class InvalidArguments {

    // Part 17 §6.3.2 maps an invalid search string to Bad_InvalidArgument; a malformed
    // pattern must fail the call rather than silently matching nothing.
    @ParameterizedTest
    @ValueSource(strings = {"abc\\", "[]", "[abc", "[z-a]"})
    void malformedPatternFailsWithBadInvalidArgument(String pattern) {
      UaException e =
          assertThrows(
              UaException.class, () -> aliasManager.findAlias(patternCatId, pattern, null));

      assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
    }

    // The configured maxPatternLength caps pattern cost before parsing; an oversized pattern
    // is an invalid argument, not a truncated search.
    @Test
    void patternLongerThanMaxPatternLengthFailsWithBadInvalidArgument() {
      String pattern = "A".repeat(MAX_PATTERN_LENGTH + 1);

      UaException e =
          assertThrows(
              UaException.class, () -> aliasManager.findAlias(patternCatId, pattern, null));

      assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
    }

    // Binding policy: a ReferenceType filter that is not a known ReferenceType fails the call
    // with Bad_InvalidArgument instead of being treated as "matches nothing".
    @Test
    void unknownReferenceTypeFilterFailsWithBadInvalidArgument() {
      NodeId unknown = newNodeId("NoSuchReferenceType");

      UaException e =
          assertThrows(UaException.class, () -> aliasManager.findAlias(filterCatId, "%", unknown));

      assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().value());
    }

    // Part 17 defines no paging for FindAlias; when more entries match than the configured
    // maxResults the call must fail with Bad_ResponseTooLarge so the Client narrows its pattern.
    @Test
    void moreMatchesThanMaxResultsFailsWithBadResponseTooLarge() {
      UaException e =
          assertThrows(UaException.class, () -> aliasManager.findAlias(bulkCatId, "Bulk%", null));

      assertEquals(StatusCodes.Bad_ResponseTooLarge, e.getStatusCode().value());
    }
  }

  @Nested
  class OverTheWire {

    // End-to-end proof that the standard Aliases FindAlias Method (bound by the manager) is
    // callable by a Client and returns the matching alias with its targets.
    @Test
    void findAliasCallReturnsMatchingAliasAndTargets() throws UaException {
      CallMethodResult result =
          call(NodeIds.Aliases, NodeIds.Aliases_FindAlias, "WireAlias", NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());

      ExtensionObject[] xos = outputArray(result);
      assertEquals(1, xos.length);

      var entry = (AliasNameDataType) xos[0].decode(client.getStaticEncodingContext());
      assertEquals("WireAlias", entry.getAliasName().name());
      assertArrayEquals(new ExpandedNodeId[] {testInt32Id.expanded()}, entry.getReferencedNodes());
    }

    // The FindAliasVerbose Method is materialized by the manager (it is not in the standard
    // NodeSet) and hosted in the manager's fragment; this proves it routes and that the verbose
    // entry reports the organizing category and, per Part 17 §7.3, no server URI for a local
    // target.
    @Test
    void findAliasVerboseCallReportsCategoryAndNoServerUriForLocalTargets() throws UaException {
      NodeId verboseMethodId = newNodeId("Aliases/FindAliasVerbose");

      CallMethodResult result =
          call(NodeIds.Aliases, verboseMethodId, "WireAlias", NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());

      ExtensionObject[] xos = outputArray(result);
      assertEquals(1, xos.length);

      var entry = (AliasNameVerboseDataType) xos[0].decode(client.getStaticEncodingContext());
      assertEquals("WireAlias", entry.getAliasName().name());
      assertEquals(wireCatId, entry.getAliasNameCategoryId());

      // Binary encoding may surface a null array as null or empty; both mean "all local".
      String[] serverUris = entry.getServerUris();
      assertTrue(
          serverUris == null || serverUris.length == 0,
          "local targets must have no server URIs, got: " + Arrays.toString(serverUris));
    }

    // Part 17 §6.3.2: an invalid search string fails the Call with Bad_InvalidArgument in the
    // CallMethodResult, observable by a real Client.
    @Test
    void malformedPatternCallFailsWithBadInvalidArgument() throws UaException {
      CallMethodResult result =
          call(NodeIds.Aliases, NodeIds.Aliases_FindAlias, "[unclosed", NodeId.NULL_VALUE);

      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    }

    // The maxPatternLength limit must hold on the network path too, before any parsing.
    @Test
    void oversizedPatternCallFailsWithBadInvalidArgument() throws UaException {
      String pattern = "A".repeat(MAX_PATTERN_LENGTH + 1);

      CallMethodResult result =
          call(NodeIds.Aliases, NodeIds.Aliases_FindAlias, pattern, NodeId.NULL_VALUE);

      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    }
  }

  @Nested
  class RecursiveScope {

    // Part 17 §6.3.2: FindAlias searches the category and all of its subcategories, so an alias
    // organized only by a subcategory is found from the parent.
    @Test
    void aliasInSubcategoryIsFoundFromParentCategory() throws UaException {
      assertEquals(List.of("DeepAlias"), findNames(recursiveParentId, "DeepAlias"));
      assertEquals(List.of("DeepAlias"), findNames(recursiveChildId, "DeepAlias"));
    }

    // Binding policy 7: results are deduplicated by alias NodeId, so an alias reachable both
    // directly and through a subcategory appears exactly once.
    @Test
    void aliasReachableThroughTwoPathsAppearsOnce() throws UaException {
      List<AliasNameDataType> results = aliasManager.findAlias(dedupParentId, "SharedAlias", null);

      assertEquals(1, results.size());
      assertEquals("SharedAlias", results.get(0).getAliasName().name());
    }

    // A cycle in the category graph (possible with raw References) must terminate via
    // visited-category tracking instead of hanging the search.
    @Test
    void categoryCycleDoesNotHangSearch() {
      List<String> names =
          assertTimeoutPreemptively(
              Duration.ofSeconds(10), () -> findNames(cycleAId, "CycleAlias"));

      assertEquals(List.of("CycleAlias"), names);
    }
  }

  @Nested
  class ReferenceTypeFilter {

    // Part 17 §6.3.3: with no ReferenceType filter, targets of AliasFor and all of its
    // subtypes are returned.
    @Test
    void nullFilterReturnsTargetsOfAliasForAndItsSubtypes() throws UaException {
      List<AliasNameDataType> results = aliasManager.findAlias(filterCatId, "FilteredAlias", null);

      assertEquals(1, results.size());
      assertArrayEquals(
          new ExpandedNodeId[] {NodeIds.Server.expanded(), testInt32Id.expanded()},
          results.get(0).getReferencedNodes());
    }

    // Part 17 §6.3.3: "any ReferenceType includes all subtypes", so filtering on AliasFor
    // still returns the target referenced by the custom AliasFor subtype.
    @Test
    void aliasForFilterIncludesSubtypeReferences() throws UaException {
      List<AliasNameDataType> results =
          aliasManager.findAlias(filterCatId, "FilteredAlias", NodeIds.AliasFor);

      assertEquals(1, results.size());
      assertArrayEquals(
          new ExpandedNodeId[] {NodeIds.Server.expanded(), testInt32Id.expanded()},
          results.get(0).getReferencedNodes());
    }

    // Filtering on a subtype must exclude targets referenced only by the supertype.
    @Test
    void subtypeFilterReturnsOnlyTargetsOfThatSubtype() throws UaException {
      List<AliasNameDataType> results =
          aliasManager.findAlias(filterCatId, "FilteredAlias", customRefTypeId);

      assertEquals(1, results.size());
      assertArrayEquals(
          new ExpandedNodeId[] {NodeIds.Server.expanded()}, results.get(0).getReferencedNodes());
    }

    // Part 17 §6.3.2/§6.3.3: a valid ReferenceType that no alias Reference satisfies yields an
    // empty list, not an error — an alias with no passing targets is omitted entirely.
    @Test
    void filterOutsideAliasForHierarchyYieldsEmptyResult() throws UaException {
      List<AliasNameDataType> results =
          aliasManager.findAlias(filterCatId, "FilteredAlias", NodeIds.Organizes);

      assertEquals(List.of(), results);
    }
  }

  @Nested
  class Ordering {

    // Binding policy 10: entries are ordered by alias name text, then by alias NodeId, so
    // identically named aliases and repeat calls return a deterministic sequence.
    @Test
    void entriesAreOrderedByNameTextThenNodeId() throws UaException {
      List<AliasNameDataType> results = aliasManager.findAlias(orderCatId, "Order%", null);

      List<QualifiedName> names = results.stream().map(AliasNameDataType::getAliasName).toList();

      // "OrderCat/a1" < "OrderCat/a2" decides the two OrderA entries; name text puts OrderB last.
      assertEquals(
          List.of(
              newQualifiedName("OrderA"),
              new QualifiedName(0, "OrderA"),
              newQualifiedName("OrderB")),
          names);
    }
  }

  @Nested
  class Verbose {

    // Binding policy 4: the verbose aliasNameCategoryId is the organizing category with the
    // smallest depth from the searched category, so the same alias reports a different category
    // depending on where the search starts.
    @Test
    void verboseCategoryIsSmallestDepthOrganizingCategoryWithinSearchedSubtree()
        throws UaException {

      List<AliasNameVerboseDataType> fromParent =
          aliasManager.findAliasVerbose(dedupParentId, "SharedAlias", null);
      assertEquals(1, fromParent.size());
      assertEquals(dedupParentId, fromParent.get(0).getAliasNameCategoryId());

      List<AliasNameVerboseDataType> fromChild =
          aliasManager.findAliasVerbose(dedupChildId, "SharedAlias", null);
      assertEquals(1, fromChild.size());
      assertEquals(dedupChildId, fromChild.get(0).getAliasNameCategoryId());
    }

    // Part 17 §7.3 permits a null server URI for local Nodes; every target this server returns
    // is local, so the ServerUris field must be absent.
    @Test
    void verboseServerUrisAreNullForLocalTargets() throws UaException {
      List<AliasNameVerboseDataType> results =
          aliasManager.findAliasVerbose(wireCatId, "WireAlias", null);

      assertEquals(1, results.size());
      assertNull(results.get(0).getServerUris());
    }

    // Part 17 §7.3 — once any target is remote, ServerUris must be present and parallel to
    // ReferencedNodes: the remote target's Server URI in its slot and null (permitted for local
    // Nodes) in the local slots. The default target ordering puts local targets before remote
    // ones, fixing which slot is which.
    @Test
    void verboseServerUrisParallelReferencedNodesWhenAnyTargetIsRemote() throws UaException {
      List<AliasNameVerboseDataType> results =
          aliasManager.findAliasVerbose(remoteCatId, "RemoteMixAlias", null);

      assertEquals(1, results.size());
      AliasNameVerboseDataType entry = results.get(0);

      assertArrayEquals(
          new ExpandedNodeId[] {testInt32Id.expanded(), remoteTargetId},
          entry.getReferencedNodes());

      String[] serverUris = entry.getServerUris();
      assertNotNull(serverUris, "a remote target must make the ServerUris array present");
      assertArrayEquals(new String[] {null, "urn:remote:server"}, serverUris);
    }
  }

  @Nested
  class BrowseNameNamespace {

    // Part 17 §6.2: alias name matching considers only the BrowseName's name text; the
    // namespace index is ignored, so identically named aliases in different BrowseName
    // namespaces both match a literal pattern.
    @Test
    void browseNameNamespaceIsIgnoredInMatching() throws UaException {
      List<AliasNameDataType> results = aliasManager.findAlias(nsCatId, "NsAlias", null);

      List<QualifiedName> names = results.stream().map(AliasNameDataType::getAliasName).toList();

      // Same name text sorts by NodeId: "NsCat/NsAlias" < "NsCat/NsAlias-ns0".
      assertEquals(List.of(newQualifiedName("NsAlias"), new QualifiedName(0, "NsAlias")), names);
    }
  }

  private NodeId addCategory(String name, NodeId parentCategoryId) throws UaException {
    return aliasManager
        .addCategory(
            new AliasCategoryConfig(
                newNodeId(name),
                parentCategoryId,
                newQualifiedName(name),
                nodeManager,
                aliasName -> newNodeId(name + "/" + aliasName),
                false,
                false,
                false))
        .nodeId();
  }

  /**
   * Create an alias Node directly, bypassing {@link AliasManager#addAlias}: needed for a second
   * alias with the same name text (addAlias would extend the existing one) and for BrowseNames in a
   * namespace other than the alias NodeId's.
   */
  private void addRawAlias(NodeId categoryId, NodeId aliasNodeId, QualifiedName browseName) {
    var aliasNode =
        new AliasNameTypeNode(
            nodeContext,
            aliasNodeId,
            browseName,
            LocalizedText.english(browseName.name()),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            null);

    nodeManager.addNode(aliasNode);

    aliasNode.addReference(
        new Reference(
            aliasNodeId,
            NodeIds.HasTypeDefinition,
            NodeIds.AliasNameType.expanded(),
            Reference.Direction.FORWARD));

    aliasNode.addReference(
        new Reference(
            aliasNodeId, NodeIds.Organizes, categoryId.expanded(), Reference.Direction.INVERSE));

    aliasNode.addReference(
        new Reference(
            aliasNodeId, NodeIds.AliasFor, testInt32Id.expanded(), Reference.Direction.FORWARD));
  }

  private List<AliasTarget> aliasForTargets(NodeId targetId) {
    return List.of(new AliasTarget(targetId.expanded(), null, NodeIds.AliasFor));
  }

  private List<String> findNames(NodeId categoryId, String pattern) throws UaException {
    var names = new ArrayList<String>();
    for (var entry : aliasManager.findAlias(categoryId, pattern, null)) {
      names.add(requireNonNull(entry.getAliasName().name()));
    }
    return names;
  }

  private UaNode getNode(NodeId nodeId) {
    return server
        .getAddressSpaceManager()
        .getManagedNode(nodeId)
        .orElseThrow(() -> new IllegalStateException("node not found: " + nodeId));
  }

  private CallMethodResult call(
      NodeId objectId, NodeId methodId, String pattern, NodeId referenceTypeFilter)
      throws UaException {

    var request =
        new CallMethodRequest(
            objectId,
            methodId,
            new Variant[] {new Variant(pattern), new Variant(referenceTypeFilter)});

    CallResponse response = client.call(List.of(request));

    return requireNonNull(response.getResults())[0];
  }

  private ExtensionObject[] outputArray(CallMethodResult result) {
    Variant[] outputs = requireNonNull(result.getOutputArguments());
    return (ExtensionObject[]) requireNonNull(outputs[0].value());
  }
}

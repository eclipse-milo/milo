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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.util.LikeMatcher;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.jspecify.annotations.Nullable;

/**
 * Evaluates {@code FindAlias} and {@code FindAliasVerbose} queries against the live AddressSpace.
 *
 * <p>A search starts at a category Node and walks forward {@code Organizes} References, recursing
 * into every {@code AliasNameCategoryType} instance it reaches and collecting every {@code
 * AliasNameType} instance. Each alias Object appears at most once in the result, no matter how many
 * subtree paths reach it. An alias matches when the text of its BrowseName — the name alone,
 * ignoring the namespace index, per Part 17 §6.2 — matches the Part 4 {@code Like} pattern, and at
 * least one of its forward {@code AliasFor}-or-subtype References passes the ReferenceType filter;
 * an alias with no passing target References is omitted entirely, so a filter that nothing
 * satisfies produces an empty result (§6.3.2/§6.3.3: if no Nodes "match the search string or have
 * the appropriate ReferenceType, the list shall be empty").
 *
 * <p>Results are deterministically ordered: entries by alias name text, then by alias NodeId;
 * targets within an entry by the configured target ordering (default: local-before-remote, then by
 * target NodeId). Verbose results report, for each alias, the containing category closest to the
 * search root (ties broken by category BrowseName, then category NodeId).
 *
 * <p>The engine reads whatever the AddressSpace currently contains, so aliases loaded from a
 * NodeSet file or created by other components are found without registration. Searches take no
 * lock: a search overlapping a concurrent mutation may observe a partially applied change — the
 * same weak consistency Browse has over the live AddressSpace.
 *
 * <p>Instances are stateless between calls and safe for concurrent use.
 */
public final class AliasSearchEngine {

  /**
   * Verbose category selection: smallest depth from the search root, then category BrowseName, then
   * category NodeId.
   */
  private static final Comparator<CategoryRef> CATEGORY_ORDER =
      Comparator.comparingInt(CategoryRef::depth)
          .thenComparing(c -> c.browseName().toParseableString())
          .thenComparing(c -> c.nodeId().toParseableString());

  /** Result entries: alias name text first, then alias NodeId for identically named aliases. */
  private static final Comparator<MatchedAlias> ENTRY_ORDER =
      Comparator.comparing((MatchedAlias m) -> m.nameText).thenComparing(m -> m.parseableNodeId);

  private final LikeMatcher likeMatcher = new LikeMatcher();

  private final OpcUaServer server;
  private final AliasTypes aliasTypes;
  private final AliasLimits limits;
  private final Comparator<AliasTarget> targetOrdering;

  /**
   * Create an engine that searches {@code server}'s AddressSpace.
   *
   * @param server the server whose AddressSpace is searched.
   * @param limits the limits enforced on every call.
   * @param targetOrdering the ordering applied to the targets within each result entry.
   */
  public AliasSearchEngine(
      OpcUaServer server, AliasLimits limits, Comparator<AliasTarget> targetOrdering) {

    this.server = server;
    this.aliasTypes = new AliasTypes(server);
    this.limits = limits;
    this.targetOrdering = targetOrdering;
  }

  /**
   * Find aliases under {@code categoryId} whose name matches {@code pattern}.
   *
   * @param categoryId the NodeId of the {@code AliasNameCategoryType} instance to search from.
   * @param pattern a Part 4 {@code Like} pattern matched against alias name text.
   * @param referenceTypeFilter restricts targets to References of this type or a subtype; a null
   *     (or null-valued) NodeId means no restriction beyond {@code AliasFor} and its subtypes.
   * @return the matching entries, each with at least one target, ordered by alias name text then
   *     alias NodeId.
   * @throws UaException with {@code Bad_InvalidArgument} if the pattern is too long or malformed,
   *     or if the filter is not a known ReferenceType; {@code Bad_NodeIdUnknown} if {@code
   *     categoryId} is not an {@code AliasNameCategoryType} instance; {@code Bad_ResponseTooLarge}
   *     if more entries match than the configured maximum.
   */
  public List<AliasNameDataType> findAlias(
      NodeId categoryId, String pattern, @Nullable NodeId referenceTypeFilter) throws UaException {

    return findAlias(categoryId, pattern, referenceTypeFilter, aliasNodeId -> true);
  }

  /**
   * Find aliases under {@code categoryId} whose name matches {@code pattern}, applying a per-result
   * authorization filter.
   *
   * <p>Entries whose alias NodeId the filter rejects are omitted from the result. Filtering happens
   * after matching, so rejected entries still count toward the configured result maximum.
   *
   * @param categoryId the NodeId of the {@code AliasNameCategoryType} instance to search from.
   * @param pattern a Part 4 {@code Like} pattern matched against alias name text.
   * @param referenceTypeFilter restricts targets to References of this type or a subtype; a null
   *     (or null-valued) NodeId means no restriction beyond {@code AliasFor} and its subtypes.
   * @param resultFilter decides, by alias NodeId, whether a matched alias may appear in the result.
   * @return the matching, filter-approved entries, ordered by alias name text then alias NodeId.
   * @throws UaException under the same conditions as {@link #findAlias(NodeId, String, NodeId)}.
   */
  List<AliasNameDataType> findAlias(
      NodeId categoryId,
      String pattern,
      @Nullable NodeId referenceTypeFilter,
      Predicate<NodeId> resultFilter)
      throws UaException {

    List<MatchedAlias> matches = search(categoryId, pattern, referenceTypeFilter);

    List<AliasNameDataType> results = new ArrayList<>(matches.size());
    for (MatchedAlias match : matches) {
      if (!resultFilter.test(match.nodeId)) {
        continue;
      }
      results.add(
          new AliasNameDataType(
              match.browseName,
              match.targets.stream().map(AliasTarget::nodeId).toArray(ExpandedNodeId[]::new)));
    }
    return results;
  }

  /**
   * Find aliases under {@code categoryId} whose name matches {@code pattern}, with containing
   * category and target server details.
   *
   * <p>An entry's {@code ServerUris} field is null when every target is local (permitted by Part 17
   * §7.3, which allows a null URI for any local Node). When an alias carries remote target
   * References — possible for aliases created outside the manager, e.g. loaded from a NodeSet file
   * — {@code ServerUris} is an array parallel to the referenced Nodes: the remote targets' Server
   * URIs (or, for a server index the ServerTable cannot resolve, the raw index as text), null for
   * local targets.
   *
   * @param categoryId the NodeId of the {@code AliasNameCategoryType} instance to search from.
   * @param pattern a Part 4 {@code Like} pattern matched against alias name text.
   * @param referenceTypeFilter restricts targets to References of this type or a subtype; a null
   *     (or null-valued) NodeId means no restriction beyond {@code AliasFor} and its subtypes.
   * @return the matching entries, each with at least one target, ordered by alias name text then
   *     alias NodeId.
   * @throws UaException with {@code Bad_InvalidArgument} if the pattern is too long or malformed,
   *     or if the filter is not a known ReferenceType; {@code Bad_NodeIdUnknown} if {@code
   *     categoryId} is not an {@code AliasNameCategoryType} instance; {@code Bad_ResponseTooLarge}
   *     if more entries match than the configured maximum.
   */
  public List<AliasNameVerboseDataType> findAliasVerbose(
      NodeId categoryId, String pattern, @Nullable NodeId referenceTypeFilter) throws UaException {

    return findAliasVerbose(categoryId, pattern, referenceTypeFilter, aliasNodeId -> true);
  }

  /**
   * Find aliases under {@code categoryId} whose name matches {@code pattern}, with containing
   * category and target server details, applying a per-result authorization filter.
   *
   * <p>Entries whose alias NodeId the filter rejects are omitted from the result. Filtering happens
   * after matching, so rejected entries still count toward the configured result maximum.
   *
   * @param categoryId the NodeId of the {@code AliasNameCategoryType} instance to search from.
   * @param pattern a Part 4 {@code Like} pattern matched against alias name text.
   * @param referenceTypeFilter restricts targets to References of this type or a subtype; a null
   *     (or null-valued) NodeId means no restriction beyond {@code AliasFor} and its subtypes.
   * @param resultFilter decides, by alias NodeId, whether a matched alias may appear in the result.
   * @return the matching, filter-approved entries, ordered by alias name text then alias NodeId.
   * @throws UaException under the same conditions as {@link #findAliasVerbose(NodeId, String,
   *     NodeId)}.
   */
  List<AliasNameVerboseDataType> findAliasVerbose(
      NodeId categoryId,
      String pattern,
      @Nullable NodeId referenceTypeFilter,
      Predicate<NodeId> resultFilter)
      throws UaException {

    List<MatchedAlias> matches = search(categoryId, pattern, referenceTypeFilter);

    List<AliasNameVerboseDataType> results = new ArrayList<>(matches.size());
    for (MatchedAlias match : matches) {
      if (!resultFilter.test(match.nodeId)) {
        continue;
      }
      results.add(
          new AliasNameVerboseDataType(
              match.browseName,
              match.targets.stream().map(AliasTarget::nodeId).toArray(ExpandedNodeId[]::new),
              serverUrisOf(match.targets),
              match.category.nodeId()));
    }
    return results;
  }

  private List<MatchedAlias> search(
      NodeId categoryId, String pattern, @Nullable NodeId referenceTypeFilter) throws UaException {

    if (pattern.length() > limits.maxPatternLength()) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "pattern length %d exceeds maximum %d"
              .formatted(pattern.length(), limits.maxPatternLength()));
    }

    // Compile the pattern up front: a malformed pattern fails the call before any traversal
    // happens (§6.3.2 maps invalid search strings to Bad_InvalidArgument), and the per-alias
    // match then runs against the compiled form, free of the matcher's shared pattern cache.
    LikeMatcher.CompiledPattern compiledPattern;
    try {
      compiledPattern = likeMatcher.compile(pattern);
    } catch (IllegalArgumentException e) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "malformed pattern: " + pattern, e);
    }

    // A null NodeId on the wire arrives as NodeId.NULL_VALUE; both spellings mean "no filter".
    NodeId filter =
        referenceTypeFilter == null || referenceTypeFilter.isNull() ? null : referenceTypeFilter;
    if (filter != null && !server.getReferenceTypeTree().containsType(filter)) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument, "unknown ReferenceType: " + filter.toParseableString());
    }

    UaNode categoryNode =
        server
            .getAddressSpaceManager()
            .getManagedNode(categoryId)
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_NodeIdUnknown,
                        "category not found: " + categoryId.toParseableString()));

    if (!aliasTypes.isAliasNameCategoryInstance(categoryId)) {
      throw new UaException(
          StatusCodes.Bad_NodeIdUnknown,
          "not an AliasNameCategoryType instance: " + categoryId.toParseableString());
    }

    return traverse(
        new CategoryRef(categoryId, categoryNode.getBrowseName(), 0), compiledPattern, filter);
  }

  /**
   * Breadth-first walk of the category subtree rooted at {@code root}, collecting matched aliases.
   *
   * <p>Breadth-first order visits categories in non-decreasing depth, which the verbose
   * category-selection rule (smallest depth first) relies on only loosely: candidates are compared
   * explicitly, so ties at equal depth are still broken deterministically.
   */
  private List<MatchedAlias> traverse(
      CategoryRef root, LikeMatcher.CompiledPattern pattern, @Nullable NodeId filter)
      throws UaException {

    // LinkedHashMap only for deterministic iteration while debugging; results are re-sorted below.
    Map<NodeId, MatchedAlias> matched = new LinkedHashMap<>();
    Set<NodeId> rejected = new HashSet<>();
    Set<NodeId> visitedCategories = new HashSet<>();
    ArrayDeque<CategoryRef> queue = new ArrayDeque<>();

    visitedCategories.add(root.nodeId());
    queue.addLast(root);

    while (!queue.isEmpty()) {
      CategoryRef category = queue.removeFirst();

      List<Reference> organizes =
          server
              .getAddressSpaceManager()
              .getManagedReferences(category.nodeId(), Reference.ORGANIZES_PREDICATE);

      for (Reference reference : organizes) {
        Optional<NodeId> targetId =
            reference.getTargetNodeId().toNodeId(server.getNamespaceTable());
        if (targetId.isEmpty()) {
          continue;
        }
        NodeId organizedId = targetId.get();

        NodeId typeDefinitionId = aliasTypes.getTypeDefinitionId(organizedId);
        if (typeDefinitionId == null) {
          continue;
        }

        if (aliasTypes.isAliasNameCategoryType(typeDefinitionId)) {
          if (visitedCategories.add(organizedId)) {
            server
                .getAddressSpaceManager()
                .getManagedNode(organizedId)
                .ifPresent(
                    subcategoryNode ->
                        queue.addLast(
                            new CategoryRef(
                                organizedId,
                                subcategoryNode.getBrowseName(),
                                category.depth() + 1)));
          }
        } else if (aliasTypes.isAliasNameType(typeDefinitionId)) {
          processAlias(organizedId, category, pattern, filter, matched, rejected);
        }
      }
    }

    List<MatchedAlias> results = new ArrayList<>(matched.values());
    results.sort(ENTRY_ORDER);
    return results;
  }

  private void processAlias(
      NodeId aliasNodeId,
      CategoryRef category,
      LikeMatcher.CompiledPattern pattern,
      @Nullable NodeId filter,
      Map<NodeId, MatchedAlias> matched,
      Set<NodeId> rejected)
      throws UaException {

    MatchedAlias existing = matched.get(aliasNodeId);
    if (existing != null) {
      // Already matched via another subtree path: only the verbose category selection can change.
      if (CATEGORY_ORDER.compare(category, existing.category) < 0) {
        existing.category = category;
      }
      return;
    }
    if (rejected.contains(aliasNodeId)) {
      // Whether an alias matches is a property of the alias Node alone, not of the path that
      // reached it, so a rejection holds for every other path too.
      return;
    }

    UaNode aliasNode = server.getAddressSpaceManager().getManagedNode(aliasNodeId).orElse(null);
    if (aliasNode == null) {
      rejected.add(aliasNodeId);
      return;
    }

    QualifiedName browseName = aliasNode.getBrowseName();
    String nameText = browseName.name();
    if (nameText == null || !pattern.matches(nameText)) {
      rejected.add(aliasNodeId);
      return;
    }

    List<AliasTarget> targets = collectTargets(aliasNodeId, filter);
    if (targets.isEmpty()) {
      // No target Reference passed the filter; an entry without targets is not returned
      // (§6.3.2/§6.3.3: Nodes that don't "have the appropriate ReferenceType" contribute nothing).
      rejected.add(aliasNodeId);
      return;
    }

    if (matched.size() >= limits.maxResults()) {
      throw new UaException(
          StatusCodes.Bad_ResponseTooLarge,
          "more than %d aliases match".formatted(limits.maxResults()));
    }

    matched.put(
        aliasNodeId, new MatchedAlias(aliasNodeId, browseName, nameText, targets, category));
  }

  /**
   * Collect the targets of {@code aliasNodeId}: forward References whose type is {@code AliasFor}
   * or a subtype and, when a filter is present, also the filter type or a subtype of it.
   *
   * <p>Targets are sorted with the configured ordering; when several References of different types
   * reach the same target Node, the earliest-ordered occurrence decides its position.
   */
  private List<AliasTarget> collectTargets(NodeId aliasNodeId, @Nullable NodeId filter) {
    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(
                aliasNodeId,
                reference ->
                    reference.isForward()
                        && aliasTypes.isAliasForOrSubtype(reference.getReferenceTypeId())
                        && (filter == null
                            || matchesReferenceType(reference.getReferenceTypeId(), filter)));

    List<AliasTarget> ordered =
        references.stream().map(this::toAliasTarget).sorted(targetOrdering).toList();

    var seen = new HashSet<ExpandedNodeId>();
    var targets = new ArrayList<AliasTarget>(ordered.size());
    for (AliasTarget target : ordered) {
      if (seen.add(target.nodeId())) {
        targets.add(target);
      }
    }
    return targets;
  }

  /**
   * The {@code ServerUris} output entries for {@code targets}: null when every target is local
   * (permitted by Part 17 §7.3), otherwise an array parallel to the referenced Nodes whose entries
   * are the remote targets' Server URIs and null for local targets.
   */
  private static @Nullable String @Nullable [] serverUrisOf(List<AliasTarget> targets) {
    if (targets.stream().allMatch(AliasTarget::isLocal)) {
      return null;
    }

    var serverUris = new @Nullable String[targets.size()];
    for (int i = 0; i < serverUris.length; i++) {
      serverUris[i] = targets.get(i).serverUri();
    }
    return serverUris;
  }

  /**
   * Adapt a target Reference to an {@link AliasTarget} so the configured {@code
   * Comparator<AliasTarget>} can order it.
   *
   * <p>The server URI of a remote target is resolved through the ServerTable; if the target's
   * server index has no ServerTable entry, the raw index is carried in the URI position so the
   * target still classifies as remote for ordering purposes.
   */
  private AliasTarget toAliasTarget(Reference reference) {
    ExpandedNodeId targetId = reference.getTargetNodeId();

    String serverUri = null;
    if (!targetId.isLocal()) {
      serverUri = targetId.getServerUri(server.getServerTable());
      if (serverUri == null) {
        serverUri = String.valueOf(targetId.getServerIndex());
      }
    }

    return new AliasTarget(targetId, serverUri, reference.getReferenceTypeId());
  }

  /** "Any ReferenceType includes all subtypes" (§6.3.3), so a filter admits itself and subtypes. */
  private boolean matchesReferenceType(NodeId referenceTypeId, NodeId filter) {
    return referenceTypeId.equals(filter)
        || server.getReferenceTypeTree().isSubtypeOf(referenceTypeId, filter);
  }

  /** A category on the traversal frontier, with its depth from the search root (root = 0). */
  private record CategoryRef(NodeId nodeId, QualifiedName browseName, int depth) {}

  /** A matched alias accumulated during traversal; {@code category} is refined as paths arrive. */
  private static final class MatchedAlias {

    final NodeId nodeId;
    final QualifiedName browseName;
    final String nameText;
    final String parseableNodeId;
    final List<AliasTarget> targets;

    CategoryRef category;

    MatchedAlias(
        NodeId nodeId,
        QualifiedName browseName,
        String nameText,
        List<AliasTarget> targets,
        CategoryRef category) {
      this.nodeId = nodeId;
      this.browseName = browseName;
      this.nameText = nameText;
      this.parseableNodeId = nodeId.toParseableString();
      this.targets = targets;
      this.category = category;
    }
  }
}

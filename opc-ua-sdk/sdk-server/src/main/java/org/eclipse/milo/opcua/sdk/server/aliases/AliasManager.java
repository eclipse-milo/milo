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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AbstractLifecycle;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceComposite;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpaceFragmentWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.BrowsePath;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationResult;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.NodeInstantiator;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Opt-in server-side support for OPC UA Part 17 Alias Names: binds {@code FindAlias} (and
 * optionally {@code FindAliasVerbose}, {@code AddAliasesToCategory}, and {@code
 * DeleteAliasesFromCategory}) behavior onto the standard {@code Aliases}, {@code TagVariables}, and
 * {@code Topics} Objects, manages application-defined alias categories, and maintains the {@code
 * LastChange} version invariant.
 *
 * <p>Construct with the {@link OpcUaServer} and an {@link AliasManagerConfig}, then call {@link
 * #startup()} <em>after</em> the server has started. Startup fails if a standard {@code FindAlias}
 * Method already has an application-bound invocation handler, if a NodeId needed for a materialized
 * Method Node is already in use, or if the configured {@link AliasVersionStore} cannot be read; a
 * failed startup rolls back anything it had already applied, leaving no trace. On {@link
 * #shutdown()} the manager unbinds its handlers, clears the executable flags it restored, removes
 * the Method Nodes it materialized, and unregisters its AddressSpace fragment.
 *
 * <h2>Node hosting</h2>
 *
 * <p>The manager owns an AddressSpace fragment (with its own NodeManager) that it registers with
 * the server's AddressSpaceManager at startup and unregisters at shutdown; the fragment claims
 * exactly the Nodes it contains, so services like Read and Browse route to them regardless of their
 * NodeId's namespace. The hosting rule is: every Node the manager itself creates outside an
 * application namespace — the Optional Method Nodes materialized on the standard Objects, and alias
 * Nodes created by {@link #addAlias} in standard or adopted categories — lives in the manager's
 * fragment, and therefore leaves the AddressSpace when the manager shuts down. Category and alias
 * Nodes created through {@link #addCategory} live in the application-supplied {@link NodeManager},
 * whose owning namespace claims them; they remain in place at shutdown.
 *
 * <p>Categories are registered through {@link #addCategory} (materializes a new {@code
 * AliasNameCategoryType} instance) or {@link #adoptCategory} (binds behavior onto an existing
 * instance, e.g. one loaded from a NodeSet file). Aliases are created and removed through {@link
 * #addAlias} and {@link #deleteAlias}; these programmatic paths are trusted application code and
 * are not subject to the {@link AliasAuthorizationPolicy}. All mutation must flow through the
 * manager (or be followed by {@link #touch}) for {@code LastChange} correctness; lookups read the
 * live AddressSpace and need no registration.
 *
 * <p>Mutations are serialized by a manager-wide lock. Lookups take no lock and may observe a
 * concurrent mutation partially applied — the same weak consistency Browse has.
 */
public final class AliasManager extends AbstractLifecycle {

  private static final List<NodeId> STANDARD_CATEGORY_IDS =
      List.of(NodeIds.Aliases, NodeIds.TagVariables, NodeIds.Topics);

  private static final List<NodeId> STANDARD_FIND_ALIAS_NODE_IDS =
      List.of(NodeIds.Aliases_FindAlias, NodeIds.TagVariables_FindAlias, NodeIds.Topics_FindAlias);

  private static final BrowsePath FIND_ALIAS_PATH =
      BrowsePath.of(new QualifiedName(0, "FindAlias"));

  private static final BrowsePath FIND_ALIAS_VERBOSE_PATH =
      BrowsePath.of(new QualifiedName(0, "FindAliasVerbose"));

  private static final BrowsePath ADD_ALIASES_TO_CATEGORY_PATH =
      BrowsePath.of(new QualifiedName(0, "AddAliasesToCategory"));

  private static final BrowsePath DELETE_ALIASES_FROM_CATEGORY_PATH =
      BrowsePath.of(new QualifiedName(0, "DeleteAliasesFromCategory"));

  private static final BrowsePath LAST_CHANGE_PATH =
      BrowsePath.of(new QualifiedName(0, "LastChange"));

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ReentrantLock lock = new ReentrantLock();

  /** Managed categories, keyed by category NodeId. Guarded by {@link #lock}. */
  private final Map<NodeId, CategoryRecord> categories = new HashMap<>();

  /** The standard {@code FindAlias} Nodes bound at startup. Guarded by {@link #lock}. */
  private final List<UaMethodNode> boundStandardMethodNodes = new ArrayList<>();

  /** Method Nodes materialized on the standard Objects at startup. Guarded by {@link #lock}. */
  private final List<UaMethodNode> materializedMethodNodes = new ArrayList<>();

  private final AliasSearchEngine searchEngine;
  private final AliasVersionManager versionManager;
  private final AliasTypes aliasTypes;

  /** Hosts the Nodes the manager creates outside application namespaces. */
  private final AliasFragment fragment;

  private final OpcUaServer server;
  private final AliasManagerConfig config;

  /**
   * Create an {@link AliasManager} for {@code server}.
   *
   * <p>The manager does nothing until {@link #startup()} is called, which must happen after the
   * server itself has started (the standard namespace Nodes must exist).
   *
   * @param server the server whose AddressSpace the manager operates on.
   * @param config the manager configuration.
   */
  public AliasManager(OpcUaServer server, AliasManagerConfig config) {
    this.server = server;
    this.config = config;

    searchEngine = new AliasSearchEngine(server, config.getLimits(), config.getTargetOrdering());
    versionManager = new AliasVersionManager(server, config.getVersionStore());
    aliasTypes = new AliasTypes(server);
    fragment = new AliasFragment(server);
  }

  @Override
  protected void onStartup() {
    lock.lock();
    try {
      // Validation phase: check everything that can foreseeably fail BEFORE mutating any state,
      // so an expected failure (handler conflict, NodeId collision, unreadable version store)
      // leaves no trace behind.
      List<UaMethodNode> findAliasNodes = resolveStandardFindAliasNodes();

      for (UaMethodNode methodNode : findAliasNodes) {
        MethodInvocationHandler handler = methodNode.getInvocationHandler();

        if (!(handler instanceof MethodInvocationHandler.NotImplementedHandler)) {
          throw new IllegalStateException(
              "FindAlias Method %s already has an invocation handler: %s"
                  .formatted(
                      methodNode.getNodeId().toParseableString(), handler.getClass().getName()));
        }
      }

      List<MethodPlan> methodPlans = resolveStandardMethodPlans();

      // Mutation phase: pre-validation makes failure here unexpected, but if anything throws
      // anyway, roll back whatever was already applied before rethrowing.
      boolean fragmentStarted = false;
      try {
        Map<NodeId, UInteger> persisted;
        try {
          // A load failure throws before the version manager mutates any state; on success the
          // persisted values are seeded in memory only — they are republished to the LastChange
          // Properties at the end of startup, after every step that can fail.
          persisted = versionManager.loadPersisted();
        } catch (UaException e) {
          throw new IllegalStateException("failed to load persisted LastChange versions", e);
        }

        fragment.startup();
        fragmentStarted = true;

        for (UaMethodNode methodNode : findAliasNodes) {
          bindHandler(
              methodNode,
              new FindAliasMethodImpl(methodNode, searchEngine, config.getAuthorizationPolicy()));

          boundStandardMethodNodes.add(methodNode);
        }

        for (MethodPlan methodPlan : methodPlans) {
          materializeMethod(methodPlan);
        }

        // The root Aliases Object's LastChange value is mandatory and persisted; when nothing was
        // persisted yet, initialize it to a freshly computed VersionTime. Done last so the store
        // is not written to until every other startup step has succeeded.
        if (!persisted.containsKey(NodeIds.Aliases)) {
          try {
            versionManager.touch(NodeIds.Aliases);
          } catch (UaException e) {
            // A store that cannot save is as disqualifying as one that cannot load: continuing
            // without a persisted root version would violate the §6.3.1 persistence contract.
            throw new IllegalStateException("failed to persist initial LastChange version", e);
          }
        }

        // Publish the loaded versions to the LastChange Properties only now, after every step
        // that can fail, so a failed startup leaves no trace in the AddressSpace.
        versionManager.publishLoaded();
      } catch (RuntimeException | Error e) {
        rollbackStartup(fragmentStarted);
        throw e;
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Undo the state a partially completed startup applied: unbind handlers, delete materialized
   * Nodes, and unregister the fragment. Best-effort; failures are logged, not propagated, so the
   * original startup failure is the one the caller sees.
   */
  private void rollbackStartup(boolean fragmentStarted) {
    for (UaMethodNode methodNode : boundStandardMethodNodes) {
      try {
        unbindHandler(methodNode);
      } catch (Exception e) {
        logger.warn("Rollback failed to unbind handler: {}", methodNode.getNodeId(), e);
      }
    }
    boundStandardMethodNodes.clear();

    for (UaMethodNode methodNode : materializedMethodNodes) {
      try {
        methodNode.delete();
      } catch (Exception e) {
        logger.warn("Rollback failed to delete Method Node: {}", methodNode.getNodeId(), e);
      }
    }
    materializedMethodNodes.clear();

    if (fragmentStarted) {
      try {
        fragment.shutdown();
      } catch (Exception e) {
        logger.warn("Rollback failed to shut down AddressSpace fragment", e);
      }
    }
  }

  @Override
  protected void onShutdown() {
    lock.lock();
    try {
      for (UaMethodNode methodNode : boundStandardMethodNodes) {
        unbindHandler(methodNode);
      }
      boundStandardMethodNodes.clear();

      for (UaMethodNode methodNode : materializedMethodNodes) {
        methodNode.delete();
      }
      materializedMethodNodes.clear();

      for (CategoryRecord record : categories.values()) {
        for (UaMethodNode methodNode : record.boundMethodNodes()) {
          unbindHandler(methodNode);
        }
      }
      categories.clear();

      // Last, mirroring startup order: the fragment (and the alias Nodes it still hosts) leaves
      // the AddressSpace only after every handler is unbound and every materialized Node deleted.
      fragment.shutdown();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Materialize a new {@code AliasNameCategoryType} instance and manage it.
   *
   * <p>The category Node's NodeId is the config's {@code categoryNodeId}; member Nodes get NodeIds
   * derived from it, and the {@code aliasNodeIdFactory} is reserved for alias Nodes (it is never
   * invoked here, so stateful factories are safe). The mandatory {@code FindAlias} Method is always
   * bound; {@code FindAliasVerbose}, the {@code LastChange} Property, and the mutation Methods
   * ({@code AddAliasesToCategory} / {@code DeleteAliasesFromCategory}) are materialized when
   * enabled by the config. Mutation Methods are network-callable only for sessions the {@link
   * AliasAuthorizationPolicy} grants mutation to; the default policy denies every session.
   *
   * @param categoryConfig describes the category to create.
   * @return a handle for the created category.
   * @throws UaException with {@code Bad_InvalidArgument} if the BrowseName has no name text; {@code
   *     Bad_NodeIdExists} if a category with the same NodeId is already managed; {@code
   *     Bad_InternalError} if the category's initial {@code LastChange} version cannot be persisted
   *     (the instantiation is undone, so nothing is created); or if instantiation fails (e.g. the
   *     parent does not exist or a NodeId collides).
   * @throws IllegalStateException if the manager is not running.
   */
  public AliasCategory addCategory(AliasCategoryConfig categoryConfig) throws UaException {
    lock.lock();
    try {
      checkRunning();

      String name = categoryConfig.browseName().getName();
      if (name == null || name.isEmpty()) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument, "category BrowseName has no name text");
      }

      NodeId categoryId = categoryConfig.categoryNodeId();

      if (categories.containsKey(categoryId)) {
        throw new UaException(
            StatusCodes.Bad_NodeIdExists,
            "category already managed: " + categoryId.toParseableString());
      }

      var boundMethodNodes = new ArrayList<UaMethodNode>();

      InstantiationRequest.Builder<AliasNameCategoryTypeNode> builder =
          InstantiationRequest.of(AliasNameCategoryTypeNode.class, NodeIds.AliasNameCategoryType)
              .nodeId(categoryId)
              .browseName(categoryConfig.browseName())
              .displayName(LocalizedText.english(name))
              .parent(categoryConfig.parentCategoryId(), NodeIds.Organizes)
              .target(categoryConfig.nodeManager());

      bindMethodAt(
          builder,
          FIND_ALIAS_PATH,
          boundMethodNodes,
          methodNode ->
              new FindAliasMethodImpl(methodNode, searchEngine, config.getAuthorizationPolicy()));

      if (categoryConfig.findAliasVerboseEnabled()) {
        builder.includeOptional(FIND_ALIAS_VERBOSE_PATH);
        bindMethodAt(
            builder,
            FIND_ALIAS_VERBOSE_PATH,
            boundMethodNodes,
            methodNode ->
                new FindAliasVerboseMethodImpl(
                    methodNode, searchEngine, config.getAuthorizationPolicy()));
      }

      if (categoryConfig.configurationEnabled()) {
        builder.includeOptional(ADD_ALIASES_TO_CATEGORY_PATH);
        bindMethodAt(
            builder,
            ADD_ALIASES_TO_CATEGORY_PATH,
            boundMethodNodes,
            methodNode ->
                new AddAliasesToCategoryMethodImpl(
                    methodNode, this, config.getAuthorizationPolicy()));

        builder.includeOptional(DELETE_ALIASES_FROM_CATEGORY_PATH);
        bindMethodAt(
            builder,
            DELETE_ALIASES_FROM_CATEGORY_PATH,
            boundMethodNodes,
            methodNode ->
                new DeleteAliasesFromCategoryMethodImpl(
                    methodNode, this, config.getAuthorizationPolicy()));
      }

      if (categoryConfig.lastChangeEnabled()) {
        builder.includeOptional(LAST_CHANGE_PATH);
      }

      InstantiationResult<AliasNameCategoryTypeNode> result =
          new NodeInstantiator(server).instantiate(builder.build());

      if (categoryConfig.lastChangeEnabled()) {
        try {
          // Initialize the new category's LastChange Property (and propagate to its ancestors).
          versionManager.touch(categoryId);
        } catch (UaException e) {
          // The initial version could not be persisted, so the category must not come into
          // existence: an unpersisted LastChange could repeat after a restart and leave Client
          // caches undetectably stale. Undo the instantiation and report a clean failure.
          for (UaMethodNode methodNode : boundMethodNodes) {
            unbindHandler(methodNode);
          }
          result.deleteCreated();
          versionManager.remove(categoryId);
          throw e;
        }
      }

      var category =
          new AliasCategory(
              categoryId,
              categoryConfig.browseName(),
              categoryConfig.lastChangeEnabled(),
              categoryConfig.findAliasVerboseEnabled(),
              categoryConfig.configurationEnabled());

      categories.put(
          categoryId,
          new CategoryRecord(
              categoryConfig.aliasNodeIdFactory(),
              categoryConfig.nodeManager(),
              boundMethodNodes,
              result));

      return category;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Manage an existing {@code AliasNameCategoryType} instance, e.g. one loaded from a NodeSet file.
   *
   * <p>Adoption is non-recursive: exactly the given category is adopted, and subcategories found in
   * the graph must be adopted individually if they should be bound too (they are still
   * <em>searched</em> by any enclosing category's {@code FindAlias} regardless). The mandatory
   * {@code FindAlias} Method is always bound. Pre-existing optional Method Nodes ({@code
   * FindAliasVerbose}, {@code AddAliasesToCategory}, {@code DeleteAliasesFromCategory}) — the
   * category's definition (e.g. its NodeSet) decided to offer them — are each bound only if still
   * unbound; an instance that already has an invocation handler is left untouched, and a Method the
   * category does not have is not created. Bound mutation Methods are network-callable only for
   * sessions the {@link AliasAuthorizationPolicy} grants mutation to; the default policy denies
   * every session. Alias Nodes created by {@link #addAlias} on an adopted category are hosted in
   * the manager's own AddressSpace fragment, with NodeIds allocated in the config's Node namespace
   * as {@code "<category NodeId>/Alias/<alias name>"}.
   *
   * <p>Binding publishes the {@code InputArguments}/{@code OutputArguments} Properties the handler
   * declares onto the adopted Method Nodes. Shutdown unbinds the handlers but does not remove or
   * restore those Properties — they remain, correctly describing the (again non-executable)
   * Methods.
   *
   * @param categoryId the NodeId of the category to adopt.
   * @return a handle for the adopted category; {@code lastChangeEnabled} reflects whether the
   *     category has a {@code LastChange} Property Node, {@code findAliasVerboseEnabled} reflects
   *     whether a {@code FindAliasVerbose} Method was bound, and {@code configurationEnabled}
   *     reflects whether at least one mutation Method was bound.
   * @throws UaException with {@code Bad_NodeIdExists} if the category is already managed; {@code
   *     Bad_InvalidArgument} if it is a standard category (those are managed automatically); {@code
   *     Bad_NodeIdUnknown} if it does not exist or is not an {@code AliasNameCategoryType}
   *     instance; {@code Bad_NotFound} if it has no {@code FindAlias} Method Node; {@code
   *     Bad_InvalidState} if its {@code FindAlias} Method already has an invocation handler.
   * @throws IllegalStateException if the manager is not running.
   */
  public AliasCategory adoptCategory(NodeId categoryId) throws UaException {
    lock.lock();
    try {
      checkRunning();

      if (categories.containsKey(categoryId)) {
        throw new UaException(
            StatusCodes.Bad_NodeIdExists,
            "category already managed: " + categoryId.toParseableString());
      }

      if (STANDARD_CATEGORY_IDS.contains(categoryId)) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument,
            "standard categories are managed automatically: " + categoryId.toParseableString());
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

      UaMethodNode findAliasNode =
          findComponentMethodNode(categoryId, new QualifiedName(0, "FindAlias"));

      if (findAliasNode == null) {
        throw new UaException(
            StatusCodes.Bad_NotFound,
            "category has no FindAlias Method: " + categoryId.toParseableString());
      }

      MethodInvocationHandler handler = findAliasNode.getInvocationHandler();
      if (!(handler instanceof MethodInvocationHandler.NotImplementedHandler)) {
        throw new UaException(
            StatusCodes.Bad_InvalidState,
            "FindAlias Method %s already has an invocation handler: %s"
                .formatted(
                    findAliasNode.getNodeId().toParseableString(), handler.getClass().getName()));
      }

      bindHandler(
          findAliasNode,
          new FindAliasMethodImpl(findAliasNode, searchEngine, config.getAuthorizationPolicy()));

      var boundMethodNodes = new ArrayList<UaMethodNode>();
      boundMethodNodes.add(findAliasNode);

      // Pre-existing optional Method Nodes were the category definition's decision to offer;
      // give them behavior, but only where no other component already has.
      boolean verboseBound =
          bindOptionalMethodIfUnbound(
              categoryId,
              "FindAliasVerbose",
              methodNode ->
                  new FindAliasVerboseMethodImpl(
                      methodNode, searchEngine, config.getAuthorizationPolicy()),
              boundMethodNodes);

      boolean addBound =
          bindOptionalMethodIfUnbound(
              categoryId,
              "AddAliasesToCategory",
              methodNode ->
                  new AddAliasesToCategoryMethodImpl(
                      methodNode, this, config.getAuthorizationPolicy()),
              boundMethodNodes);

      boolean deleteBound =
          bindOptionalMethodIfUnbound(
              categoryId,
              "DeleteAliasesFromCategory",
              methodNode ->
                  new DeleteAliasesFromCategoryMethodImpl(
                      methodNode, this, config.getAuthorizationPolicy()),
              boundMethodNodes);

      boolean lastChangeEnabled =
          categoryNode.getPropertyNode(AliasNameCategoryType.LAST_CHANGE).isPresent();

      var category =
          new AliasCategory(
              categoryId,
              categoryNode.getBrowseName(),
              lastChangeEnabled,
              verboseBound,
              addBound || deleteBound);

      categories.put(
          categoryId,
          new CategoryRecord(
              defaultAliasNodeIdFactory(categoryId),
              fragment.getNodeManager(),
              boundMethodNodes,
              null));

      return category;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Stop managing a category: unbind its Method handlers and, if the category was created by {@link
   * #addCategory}, delete the Nodes and References that creation added.
   *
   * <p>Alias Nodes and child categories added after the category's creation survive. Their {@code
   * Organizes} linkage to the deleted category is removed so recreating its NodeId does not
   * reconnect them. Delete aliases first with {@link #deleteAlias} if they should not outlive the
   * category.
   *
   * <p>When the category Node is deleted, {@code LastChange} is bumped for its former ancestor
   * categories (captured before deletion). The category's version high-water mark is retained for
   * the manager's lifetime so recreating its NodeId continues the sequence. Removing an
   * <em>adopted</em> category only unbinds handlers — the AddressSpace is unchanged, so no version
   * is bumped.
   *
   * @param categoryId the NodeId of the category to remove.
   * @throws UaException with {@code Bad_InvalidArgument} if {@code categoryId} is a standard
   *     category (those cannot be removed); {@code Bad_NodeIdUnknown} if it is not managed; {@code
   *     Bad_InternalError} if the ancestor categories' new {@code LastChange} versions cannot be
   *     persisted (the category is left untouched).
   * @throws IllegalStateException if the manager is not running.
   */
  public void removeCategory(NodeId categoryId) throws UaException {
    lock.lock();
    try {
      checkRunning();

      if (STANDARD_CATEGORY_IDS.contains(categoryId)) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument,
            "standard categories cannot be removed: " + categoryId.toParseableString());
      }

      CategoryRecord record = categories.get(categoryId);
      if (record == null) {
        throw new UaException(
            StatusCodes.Bad_NodeIdUnknown,
            "category not managed: " + categoryId.toParseableString());
      }

      try {
        if (record.instantiationResult() != null) {
          // Capture the ancestor chain before deletion — once the category Node and its parent
          // linkage are gone, the ancestors are no longer discoverable from it — and persist
          // their new versions before any mutation: a failed save aborts the removal with
          // nothing changed, leaving the category managed. Values saved before the failure
          // stay pending and are published by the finally, per the prepare contract.
          List<NodeId> ancestors = versionManager.getAncestorCategories(categoryId);

          if (!ancestors.isEmpty()) {
            versionManager.prepare(ancestors);
          }
        }

        categories.remove(categoryId);

        for (UaMethodNode methodNode : record.boundMethodNodes()) {
          unbindHandler(methodNode);
        }

        if (record.instantiationResult() != null) {
          // A surviving member's linkage may have been added after this category's creation
          // journal. Detach both aliases and child categories before deleting the parent.
          for (NodeId memberId : findOrganizedMembers(categoryId)) {
            server
                .getAddressSpaceManager()
                .getManagedNode(memberId)
                .ifPresent(member -> removeFromCategory(member, categoryId));
          }

          record.instantiationResult().deleteCreated();

          versionManager.remove(categoryId);
        }
      } finally {
        versionManager.publishPending();
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Create an alias in a category, or extend an existing alias of the same name with additional
   * targets.
   *
   * <p>The category must be managed (via {@link #addCategory} or {@link #adoptCategory}) or one of
   * the standard categories ({@code Aliases}, {@code TagVariables}, {@code Topics}). The alias
   * Node's NodeId is allocated by the owning category's alias NodeId factory; for standard
   * categories a default factory allocates {@code "<category NodeId>/Alias/<alias name>"} in the
   * config's Node namespace, and the Node is hosted in the manager's own AddressSpace fragment (it
   * leaves the AddressSpace when the manager shuts down). The alias Node's BrowseName uses its own
   * NodeId's namespace index — lookup ignores the BrowseName namespace, so this is a presentation
   * choice only.
   *
   * <p>Targets organized under the standard {@code TagVariables} and {@code Topics} Objects are
   * constrained: when {@code categoryId} is {@code TagVariables} or {@code Topics}, or a category
   * whose ancestor {@code Organizes} chain reaches one of them, each target Node must have the
   * Variable NodeClass (TagVariables) or be an instance of {@code PublishedDataSetType} or a
   * subtype (Topics).
   *
   * <p>Adding is idempotent: if an alias of the same name already exists in the category, missing
   * target References are added and the existing NodeId is returned; if every given target is
   * already associated, nothing changes. {@code LastChange} is bumped for the category (and its
   * ancestors) whenever something actually changed.
   *
   * <p>The existing-alias lookup scans the category's directly organized members (the AddressSpace
   * is the single source of truth; there is no name index), so each call costs O(category size)
   * under the manager lock — bulk-loading N aliases one by one into the same category is O(N²).
   *
   * <p>This programmatic path is trusted application code: the {@link AliasAuthorizationPolicy} is
   * not consulted.
   *
   * @param categoryId the NodeId of the organizing category.
   * @param aliasName the alias name; becomes the alias Node's BrowseName text.
   * @param targets the targets to associate; at least one, all local.
   * @return the NodeId of the created (or pre-existing) alias Node.
   * @throws UaException with {@code Bad_NodeIdUnknown} if the category is not managed-or-standard,
   *     does not exist, or a target Node does not exist; {@code Bad_InvalidArgument} if the alias
   *     name is empty, {@code targets} is empty, a target's ReferenceType is not {@code AliasFor}
   *     or a subtype, or a target violates the TagVariables/Topics NodeClass constraint; {@code
   *     Bad_NotSupported} for a remote target; {@code Bad_NodeIdExists} if the allocated alias
   *     NodeId is already in use by a different Node; {@code Bad_InternalError} if the affected
   *     categories' new {@code LastChange} versions cannot be persisted (the mutation is not
   *     applied).
   * @throws IllegalStateException if the manager is not running.
   */
  public NodeId addAlias(NodeId categoryId, String aliasName, List<AliasTarget> targets)
      throws UaException {

    lock.lock();
    try {
      checkRunning();

      CategoryRecord record = resolveCategoryRecord(categoryId);

      if (aliasName.isEmpty()) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "aliasName is empty");
      }
      if (targets.isEmpty()) {
        throw new UaException(StatusCodes.Bad_InvalidArgument, "at least one target is required");
      }

      TargetConstraint constraint = getTargetConstraint(categoryId);

      var resolvedTargets = new LinkedHashSet<ResolvedTarget>();
      for (AliasTarget target : targets) {
        resolvedTargets.add(resolveTarget(target, constraint));
      }

      try {
        return applyAddAlias(record, categoryId, aliasName, resolvedTargets);
      } finally {
        versionManager.publishPending();
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Create an alias in a category, or extend an existing alias of the same name with the missing
   * targets among {@code resolvedTargets} — the shared apply step of the programmatic {@link
   * #addAlias} and the per-entry Method path, called with the lock held and every input already
   * validated. The caller publishes the prepared versions in a {@code finally}.
   *
   * <p>The affected categories' new versions are prepared (persisted) as soon as a change is about
   * to be applied — before the mutating call, so the LastChange bump both aborts the mutation when
   * persistence fails and survives a mutation that throws partway through. A fully duplicate
   * association changes nothing, prepares nothing, and is not an error (Part 17 §6.3.4
   * idempotency).
   *
   * @return the NodeId of the created (or pre-existing) alias Node.
   */
  private NodeId applyAddAlias(
      CategoryRecord record,
      NodeId categoryId,
      String aliasName,
      Collection<ResolvedTarget> resolvedTargets)
      throws UaException {

    NodeId existingAliasId = findAliasInCategory(categoryId, aliasName);

    if (existingAliasId != null) {
      UaNode aliasNode =
          server
              .getAddressSpaceManager()
              .getManagedNode(existingAliasId)
              .orElseThrow(
                  () ->
                      new UaException(
                          StatusCodes.Bad_NodeIdUnknown,
                          "alias not found: " + existingAliasId.toParseableString()));

      // One Reference scan covers the whole batch; testing each target against a fresh scan
      // would repeat the aggregated AddressSpace query per target.
      Set<ResolvedTarget> existingAssociations = collectExistingAssociations(existingAliasId);

      boolean prepared = false;

      for (ResolvedTarget target : resolvedTargets) {
        if (!existingAssociations.contains(target)) {
          // A target change is observable from every category that organizes the alias, not
          // just the one addressed, so all of them get a LastChange bump. Prepared BEFORE the
          // mutation so a failed save aborts the change and a Reference add that throws
          // partway through still gets its bumps published.
          if (!prepared) {
            var bumped = new ArrayList<NodeId>();
            bumped.add(categoryId);
            bumped.addAll(getOrganizingCategories(existingAliasId));
            versionManager.prepare(bumped);
            prepared = true;
          }

          aliasNode.addReference(
              new Reference(
                  existingAliasId,
                  target.referenceTypeId(),
                  target.nodeId().expanded(),
                  Reference.Direction.FORWARD));
        }
      }

      return existingAliasId;
    }

    NodeId aliasNodeId = record.aliasNodeIdFactory().apply(aliasName);

    if (server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent()) {
      throw new UaException(
          StatusCodes.Bad_NodeIdExists,
          "alias NodeId already in use: " + aliasNodeId.toParseableString());
    }

    var aliasNode =
        new AliasNameTypeNode(
            new ManagedNodeContext(server, record.nodeManager()),
            aliasNodeId,
            new QualifiedName(aliasNodeId.getNamespaceIndex(), aliasName),
            // §6.2: the DisplayName is the BrowseName text "with an empty locale id and no
            // other locale shall be provided" — hence not the single-argument constructor,
            // which defaults the locale to "en".
            new LocalizedText(null, aliasName),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            null);

    // Prepared BEFORE the mutations so a failed save aborts the creation with nothing applied,
    // and the LastChange bump publishes even if the Node add or a Reference add throws partway
    // through.
    versionManager.prepare(List.of(categoryId));

    record.nodeManager().addNode(aliasNode);

    try {
      aliasNode.addReference(
          new Reference(
              aliasNodeId,
              NodeIds.HasTypeDefinition,
              NodeIds.AliasNameType.expanded(),
              Reference.Direction.FORWARD));

      aliasNode.addReference(
          new Reference(
              aliasNodeId, NodeIds.Organizes, categoryId.expanded(), Reference.Direction.INVERSE));

      for (ResolvedTarget target : resolvedTargets) {
        aliasNode.addReference(
            new Reference(
                aliasNodeId,
                target.referenceTypeId(),
                target.nodeId().expanded(),
                Reference.Direction.FORWARD));
      }
    } catch (RuntimeException e) {
      // A partially wired alias Node — untyped, unlinked, or targetless — violates the Part 17
      // model and would be visible to searches forever, so a failure after addNode removes the
      // Node again. Best-effort only: the removal runs through the same NodeManager that just
      // threw, so its own failure is logged and the original failure propagates.
      try {
        aliasNode.delete();
      } catch (RuntimeException suppressed) {
        e.addSuppressed(suppressed);
        logger.error(
            "Failed to remove partially created alias Node: {}",
            aliasNodeId.toParseableString(),
            suppressed);
      }
      throw e;
    }

    return aliasNodeId;
  }

  /**
   * Delete an alias from a category, or remove individual target References from it.
   *
   * <p>With {@code targets == null} the alias Object is removed from this category; if no other
   * category organizes it, the Object and all its References are deleted. With explicit targets,
   * the matching {@code AliasFor}-or-subtype References are removed (removing a target that is not
   * associated is a no-op); if the last such Reference is removed, the alias Object is deleted from
   * <em>every</em> organizing category, because an alias without a target violates the Part 17
   * model. {@code LastChange} is bumped for every affected category.
   *
   * <p>This programmatic path is trusted application code: the {@link AliasAuthorizationPolicy} is
   * not consulted.
   *
   * @param categoryId the NodeId of the organizing category.
   * @param aliasName the alias name, matched against alias BrowseName text (namespace ignored).
   * @param targets the targets to disassociate, or null to remove the alias from the category.
   * @throws UaException with {@code Bad_NodeIdUnknown} if the category is not managed-or-standard
   *     or does not exist; {@code Bad_NotFound} if no alias of that name exists in the category;
   *     {@code Bad_InvalidArgument} if {@code targets} is non-null but empty, or an explicit
   *     target's ReferenceType is not {@code AliasFor} or a subtype; {@code Bad_InternalError} if
   *     the affected categories' new {@code LastChange} versions cannot be persisted (the mutation
   *     is not applied).
   * @throws IllegalStateException if the manager is not running.
   */
  public void deleteAlias(NodeId categoryId, String aliasName, @Nullable List<AliasTarget> targets)
      throws UaException {

    lock.lock();
    try {
      checkRunning();

      resolveCategoryRecord(categoryId);

      NodeId aliasNodeId = findAliasInCategory(categoryId, aliasName);
      if (aliasNodeId == null) {
        throw new UaException(
            StatusCodes.Bad_NotFound,
            "alias \"%s\" not found in category %s"
                .formatted(aliasName, categoryId.toParseableString()));
      }

      UaNode aliasNode =
          server
              .getAddressSpaceManager()
              .getManagedNode(aliasNodeId)
              .orElseThrow(
                  () ->
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "alias not found: " + aliasNodeId.toParseableString()));

      if (targets == null) {
        try {
          // Persist the category's new version before mutating: a failed save aborts the
          // removal with nothing changed.
          versionManager.prepare(List.of(categoryId));

          removeFromCategory(aliasNode, categoryId);

          if (getOrganizingCategories(aliasNodeId).isEmpty()) {
            aliasNode.delete();
          }
        } finally {
          versionManager.publishPending();
        }
        return;
      }

      if (targets.isEmpty()) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument,
            "targets is empty; pass null to remove the alias from the category");
      }

      // Validate every explicit target's ReferenceType before removing anything: only alias
      // linkage (AliasFor or a subtype) may be removed here. Without this check a caller-supplied
      // ReferenceType like HasTypeDefinition would strip structural References and corrupt the
      // Node.
      for (AliasTarget target : targets) {
        if (!aliasTypes.isAliasForOrSubtype(target.referenceTypeId())) {
          throw new UaException(
              StatusCodes.Bad_InvalidArgument,
              "ReferenceType is not AliasFor or a subtype: "
                  + target.referenceTypeId().toParseableString());
        }
      }

      // Collect the matching References before removing anything, so the affected categories'
      // new versions can be persisted before the first mutation.
      var matching = new ArrayList<Reference>();
      for (AliasTarget target : targets) {
        NodeId targetNodeId = target.nodeId().toNodeId(server.getNamespaceTable()).orElse(null);
        if (targetNodeId == null) {
          // No local Node can match a non-local target; nothing to remove.
          continue;
        }

        matching.addAll(
            findTargetReferences(aliasNodeId, targetNodeId, target.referenceTypeId()::equals));
      }

      if (matching.isEmpty()) {
        return;
      }

      try {
        // A target change is observable from every category that organizes the alias, not just
        // the one addressed, so all of them get a LastChange bump — persisted before the
        // mutation so a failed save aborts it.
        var bumped = new ArrayList<NodeId>();
        bumped.add(categoryId);
        bumped.addAll(getOrganizingCategories(aliasNodeId));
        versionManager.prepare(bumped);

        for (Reference reference : matching) {
          server.getAddressSpaceManager().removeManagedReferences(reference);
        }

        deleteIfTargetless(aliasNode);
      } finally {
        versionManager.publishPending();
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Apply the entries of one {@code AddAliasesToCategory} Method call to a category, returning one
   * StatusCode per entry (Part 17 §6.3.4).
   *
   * <p>Call-level validation failures fail the whole call before any entry is processed: null,
   * empty, or non-parallel {@code AliasNames}/{@code TargetNodes} arrays, a non-empty {@code
   * TargetServers} array of a different length, more entries than the configured
   * operations-per-call limit, or a {@code TargetReferenceType} that is not a known ReferenceType
   * in the {@code AliasFor} hierarchy. Everything else is reported per entry, and a failed entry
   * does not affect any other entry. {@code LastChange} is bumped once per affected category after
   * all entries are processed. Each entry locates its alias by scanning the category's directly
   * organized members (see {@link AliasLimits#maxOperationsPerCall}), so a call costs O(entries
   * &times; category size) under the manager lock.
   *
   * <p>Unlike the programmatic {@link #addAlias} — where a non-local {@link ExpandedNodeId} with a
   * null server URI is rejected with {@code Bad_NotSupported} — this wire path follows §6.3.4's
   * rule that "the ServerIndex in the ExpandedNodeId shall be ignored and the TargetServers Uri
   * shall be used": when an entry's {@code TargetServers} element is null or empty the target is
   * local, and any server reference carried by the wire {@code ExpandedNodeId} is dropped before
   * resolution. A non-empty {@code TargetServers} element still marks the entry's target remote,
   * which fails that entry with {@code Bad_NotSupported}.
   *
   * <p>Called by the network-facing Method handler after authorization; the {@link
   * AliasAuthorizationPolicy} is not consulted here.
   *
   * @param categoryId the NodeId of the category the Method was called on.
   * @param aliasNames the alias names, parallel to {@code targetNodes}.
   * @param targetNodes the target Nodes, parallel to {@code aliasNames}.
   * @param targetServers the target server URIs, either null/empty (all targets local) or parallel
   *     to {@code aliasNames}.
   * @param targetReferenceType the ReferenceType for every created association; null (or a
   *     null-valued NodeId) defaults to {@code AliasFor}.
   * @return one StatusCode per entry, parallel to the inputs.
   * @throws UaException with {@code Bad_InvalidArgument} for invalid array shapes (including all
   *     arrays empty) or an invalid {@code TargetReferenceType}; {@code Bad_TooManyOperations} if
   *     the arrays exceed the operations-per-call limit; {@code Bad_NodeIdUnknown} if the category
   *     is not managed-or-standard or no longer exists; {@code Bad_InvalidState} if the manager is
   *     no longer running.
   */
  StatusCode[] addAliasEntries(
      NodeId categoryId,
      String @Nullable [] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes,
      String @Nullable [] targetServers,
      @Nullable NodeId targetReferenceType)
      throws UaException {

    // §6.3.4 requires AliasNames and TargetNodes to be parallel; absent arrays cannot satisfy it.
    if (aliasNames == null || targetNodes == null || aliasNames.length != targetNodes.length) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument, "AliasNames and TargetNodes must be parallel arrays");
    }

    // §6.3.4 Table 11 defines Bad_InvalidArgument for a call where "the size of the arrays for
    // all arguments except TargetServers is not the same or if all arrays are empty" — so a
    // zero-entry call is a call-level failure, not an empty success.
    if (aliasNames.length == 0) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "all arrays are empty");
    }

    // §6.3.4: a null or empty TargetServers array means every target is on the local server;
    // a non-empty one must be parallel to the other arrays.
    if (targetServers != null
        && targetServers.length > 0
        && targetServers.length != aliasNames.length) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "TargetServers must be null, empty, or parallel to AliasNames");
    }

    // Part 17 §6.3.4 names only Bad_InvalidArgument and Bad_UserAccessDenied as call-level
    // results, but Part 4 defines Bad_TooManyOperations for a request that specifies more
    // operations than the Server supports — exactly this condition, and the code Milo's
    // service-level operation limits already use — so it is preferred over the generic code.
    if (aliasNames.length > config.getLimits().maxOperationsPerCall()) {
      throw new UaException(
          StatusCodes.Bad_TooManyOperations,
          "%d entries exceed the maximum of %d"
              .formatted(aliasNames.length, config.getLimits().maxOperationsPerCall()));
    }

    // §6.3.4: a null TargetReferenceType defaults to AliasFor. Anything else must be a known
    // ReferenceType in the AliasFor hierarchy — every alias must have an AliasFor-or-subtype
    // Reference, so an invalid type fails the whole call, not individual entries.
    NodeId referenceTypeId;
    if (targetReferenceType == null || targetReferenceType.isNull()) {
      referenceTypeId = NodeIds.AliasFor;
    } else {
      if (!server.getReferenceTypeTree().containsType(targetReferenceType)) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument,
            "unknown ReferenceType: " + targetReferenceType.toParseableString());
      }
      if (!aliasTypes.isAliasForOrSubtype(targetReferenceType)) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument,
            "ReferenceType is not AliasFor or a subtype: "
                + targetReferenceType.toParseableString());
      }
      referenceTypeId = targetReferenceType;
    }

    return processEntries(
        categoryId,
        aliasNames.length,
        record -> {
          TargetConstraint constraint = getTargetConstraint(categoryId);

          return i -> {
            String targetServer =
                targetServers != null && targetServers.length > 0 ? targetServers[i] : null;

            return addAliasEntry(
                record,
                categoryId,
                constraint,
                aliasNames[i],
                targetNodes[i],
                targetServer,
                referenceTypeId);
          };
        });
  }

  /**
   * Apply one {@code AddAliasesToCategory} entry, mapping failures to the entry's StatusCode.
   *
   * <p>Validation happens before any mutation, so an entry that fails validation leaves the
   * AddressSpace untouched. A duplicate of an existing association — whether pre-existing or
   * created by an earlier entry of the same call — changes nothing and reports {@code Good}
   * (§6.3.4: such entries "shall be ignored and no error shall be generated"). Application-supplied
   * code (the category's alias NodeId factory, its {@link NodeManager}) can throw unchecked
   * mid-entry, however; such a failure is confined to its entry as {@code Bad_InternalError}, and
   * any category whose new version was prepared before the mutation still gets its LastChange
   * published.
   */
  private StatusCode addAliasEntry(
      CategoryRecord record,
      NodeId categoryId,
      TargetConstraint constraint,
      @Nullable String aliasName,
      @Nullable ExpandedNodeId targetNode,
      @Nullable String targetServer,
      NodeId referenceTypeId) {

    try {
      if (aliasName == null || aliasName.isEmpty()) {
        // §6.3.4's per-entry table defines no code for an invalid alias name (its
        // Bad_NodeIdInvalid is about the TargetNode), so the generic code is used.
        return new StatusCode(StatusCodes.Bad_InvalidArgument);
      }

      if (targetNode == null || targetNode.isNull()) {
        // §6.3.4: "The syntax of the NodeId is not valid."
        return new StatusCode(StatusCodes.Bad_NodeIdInvalid);
      }

      if (targetServer != null && !targetServer.isEmpty()) {
        // §6.3.4 defines Bad_NotSupported for servers that do not support aliases with remote
        // targets, which this manager does not; Uncertain_ReferenceOutOfServer is reserved for
        // servers that accept a remote target they cannot verify.
        return new StatusCode(StatusCodes.Bad_NotSupported);
      }

      // §6.3.4: "The ServerIndex in the ExpandedNodeId shall be ignored and the TargetServers
      // Uri shall be used." The entry's TargetServers element is null or empty here, so the
      // target is local and any server reference the wire ExpandedNodeId carries is dropped
      // before resolution. (The programmatic addAlias keeps its stricter contract: a non-local
      // ExpandedNodeId with a null serverUri is rejected with Bad_NotSupported.)
      var localTargetNode =
          new ExpandedNodeId(
              ExpandedNodeId.ServerReference.of(0),
              targetNode.namespace(),
              targetNode.identifier());

      // resolveTarget rejects unresolvable or missing local targets with Bad_NodeIdUnknown and
      // TagVariables/Topics constraint violations with Bad_InvalidArgument — the §6.3.4
      // per-entry codes.
      ResolvedTarget resolved =
          resolveTarget(new AliasTarget(localTargetNode, null, referenceTypeId), constraint);

      applyAddAlias(record, categoryId, aliasName, List.of(resolved));

      return StatusCode.GOOD;
    } catch (UaException e) {
      return e.getStatusCode();
    } catch (RuntimeException e) {
      // Application-supplied code runs inside an entry (the category's aliasNodeIdFactory, its
      // NodeManager) and can throw unchecked; confine the failure to this entry so the others
      // stay independent (§6.3.4's per-entry contract).
      logger.error("AddAliasesToCategory entry failed: alias \"{}\"", aliasName, e);
      return new StatusCode(StatusCodes.Bad_InternalError);
    }
  }

  /**
   * Apply the entries of one {@code DeleteAliasesFromCategory} Method call to a category, returning
   * one StatusCode per entry (Part 17 §6.3.5).
   *
   * <p>Call-level validation failures fail the whole call before any entry is processed: a null
   * {@code AliasNames} array, a non-null {@code TargetNodes} array of a different length, or more
   * entries than the configured operations-per-call limit. §6.3.5 defines {@code TargetNodes} as a
   * restriction on what is deleted and gives a null <em>entry</em> the meaning "all aliases with
   * the provided name are deleted from the category"; a null {@code TargetNodes} <em>array</em> is
   * read as every entry being null — no restriction anywhere — mirroring how §6.3.4 treats an
   * absent {@code TargetServers} array. Per-entry failures affect only their own entry, and {@code
   * LastChange} is bumped once per affected category after all entries are processed. Each entry
   * locates its aliases by scanning the category's directly organized members (see {@link
   * AliasLimits#maxOperationsPerCall}), so a call costs O(entries &times; category size) under the
   * manager lock.
   *
   * <p>Called by the network-facing Method handler after authorization; the {@link
   * AliasAuthorizationPolicy} is not consulted here.
   *
   * @param categoryId the NodeId of the category the Method was called on.
   * @param aliasNames the alias names to delete.
   * @param targetNodes the per-entry target restrictions, or null for no restrictions.
   * @return one StatusCode per entry, parallel to the inputs.
   * @throws UaException with {@code Bad_InvalidArgument} for invalid array shapes; {@code
   *     Bad_TooManyOperations} if the arrays exceed the operations-per-call limit; {@code
   *     Bad_NodeIdUnknown} if the category is not managed-or-standard or no longer exists; {@code
   *     Bad_InvalidState} if the manager is no longer running.
   */
  StatusCode[] deleteAliasEntries(
      NodeId categoryId, String @Nullable [] aliasNames, ExpandedNodeId @Nullable [] targetNodes)
      throws UaException {

    if (aliasNames == null) {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "AliasNames is null");
    }

    if (targetNodes != null && targetNodes.length != aliasNames.length) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument, "TargetNodes must be null or parallel to AliasNames");
    }

    // Deliberately NO empty-arrays failure here, unlike addAliasEntries: §6.3.5 Table 15's
    // Bad_InvalidArgument covers only "an argument is of the wrong type or the size of the
    // arrays for all arguments is not the same" — it lacks §6.3.4 Table 11's "or if all arrays
    // are empty" clause — so a zero-entry Delete call succeeds vacuously with empty results.

    // See addAliasEntries for why Bad_TooManyOperations is preferred over Bad_InvalidArgument.
    if (aliasNames.length > config.getLimits().maxOperationsPerCall()) {
      throw new UaException(
          StatusCodes.Bad_TooManyOperations,
          "%d entries exceed the maximum of %d"
              .formatted(aliasNames.length, config.getLimits().maxOperationsPerCall()));
    }

    return processEntries(
        categoryId,
        aliasNames.length,
        record ->
            i ->
                deleteAliasEntry(
                    categoryId, aliasNames[i], targetNodes != null ? targetNodes[i] : null));
  }

  /**
   * The shared skeleton of the wire mutation paths ({@link #addAliasEntries}, {@link
   * #deleteAliasEntries}): serialize under the manager lock, re-check the running state, resolve
   * the category, apply one entry at a time, and publish the {@code LastChange} values the entries
   * prepared.
   *
   * <p>Each entry persists the new versions of the categories it affects <em>before</em> its first
   * mutation (a failed save fails the entry with nothing applied); the publish runs in a finally
   * because entries applied before an escaping failure already mutated the AddressSpace, and
   * §6.3.1's LastChange invariant must hold for them regardless.
   */
  private StatusCode[] processEntries(
      NodeId categoryId, int entryCount, Function<CategoryRecord, EntryFunction> entrySetup)
      throws UaException {

    lock.lock();
    try {
      // A Call dispatched before shutdown() can still be mid-dispatch when shutdown wins the
      // lock first; by the time it gets here the fragment is unregistered, so mutating would
      // create ghost state and persist spurious LastChange values. Fail with a defined code
      // instead.
      if (isNotRunning()) {
        throw new UaException(StatusCodes.Bad_InvalidState, "AliasManager is not running");
      }

      EntryFunction entryFunction = entrySetup.apply(resolveCategoryRecord(categoryId));

      var results = new StatusCode[entryCount];

      try {
        for (int i = 0; i < entryCount; i++) {
          results[i] = entryFunction.apply(i);
        }
      } finally {
        versionManager.publishPending();
      }

      return results;
    } finally {
      lock.unlock();
    }
  }

  /** Applies one wire mutation entry, reporting its outcome as the entry's StatusCode. */
  @FunctionalInterface
  private interface EntryFunction {
    StatusCode apply(int index);
  }

  /**
   * Apply one {@code DeleteAliasesFromCategory} entry, mapping failures to the entry's StatusCode.
   *
   * <p>Unlike the programmatic {@link #deleteAlias}, which operates on one deterministically chosen
   * alias, an entry applies to <em>every</em> alias Object of the given name directly organized by
   * the category, per §6.3.5 ("all AliasNames with the provided name"). A null or null-valued
   * target restriction removes those aliases from the category (an alias no other category
   * organizes is deleted entirely); an explicit target removes the matching AliasFor-or-subtype
   * References, and an alias whose last target Reference is removed is deleted from every
   * organizing category, because an alias without a target violates the Part 17 model. Removing a
   * target that is not associated changes nothing and reports {@code Good}; §6.3.5 reserves {@code
   * Bad_NotFound} for an alias name the category does not contain.
   *
   * <p>Validation happens before any mutation, and reference removal through the manager's own
   * fragment cannot partially fail, so an entry ordinarily either fully applies or leaves its state
   * untouched (§6.3.5: "If all targets for an AliasNames array entry cannot be deleted, then none
   * of the targets are deleted"). Aliases living in an application {@link NodeManager} can throw
   * unchecked mid-entry, however; such a failure is confined to its entry as {@code
   * Bad_InternalError}, and any category whose new version was prepared before the mutation still
   * gets its LastChange published.
   */
  private StatusCode deleteAliasEntry(
      NodeId categoryId, @Nullable String aliasName, @Nullable ExpandedNodeId targetNode) {

    if (aliasName == null || aliasName.isEmpty()) {
      // §6.3.5 defines no code for an invalid alias name; the generic code is used, matching
      // the Add path.
      return new StatusCode(StatusCodes.Bad_InvalidArgument);
    }

    try {
      List<NodeId> aliasNodeIds = findAliasesInCategory(categoryId, aliasName);
      if (aliasNodeIds.isEmpty()) {
        // §6.3.5: "The AliasName was not located."
        return new StatusCode(StatusCodes.Bad_NotFound);
      }

      if (targetNode == null || targetNode.isNull()) {
        // §6.3.5: a null or empty TargetNodes entry deletes all aliases with the provided name
        // from the category.
        //
        // Prepared BEFORE the mutations so a failed save fails the entry with nothing applied,
        // and the category's LastChange bump publishes even if a removal throws partway
        // through.
        versionManager.prepare(List.of(categoryId));

        for (NodeId aliasNodeId : aliasNodeIds) {
          UaNode aliasNode =
              server.getAddressSpaceManager().getManagedNode(aliasNodeId).orElse(null);
          if (aliasNode == null) {
            continue;
          }

          removeFromCategory(aliasNode, categoryId);

          if (getOrganizingCategories(aliasNodeId).isEmpty()) {
            aliasNode.delete();
          }
        }

        return StatusCode.GOOD;
      }

      NodeId targetNodeId = targetNode.toNodeId(server.getNamespaceTable()).orElse(null);
      if (targetNodeId == null) {
        // No local Reference can match a non-local target restriction; nothing to remove.
        return StatusCode.GOOD;
      }

      var removals = new LinkedHashMap<UaNode, List<Reference>>();
      var bumped = new LinkedHashSet<NodeId>();

      for (NodeId aliasNodeId : aliasNodeIds) {
        UaNode aliasNode = server.getAddressSpaceManager().getManagedNode(aliasNodeId).orElse(null);
        if (aliasNode == null) {
          continue;
        }

        // The Method carries no ReferenceType argument, so any AliasFor-or-subtype Reference to
        // the target is removed.
        List<Reference> matching =
            findTargetReferences(aliasNodeId, targetNodeId, aliasTypes::isAliasForOrSubtype);

        if (!matching.isEmpty()) {
          removals.put(aliasNode, matching);
          bumped.add(categoryId);
          bumped.addAll(getOrganizingCategories(aliasNodeId));
        }
      }

      // One entry can affect several same-name aliases and their other organizing categories.
      // Persist the entire union before the first deletion, so any save failure leaves every
      // alias in this entry untouched.
      versionManager.prepare(bumped);

      for (var removal : removals.entrySet()) {
        for (Reference reference : removal.getValue()) {
          server.getAddressSpaceManager().removeManagedReferences(reference);
        }
        deleteIfTargetless(removal.getKey());
      }

      return StatusCode.GOOD;
    } catch (UaException e) {
      // A failed LastChange save aborts the entry before its mutation is applied.
      return e.getStatusCode();
    } catch (RuntimeException e) {
      // Application-supplied code runs inside an entry (the aliases and their References can
      // live in an application NodeManager) and can throw unchecked; confine the failure to
      // this entry so the others stay independent (§6.3.5's per-entry contract).
      logger.error("DeleteAliasesFromCategory entry failed: alias \"{}\"", aliasName, e);
      return new StatusCode(StatusCodes.Bad_InternalError);
    }
  }

  /**
   * Find aliases under {@code categoryId} whose name matches {@code pattern}.
   *
   * <p>Uses the same engine the {@code FindAlias} Method uses, but as a trusted programmatic call:
   * the {@link AliasAuthorizationPolicy} is not consulted. No lock is taken; a lookup overlapping a
   * mutation may observe it partially applied.
   *
   * @param categoryId the NodeId of the category to search from.
   * @param pattern a Part 4 {@code Like} pattern matched against alias name text.
   * @param referenceTypeFilter restricts targets to References of this type or a subtype; null
   *     means no restriction beyond {@code AliasFor} and its subtypes.
   * @return the matching entries, ordered by alias name text then alias NodeId.
   * @throws UaException see {@link AliasSearchEngine#findAlias(NodeId, String, NodeId)}.
   * @throws IllegalStateException if the manager is not running.
   */
  public List<AliasNameDataType> findAlias(
      NodeId categoryId, String pattern, @Nullable NodeId referenceTypeFilter) throws UaException {

    checkRunning();

    return searchEngine.findAlias(categoryId, pattern, referenceTypeFilter);
  }

  /**
   * Find aliases under {@code categoryId} whose name matches {@code pattern}, with containing
   * category and target server details.
   *
   * <p>Uses the same engine the {@code FindAliasVerbose} Method uses, but as a trusted programmatic
   * call: the {@link AliasAuthorizationPolicy} is not consulted. No lock is taken; a lookup
   * overlapping a mutation may observe it partially applied.
   *
   * @param categoryId the NodeId of the category to search from.
   * @param pattern a Part 4 {@code Like} pattern matched against alias name text.
   * @param referenceTypeFilter restricts targets to References of this type or a subtype; null
   *     means no restriction beyond {@code AliasFor} and its subtypes.
   * @return the matching entries, ordered by alias name text then alias NodeId.
   * @throws UaException see {@link AliasSearchEngine#findAliasVerbose(NodeId, String, NodeId)}.
   * @throws IllegalStateException if the manager is not running.
   */
  public List<AliasNameVerboseDataType> findAliasVerbose(
      NodeId categoryId, String pattern, @Nullable NodeId referenceTypeFilter) throws UaException {

    checkRunning();

    return searchEngine.findAliasVerbose(categoryId, pattern, referenceTypeFilter);
  }

  /**
   * Bump the {@code LastChange} version of a category and its ancestor categories.
   *
   * <p>An escape hatch for applications that deliberately edited the alias hierarchy directly
   * through a NodeManager: such edits are found by searches but bypass version maintenance, so they
   * must be followed by a {@code touch} for Client caches to invalidate correctly.
   *
   * @param categoryId the NodeId of the category to bump.
   * @throws UaException with {@code Bad_InternalError} if the new version cannot be persisted; a
   *     version is never published without being persisted first.
   * @throws IllegalStateException if the manager is not running.
   */
  public void touch(NodeId categoryId) throws UaException {
    lock.lock();
    try {
      checkRunning();

      versionManager.touch(categoryId);
    } finally {
      lock.unlock();
    }
  }

  private void checkRunning() {
    if (isNotRunning()) {
      throw new IllegalStateException("AliasManager is not running");
    }
  }

  /** Resolve the standard {@code FindAlias} Nodes; a missing Node is logged and skipped. */
  private List<UaMethodNode> resolveStandardFindAliasNodes() {
    var methodNodes = new ArrayList<UaMethodNode>();

    for (NodeId nodeId : STANDARD_FIND_ALIAS_NODE_IDS) {
      UaNode node = server.getAddressSpaceManager().getManagedNode(nodeId).orElse(null);

      if (node instanceof UaMethodNode methodNode) {
        methodNodes.add(methodNode);
      } else {
        logger.warn("FindAlias UaMethodNode not found: {}", nodeId.toParseableString());
      }
    }

    return methodNodes;
  }

  /**
   * Resolve the Optional Method Nodes to materialize on the standard Objects — {@code
   * FindAliasVerbose} when verbose lookup is enabled, {@code AddAliasesToCategory} and {@code
   * DeleteAliasesFromCategory} when configuration is enabled — and verify their NodeIds are free; a
   * missing Object is logged and skipped, a NodeId collision fails startup.
   *
   * <p>Pure validation: no state is mutated, so a throw here leaves no trace.
   */
  private List<MethodPlan> resolveStandardMethodPlans() {
    var plans = new ArrayList<MethodPlan>();

    for (NodeId objectId : STANDARD_CATEGORY_IDS) {
      UaNode objectNode = server.getAddressSpaceManager().getManagedNode(objectId).orElse(null);
      if (objectNode == null) {
        logger.warn(
            "Cannot materialize Optional Methods; Object not found: {}",
            objectId.toParseableString());
        continue;
      }

      // The ns=0 BrowseName of the standard Object seeds the materialized Method NodeIds, e.g.
      // "Aliases/FindAliasVerbose".
      String objectName = objectNode.getBrowseName().name();
      if (objectName == null) {
        logger.warn(
            "Cannot materialize Optional Methods; Object has no BrowseName text: {}",
            objectId.toParseableString());
        continue;
      }

      if (config.isFindAliasVerboseEnabled()) {
        plans.add(
            newMethodPlan(
                objectNode,
                objectName,
                "FindAliasVerbose",
                methodNode ->
                    new FindAliasVerboseMethodImpl(
                        methodNode, searchEngine, config.getAuthorizationPolicy())));
      }

      if (config.isConfigurationEnabled()) {
        plans.add(
            newMethodPlan(
                objectNode,
                objectName,
                "AddAliasesToCategory",
                methodNode ->
                    new AddAliasesToCategoryMethodImpl(
                        methodNode, this, config.getAuthorizationPolicy())));

        plans.add(
            newMethodPlan(
                objectNode,
                objectName,
                "DeleteAliasesFromCategory",
                methodNode ->
                    new DeleteAliasesFromCategoryMethodImpl(
                        methodNode, this, config.getAuthorizationPolicy())));
      }
    }

    return plans;
  }

  /**
   * Build the plan for one Method Node to materialize, verifying its NodeId — {@code
   * "<objectName>/<methodName>"} in the configured Node namespace — is free.
   */
  private MethodPlan newMethodPlan(
      UaNode objectNode,
      String objectName,
      String methodName,
      Function<UaMethodNode, AbstractMethodInvocationHandler> handlerFactory) {

    var methodNodeId = new NodeId(config.getNodeNamespaceIndex(), objectName + "/" + methodName);

    if (server.getAddressSpaceManager().getManagedNode(methodNodeId).isPresent()) {
      throw new IllegalStateException("NodeId already in use: " + methodNodeId.toParseableString());
    }

    return new MethodPlan(
        objectNode, methodNodeId, new QualifiedName(0, methodName), handlerFactory);
  }

  /**
   * Materialize the Method Node described by {@code plan} as a component of its standard Object,
   * bind a handler, and record it for removal at shutdown.
   *
   * <p>The Node lives in the manager's own fragment; its NodeId is in the configured Method-Node
   * namespace and its BrowseName is the ns=0 name of the standard type member it instantiates.
   */
  private void materializeMethod(MethodPlan plan) {
    NodeId methodNodeId = plan.methodNodeId();
    QualifiedName browseName = plan.browseName();

    var methodNode =
        new UaMethodNode(
            fragment.getNodeContext(),
            methodNodeId,
            browseName,
            LocalizedText.english(browseName.name()),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            true,
            true);

    fragment.getNodeManager().addNode(methodNode);

    methodNode.addReference(
        new Reference(
            methodNodeId,
            NodeIds.HasComponent,
            plan.objectNode().getNodeId().expanded(),
            Reference.Direction.INVERSE));

    bindHandler(methodNode, plan.handlerFactory().apply(methodNode));

    materializedMethodNodes.add(methodNode);
  }

  /**
   * Add a {@code bindMethod} step to {@code builder}: bind the handler the factory produces and
   * record the bound Node in {@code boundMethodNodes} for unbinding later.
   */
  private void bindMethodAt(
      InstantiationRequest.Builder<AliasNameCategoryTypeNode> builder,
      BrowsePath path,
      List<UaMethodNode> boundMethodNodes,
      Function<UaMethodNode, AbstractMethodInvocationHandler> handlerFactory) {

    builder.bindMethod(
        path,
        methodNode -> {
          bindHandler(methodNode, handlerFactory.apply(methodNode));
          boundMethodNodes.add(methodNode);
        });
  }

  /**
   * Bind {@code handler} to {@code methodNode}, set the argument Properties from the handler's
   * definitions, and restore the executable flags.
   */
  private static void bindHandler(
      UaMethodNode methodNode, AbstractMethodInvocationHandler handler) {

    methodNode.bindInvocationHandler(handler);

    methodNode.setExecutable(true);
    methodNode.setUserExecutable(true);
  }

  /**
   * Reset {@code methodNode} to its unbound state: no handler, not executable.
   *
   * <p>Deliberately not the full inverse of {@link #bindHandler}: argument Properties the bind
   * published stay in place, still correctly describing the now non-executable Method.
   */
  private static void unbindHandler(UaMethodNode methodNode) {
    methodNode.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
    methodNode.setExecutable(false);
    methodNode.setUserExecutable(false);
  }

  /**
   * Resolve the record of a managed or standard category, verifying the category Node exists.
   *
   * <p>Standard categories are usable without registration; their record hosts alias Nodes in the
   * manager's own fragment and uses the default alias NodeId factory.
   */
  private CategoryRecord resolveCategoryRecord(NodeId categoryId) throws UaException {
    CategoryRecord record = categories.get(categoryId);

    if (record == null && !STANDARD_CATEGORY_IDS.contains(categoryId)) {
      throw new UaException(
          StatusCodes.Bad_NodeIdUnknown, "category not managed: " + categoryId.toParseableString());
    }

    if (server.getAddressSpaceManager().getManagedNode(categoryId).isEmpty()) {
      throw new UaException(
          StatusCodes.Bad_NodeIdUnknown, "category not found: " + categoryId.toParseableString());
    }

    if (record != null) {
      return record;
    }

    return new CategoryRecord(
        defaultAliasNodeIdFactory(categoryId), fragment.getNodeManager(), List.of(), null);
  }

  /**
   * The alias NodeId factory used for standard and adopted categories, which have no
   * application-supplied factory: {@code "<category NodeId>/Alias/<alias name>"} in the configured
   * Node namespace.
   */
  private Function<String, NodeId> defaultAliasNodeIdFactory(NodeId categoryId) {
    return aliasName ->
        new NodeId(
            config.getNodeNamespaceIndex(), categoryId.toParseableString() + "/Alias/" + aliasName);
  }

  /**
   * The target constraint the organizing category imposes: Variable NodeClass under {@code
   * TagVariables}, {@code PublishedDataSetType} instances under {@code Topics}, none elsewhere.
   *
   * <p>A constraint applies when {@code categoryId} <em>is</em> the standard Object or its ancestor
   * {@code Organizes} chain (through {@code AliasNameCategoryType} instances) reaches it. A
   * category reachable from both — a degenerate multi-parent arrangement — gets both constraints,
   * which no target can satisfy.
   */
  private TargetConstraint getTargetConstraint(NodeId categoryId) {
    boolean tagVariable = NodeIds.TagVariables.equals(categoryId);
    boolean topic = NodeIds.Topics.equals(categoryId);

    if (!tagVariable && !topic) {
      List<NodeId> ancestors = versionManager.getAncestorCategories(categoryId);
      tagVariable = ancestors.contains(NodeIds.TagVariables);
      topic = ancestors.contains(NodeIds.Topics);
    }

    return new TargetConstraint(tagVariable, topic);
  }

  /**
   * Validate an {@link AliasTarget} and resolve its NodeId: the ReferenceType must be {@code
   * AliasFor} or a subtype, the target must be local and resolvable against the namespace table,
   * the target Node must exist, and the organizing category's NodeClass/type-definition constraint
   * (if any) must hold.
   */
  private ResolvedTarget resolveTarget(AliasTarget target, TargetConstraint constraint)
      throws UaException {

    if (!aliasTypes.isAliasForOrSubtype(target.referenceTypeId())) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "ReferenceType is not AliasFor or a subtype: "
              + target.referenceTypeId().toParseableString());
    }

    if (!target.isLocal() || !target.nodeId().isLocal()) {
      throw new UaException(
          StatusCodes.Bad_NotSupported,
          "remote targets are not supported: " + target.nodeId().toParseableString());
    }

    NodeId targetNodeId =
        target
            .nodeId()
            .toNodeId(server.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_NodeIdUnknown,
                        "target not resolvable: " + target.nodeId().toParseableString()));

    UaNode targetNode =
        server
            .getAddressSpaceManager()
            .getManagedNode(targetNodeId)
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_NodeIdUnknown,
                        "target not found: " + targetNodeId.toParseableString()));

    if (constraint.requireVariable() && targetNode.getNodeClass() != NodeClass.Variable) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "aliases organized under TagVariables must target Variable Nodes; %s has NodeClass %s"
              .formatted(targetNodeId.toParseableString(), targetNode.getNodeClass()));
    }

    if (constraint.requirePublishedDataSet()) {
      NodeId typeDefinitionId = aliasTypes.getTypeDefinitionId(targetNodeId);

      boolean isPublishedDataSet =
          typeDefinitionId != null
              && (NodeIds.PublishedDataSetType.equals(typeDefinitionId)
                  || server
                      .getObjectTypeTree()
                      .isSubtypeOf(typeDefinitionId, NodeIds.PublishedDataSetType));

      if (!isPublishedDataSet) {
        throw new UaException(
            StatusCodes.Bad_InvalidArgument,
            "aliases organized under Topics must target PublishedDataSetType instances: "
                + targetNodeId.toParseableString());
      }
    }

    return new ResolvedTarget(targetNodeId, target.referenceTypeId());
  }

  /**
   * Find the alias Object named {@code aliasName} directly organized by {@code categoryId},
   * matching on BrowseName text alone (namespace index ignored). When several same-named alias
   * Objects exist, the one with the smallest parseable NodeId is chosen, deterministically.
   */
  private @Nullable NodeId findAliasInCategory(NodeId categoryId, String aliasName) {
    return findAliasesInCategory(categoryId, aliasName).stream()
        .min(Comparator.comparing(NodeId::toParseableString))
        .orElse(null);
  }

  /** Find alias Objects and child categories directly organized by {@code categoryId}. */
  private List<NodeId> findOrganizedMembers(NodeId categoryId) {
    List<Reference> organizes =
        server
            .getAddressSpaceManager()
            .getManagedReferences(categoryId, Reference.ORGANIZES_PREDICATE);

    var found = new ArrayList<NodeId>();
    for (Reference reference : organizes) {
      reference
          .getTargetNodeId()
          .toNodeId(server.getNamespaceTable())
          .filter(
              id ->
                  aliasTypes.isAliasNameInstance(id) || aliasTypes.isAliasNameCategoryInstance(id))
          .ifPresent(found::add);
    }
    return found;
  }

  /**
   * Find every alias Object named {@code aliasName} directly organized by {@code categoryId},
   * matching on BrowseName text alone (namespace index ignored).
   */
  private List<NodeId> findAliasesInCategory(NodeId categoryId, String aliasName) {
    List<Reference> organizes =
        server
            .getAddressSpaceManager()
            .getManagedReferences(categoryId, Reference.ORGANIZES_PREDICATE);

    var found = new ArrayList<NodeId>();
    for (Reference reference : organizes) {
      NodeId organizedId =
          reference.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElse(null);
      if (organizedId == null) {
        continue;
      }

      // Name before type: the name comparison is an in-memory check, while the type test costs
      // another Reference query plus a type-tree walk, and most organized Nodes won't match.
      UaNode node = server.getAddressSpaceManager().getManagedNode(organizedId).orElse(null);
      if (node == null || !aliasName.equals(node.getBrowseName().name())) {
        continue;
      }

      if (aliasTypes.isAliasNameInstance(organizedId)) {
        found.add(organizedId);
      }
    }
    return found;
  }

  /**
   * The (target, ReferenceType) pairs of the alias's existing forward References, for batch
   * duplicate checks against {@link ResolvedTarget}s.
   */
  private Set<ResolvedTarget> collectExistingAssociations(NodeId aliasNodeId) {
    var associations = new HashSet<ResolvedTarget>();

    List<Reference> references =
        server.getAddressSpaceManager().getManagedReferences(aliasNodeId, Reference::isForward);

    for (Reference reference : references) {
      reference
          .getTargetNodeId()
          .toNodeId(server.getNamespaceTable())
          .ifPresent(
              targetId ->
                  associations.add(new ResolvedTarget(targetId, reference.getReferenceTypeId())));
    }

    return associations;
  }

  /**
   * The alias's forward References to {@code targetNodeId} whose ReferenceType passes {@code
   * refTypeOk}, resolving each Reference's target against the namespace table.
   */
  private List<Reference> findTargetReferences(
      NodeId aliasNodeId, NodeId targetNodeId, Predicate<NodeId> refTypeOk) {

    return server
        .getAddressSpaceManager()
        .getManagedReferences(
            aliasNodeId,
            reference ->
                reference.isForward()
                    && refTypeOk.test(reference.getReferenceTypeId())
                    && reference
                        .getTargetNodeId()
                        .toNodeId(server.getNamespaceTable())
                        .map(targetNodeId::equals)
                        .orElse(false));
  }

  /**
   * Delete the alias Object if its last target Reference is gone — an alias without a target
   * violates the Part 17 model, so it is deleted from every category that organizes it. The
   * organizing categories' new versions are prepared (persisted) <em>before</em> deletion, so a
   * failed save aborts the deletion and a deletion that throws partway through still gets its
   * {@code LastChange} bumps published.
   *
   * @return {@code true} if the alias was targetless and deleted.
   */
  private boolean deleteIfTargetless(UaNode aliasNode) throws UaException {
    NodeId aliasNodeId = aliasNode.getNodeId();

    if (!collectRemainingTargets(aliasNodeId).isEmpty()) {
      return false;
    }

    versionManager.prepare(getOrganizingCategories(aliasNodeId));

    aliasNode.delete();

    return true;
  }

  /** The forward {@code AliasFor}-or-subtype References of an alias Node. */
  private List<Reference> collectRemainingTargets(NodeId aliasNodeId) {
    return server
        .getAddressSpaceManager()
        .getManagedReferences(
            aliasNodeId,
            reference ->
                reference.isForward()
                    && aliasTypes.isAliasForOrSubtype(reference.getReferenceTypeId()));
  }

  /**
   * The categories that organize {@code aliasNodeId}, resolved via inverse Organizes References
   * aggregated across every registered NodeManager.
   *
   * <p>Limitation: a category-side-only linkage — a forward {@code Organizes} Reference recorded
   * without its inverse, possible when References are added out-of-band, e.g. by a NodeSet loader
   * that does not write inverses — is invisible from the alias side and is missed here, so such a
   * category is not treated as organizing the alias.
   */
  private List<NodeId> getOrganizingCategories(NodeId aliasNodeId) {
    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(aliasNodeId, Reference.ORGANIZED_BY_PREDICATE);

    var categoryIds = new ArrayList<NodeId>();
    for (Reference reference : references) {
      reference
          .getTargetNodeId()
          .toNodeId(server.getNamespaceTable())
          .filter(aliasTypes::isAliasNameCategoryInstance)
          .ifPresent(categoryIds::add);
    }
    return categoryIds;
  }

  /** Remove both directions of a member's Organizes linkage from every registered manager. */
  private void removeFromCategory(UaNode member, NodeId categoryId) {
    server
        .getAddressSpaceManager()
        .removeManagedReferences(
            new Reference(
                member.getNodeId(),
                NodeIds.Organizes,
                categoryId.expanded(),
                Reference.Direction.INVERSE));
  }

  /**
   * Find a Method component of {@code nodeId} by BrowseName.
   *
   * <p>Reference-based rather than typed-node-based so that NodeSet-loaded plain Object Nodes
   * qualify.
   */
  private @Nullable UaMethodNode findComponentMethodNode(NodeId nodeId, QualifiedName browseName) {
    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(nodeId, Reference.HAS_COMPONENT_PREDICATE);

    for (Reference reference : references) {
      UaNode node =
          reference
              .getTargetNodeId()
              .toNodeId(server.getNamespaceTable())
              .flatMap(id -> server.getAddressSpaceManager().getManagedNode(id))
              .orElse(null);

      if (node instanceof UaMethodNode methodNode && browseName.equals(node.getBrowseName())) {
        return methodNode;
      }
    }
    return null;
  }

  /**
   * Bind a handler on the optional {@code methodName} component of an adopted category, if that
   * Method Node exists and is still unbound; a missing or already-bound Node is left untouched.
   *
   * @return {@code true} if a handler was bound.
   */
  private boolean bindOptionalMethodIfUnbound(
      NodeId categoryId,
      String methodName,
      Function<UaMethodNode, AbstractMethodInvocationHandler> handlerFactory,
      List<UaMethodNode> boundMethodNodes) {

    UaMethodNode methodNode = findComponentMethodNode(categoryId, new QualifiedName(0, methodName));

    if (methodNode == null) {
      return false;
    }

    if (!(methodNode.getInvocationHandler()
        instanceof MethodInvocationHandler.NotImplementedHandler)) {
      return false;
    }

    bindHandler(methodNode, handlerFactory.apply(methodNode));
    boundMethodNodes.add(methodNode);

    return true;
  }

  /** A validated, locally resolved alias target. */
  private record ResolvedTarget(NodeId nodeId, NodeId referenceTypeId) {}

  /**
   * The NodeClass/type-definition constraint an organizing category imposes on alias targets; see
   * {@link #getTargetConstraint}.
   */
  private record TargetConstraint(boolean requireVariable, boolean requirePublishedDataSet) {}

  /** An Optional Method Node to materialize on a standard Object, with its handler recipe. */
  private record MethodPlan(
      UaNode objectNode,
      NodeId methodNodeId,
      QualifiedName browseName,
      Function<UaMethodNode, AbstractMethodInvocationHandler> handlerFactory) {}

  /**
   * Everything the manager tracks per managed category: where its alias Nodes live and how their
   * NodeIds are allocated, the Method Nodes whose handlers must be reset, and — for categories the
   * manager created — the instantiation journal used to delete them again.
   */
  private record CategoryRecord(
      Function<String, NodeId> aliasNodeIdFactory,
      NodeManager<UaNode> nodeManager,
      List<UaMethodNode> boundMethodNodes,
      @Nullable InstantiationResult<AliasNameCategoryTypeNode> instantiationResult) {}

  /** A minimal {@link UaNodeContext} binding manually created Nodes to their NodeManager. */
  private record ManagedNodeContext(OpcUaServer server, NodeManager<UaNode> nodeManager)
      implements UaNodeContext {

    @Override
    public OpcUaServer getServer() {
      return server;
    }

    @Override
    public NodeManager<UaNode> getNodeManager() {
      return nodeManager;
    }
  }

  /**
   * The manager's AddressSpace fragment: hosts the Nodes the manager creates outside application
   * namespaces (materialized Method Nodes, alias Nodes in standard and adopted categories) and
   * claims exactly the Nodes its NodeManager contains, so service operations route to them
   * regardless of NodeId namespace.
   *
   * <p>Startup registers the fragment and its NodeManager with the server's AddressSpaceManager;
   * shutdown unregisters both. A SubscriptionModel provides sampling for MonitoredItems created on
   * hosted Nodes.
   *
   * <p>The fragment registers itself <em>first</em> in the composite: service routing picks the
   * first registered AddressSpace whose filter matches, and hosted Nodes have NodeIds allocated in
   * an application namespace, so an application Namespace registered earlier (whose filter matches
   * its entire namespace index) would otherwise shadow them. Because the filter matches exactly the
   * Nodes the fragment contains, registering first diverts no other traffic.
   */
  private static final class AliasFragment extends ManagedAddressSpaceFragmentWithLifecycle {

    private final AddressSpaceFilter filter =
        SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);

    private final SubscriptionModel subscriptionModel;

    AliasFragment(OpcUaServer server) {
      super(server);

      subscriptionModel = new SubscriptionModel(server, this);

      getLifecycleManager().addLifecycle(subscriptionModel);
    }

    @Override
    public AddressSpaceFilter getFilter() {
      return filter;
    }

    @Override
    protected void registerWithComposite(AddressSpaceComposite composite) {
      composite.registerFirst(this);
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
      subscriptionModel.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
      subscriptionModel.onMonitoringModeChanged(monitoredItems);
    }
  }
}

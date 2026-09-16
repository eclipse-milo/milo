/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A lazy-loading {@link DataTypeTree} that resolves types on demand by browsing inverse HasSubtype
 * references.
 *
 * <p>Unlike {@link DataTypeTreeBuilder} which eagerly builds the entire tree by forward browsing
 * from {@link NodeIds#BaseDataType}, this implementation starts with only the root type and
 * resolves additional types lazily when they are queried.
 *
 * <p>This approach is useful when servers don't support recursive forward browsing of the DataType
 * hierarchy or when only a subset of types is needed.
 *
 * <h2>Thread Safety</h2>
 *
 * <p>This implementation is thread-safe. All read operations acquire a read lock and all
 * modifications acquire a write lock. However, note that type resolution (which includes network
 * I/O to browse and read from the server) is performed while holding the write lock. This means
 * that concurrent threads attempting to resolve different types will be serialized. Once a type is
 * resolved, later lookups only require the read lock and can proceed concurrently.
 *
 * <p>Because the underlying {@link Tree} is mutated as types are resolved, {@link #getRoot()} and
 * {@link #getTreeNode(NodeId)} return snapshot copies rather than live nodes.
 *
 * <h2>Resolution Behavior</h2>
 *
 * <p>Resolution errors (e.g., network failures, non-existent types) do not cause exceptions to be
 * thrown from query methods like {@link #getDataType(NodeId)}. Instead, {@code null} is returned.
 * Once a resolution attempt has failed, it will not be retried unless {@link
 * #clearFailedResolutions()} is called.
 *
 * <h2>Namespace Table</h2>
 *
 * <p>This tree caches a copy of the server's {@link NamespaceTable} for converting {@link
 * org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId}s during browse operations. If the
 * server's namespace array changes (e.g., after a reconnection or dynamic namespace registration),
 * call {@link #invalidateNamespaceTable()} or {@link #refreshNamespaceTable()} to update the cached
 * copy.
 */
public class LazyClientDataTypeTree extends DataTypeTree {

  private static final Logger LOGGER = LoggerFactory.getLogger(LazyClientDataTypeTree.class);

  /**
   * Upper bound on the number of inverse HasSubtype hops followed while resolving a type, guarding
   * against non-compliant servers with cyclic or unbounded inverse subtype references.
   */
  private static final int MAX_RESOLUTION_DEPTH = 256;

  private final OpcUaClient client;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  // All access is guarded by `lock`: reads under the read lock, mutations under the write lock.
  private final Set<NodeId> attemptedResolution = new HashSet<>();

  private volatile NamespaceTable namespaceTable;

  /**
   * Create a new {@link LazyClientDataTypeTree} with only {@link NodeIds#BaseDataType} initially
   * loaded.
   *
   * @param client a connected {@link OpcUaClient}.
   */
  public LazyClientDataTypeTree(OpcUaClient client) {
    this(client, createRootTree());
  }

  /**
   * Create a new {@link LazyClientDataTypeTree} with a pre-seeded tree.
   *
   * <p>This constructor supports preloading known types (e.g., namespace 0 types from a code
   * generator) to reduce the number of lazy resolutions needed.
   *
   * @param client a connected {@link OpcUaClient}.
   * @param preSeededTree a pre-built tree containing known types.
   */
  public LazyClientDataTypeTree(OpcUaClient client, Tree<DataType> preSeededTree) {
    super(preSeededTree);
    this.client = client;
  }

  private static Tree<DataType> createRootTree() {
    return new Tree<>(
        null,
        new ClientDataType(
            QualifiedName.parse("0:BaseDataType"),
            NodeIds.BaseDataType,
            null,
            null,
            null,
            null,
            true));
  }

  // ===== Namespace Table Management =====

  private NamespaceTable getNamespaceTable() throws UaException {
    NamespaceTable ns = namespaceTable;
    if (ns == null) {
      synchronized (this) {
        ns = namespaceTable;
        if (ns == null) {
          ns = client.readNamespaceTable();
          namespaceTable = ns;
        }
      }
    }
    return ns;
  }

  /**
   * Invalidate the cached {@link NamespaceTable}, causing it to be re-read on next use.
   *
   * <p>Call this method when the server's namespace array may have changed (e.g., after a
   * reconnection or when namespaces are dynamically registered on the server).
   *
   * @see #refreshNamespaceTable()
   */
  public void invalidateNamespaceTable() {
    namespaceTable = null;
  }

  /**
   * Refresh the cached {@link NamespaceTable} immediately by reading it from the server.
   *
   * <p>Unlike {@link #invalidateNamespaceTable()}, this method reads the namespace table
   * immediately rather than deferring until the next type resolution.
   *
   * @throws UaException if reading the namespace table fails.
   * @see #invalidateNamespaceTable()
   */
  public void refreshNamespaceTable() throws UaException {
    namespaceTable = client.readNamespaceTable();
  }

  // ===== Resolution State =====

  /**
   * Check if a type has been resolved/loaded without triggering resolution.
   *
   * @param typeId the {@link NodeId} to check.
   * @return {@code true} if the type is already loaded in the tree.
   */
  public boolean isResolved(NodeId typeId) {
    lock.readLock().lock();
    try {
      return types.containsKey(typeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Clear failed resolution attempts, allowing retry.
   *
   * <p>When a type resolution fails (e.g., due to network errors or non-existent types), the
   * failure is recorded to avoid repeated failed attempts. This method clears those records,
   * allowing later queries for those types to attempt resolution again.
   *
   * <p>This is useful after transient network issues have been resolved or when the server's type
   * system may have changed.
   */
  public void clearFailedResolutions() {
    lock.writeLock().lock();
    try {
      attemptedResolution.retainAll(types.keySet());
    } finally {
      lock.writeLock().unlock();
    }
  }

  // ===== Core Resolution Logic =====

  private void ensureResolved(NodeId dataTypeId) {
    // Fast path under read lock
    lock.readLock().lock();
    try {
      if (types.containsKey(dataTypeId) || attemptedResolution.contains(dataTypeId)) {
        return;
      }
    } finally {
      lock.readLock().unlock();
    }

    // Slow path under write lock
    lock.writeLock().lock();
    try {
      // Re-check under write lock
      if (types.containsKey(dataTypeId) || attemptedResolution.contains(dataTypeId)) {
        return;
      }

      attemptedResolution.add(dataTypeId);

      try {
        resolvePath(dataTypeId);
      } catch (UaException e) {
        LOGGER.debug("Failed to resolve DataType {}: {}", dataTypeId, e.getMessage());
      } catch (RuntimeException e) {
        // Query methods are documented not to throw on resolution failure; an unexpected
        // RuntimeException here usually indicates a non-compliant server response.
        LOGGER.warn("Unexpected error resolving DataType {}", dataTypeId, e);
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  private void resolvePath(NodeId dataTypeId) throws UaException {
    // Pin resolution to the session it started on so results assembled across a session change
    // (e.g. a reconnection mid-resolution) are never cached.
    NodeId sessionId = client.getSession().getSessionId();

    NamespaceTable nsTable = getNamespaceTable();
    OperationLimits limits = client.getOperationLimits();

    List<NodeId> pathToResolve = browseInverseUntilKnown(dataTypeId, types.keySet(), nsTable);

    if (pathToResolve.size() < 2) {
      LOGGER.debug("Could not resolve path to known ancestor for DataType {}", dataTypeId);
      return;
    }

    // pathToResolve = [target, parent, ..., knownAncestor]
    List<NodeId> nodesToAdd = pathToResolve.subList(0, pathToResolve.size() - 1);
    NodeId knownAncestorId = pathToResolve.get(pathToResolve.size() - 1);

    List<ClientDataType> dataTypes = fetchDataTypeInfoBatch(nodesToAdd, nsTable, limits);

    ClientBrowseUtils.checkSessionUnchanged(client, sessionId);

    // Add from ancestor toward target (reverse order)
    Tree<DataType> parentTree = types.get(knownAncestorId);

    for (int i = nodesToAdd.size() - 1; i >= 0; i--) {
      ClientDataType dataType = dataTypes.get(i);

      if (dataType == null) {
        // Attribute reads failed for this node; stop here rather than caching an incomplete
        // type or attaching its descendants to the wrong parent.
        LOGGER.debug("Attribute reads failed for DataType {}; path not cached", nodesToAdd.get(i));
        break;
      }

      Tree<DataType> childTree = parentTree.addChild(dataType);
      types.put(dataType.getNodeId(), childTree);
      parentTree = childTree;

      LOGGER.debug("Resolved DataType: {}", dataType.getBrowseName().toParseableString());
    }
  }

  private List<@Nullable ClientDataType> fetchDataTypeInfoBatch(
      List<NodeId> nodeIds, NamespaceTable nsTable, OperationLimits limits) throws UaException {

    // Read attributes: BrowseName, IsAbstract, DataTypeDefinition
    var readValueIds = new ArrayList<ReadValueId>();
    for (NodeId nodeId : nodeIds) {
      readValueIds.add(
          new ReadValueId(nodeId, AttributeId.BrowseName.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(nodeId, AttributeId.IsAbstract.uid(), null, QualifiedName.NULL_VALUE));
      readValueIds.add(
          new ReadValueId(
              nodeId, AttributeId.DataTypeDefinition.uid(), null, QualifiedName.NULL_VALUE));
    }

    List<DataValue> values =
        ClientBrowseUtils.readWithOperationLimits(client, readValueIds, limits);

    // Browse encodings
    List<List<ReferenceDescription>> encodingRefs =
        ClientBrowseUtils.browseEncodings(client, nodeIds, limits);

    var result = new ArrayList<@Nullable ClientDataType>();

    for (int i = 0; i < nodeIds.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      int valueOffset = i * 3;

      QualifiedName browseName = extractBrowseName(values.get(valueOffset));
      Boolean isAbstract = extractIsAbstract(values.get(valueOffset + 1));
      DataTypeDefinition definition = extractDataTypeDefinition(values.get(valueOffset + 2));

      if (browseName == null) {
        // BrowseName is a mandatory attribute; a bad read means the node is unavailable and the
        // type would be cached with meaningless values.
        result.add(null);
        continue;
      }

      ClientBrowseUtils.EncodingIds encodingIds =
          ClientBrowseUtils.extractEncodingIds(encodingRefs.get(i), nsTable);

      result.add(
          new ClientDataType(
              browseName,
              nodeId,
              encodingIds.binaryEncodingId(),
              encodingIds.xmlEncodingId(),
              encodingIds.jsonEncodingId(),
              definition,
              isAbstract));
    }

    return result;
  }

  /**
   * Browse inverse HasSubtype references from {@code startId} until reaching a node that exists in
   * {@code knownTypeIds}.
   *
   * @param startId the NodeId to start browsing from.
   * @param knownTypeIds the set of NodeIds that are already known/loaded.
   * @param namespaceTable the namespace table for converting ExpandedNodeIds.
   * @return path from startId to known ancestor (inclusive), or empty list if unreachable.
   */
  private List<NodeId> browseInverseUntilKnown(
      NodeId startId, Set<NodeId> knownTypeIds, NamespaceTable namespaceTable) {

    var visited = new HashSet<NodeId>();
    List<NodeId> path = new ArrayList<>();
    NodeId current = startId;

    while (current != null && !knownTypeIds.contains(current)) {
      if (!visited.add(current) || visited.size() > MAX_RESOLUTION_DEPTH) {
        LOGGER.warn(
            "Inverse HasSubtype references from {} are cyclic or exceed depth {}",
            startId,
            MAX_RESOLUTION_DEPTH);
        return List.of();
      }
      path.add(current);
      current = browseInverseParent(current, namespaceTable);
    }

    if (current != null && knownTypeIds.contains(current)) {
      path.add(current);
      return path;
    }

    return List.of();
  }

  /**
   * Browse the inverse HasSubtype reference from {@code nodeId} to find its parent type.
   *
   * @param nodeId the NodeId to browse from.
   * @param namespaceTable the namespace table for converting ExpandedNodeIds.
   * @return the parent NodeId, or null if not found.
   */
  private @Nullable NodeId browseInverseParent(NodeId nodeId, NamespaceTable namespaceTable) {
    try {
      BrowseDescription bd =
          new BrowseDescription(
              nodeId,
              BrowseDirection.Inverse,
              NodeIds.HasSubtype,
              false,
              uint(NodeClass.DataType.getValue()),
              uint(BrowseResultMask.All.getValue()));

      BrowseResult result = client.browse(bd);

      if (result.getStatusCode().isGood()
          && result.getReferences() != null
          && result.getReferences().length > 0) {

        return result.getReferences()[0].getNodeId().toNodeId(namespaceTable).orElse(null);
      }
    } catch (UaException e) {
      LOGGER.debug("Failed to browse inverse parent for {}: {}", nodeId, e.getMessage());
    }

    return null;
  }

  private static @Nullable QualifiedName extractBrowseName(DataValue value) {
    if (value.statusCode().isGood() && value.value().value() instanceof QualifiedName qn) {
      return qn;
    }
    return null;
  }

  private static Boolean extractIsAbstract(DataValue value) {
    if (value.statusCode().isGood() && value.value().value() instanceof Boolean b) {
      return b;
    }
    return false;
  }

  private @Nullable DataTypeDefinition extractDataTypeDefinition(DataValue value) {
    if (value.statusCode().isGood()) {
      Object o = value.value().value();
      if (o instanceof ExtensionObject xo) {
        try {
          Object decoded = xo.decode(client.getStaticEncodingContext());
          if (decoded instanceof DataTypeDefinition dtd) {
            return dtd;
          }
        } catch (Exception e) {
          LOGGER.debug("Error decoding DataTypeDefinition: {}", e.getMessage());
        }
      } else if (o instanceof DataTypeDefinition dtd) {
        return dtd;
      }
    }
    return null;
  }

  // ===== Overridden Methods =====

  /**
   * Ensure {@code dataTypeId} is resolved, then evaluate {@code query} under the read lock.
   *
   * @param dataTypeId the {@link NodeId} of the DataType the query is about.
   * @param query the query to evaluate.
   * @return the result of {@code query}.
   */
  private <T> T resolvedQuery(NodeId dataTypeId, Supplier<T> query) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return query.get();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Get a snapshot of the root of the underlying {@link Tree} structure.
   *
   * <p>Because this tree is lazily populated, returning the live tree would expose callers to
   * potential concurrent modification during traversal. Instead, this method returns a deep copy
   * (snapshot) of the current tree state, taken under a read lock.
   *
   * <p>The snapshot reflects the types that have been resolved at the time of the call. Types
   * resolved after the snapshot is taken will not appear in the returned tree.
   *
   * <p>Note: The {@link Tree} structure is copied, but the contained {@link DataType} instances are
   * shared references. This is safe because {@link DataType} instances are effectively immutable.
   *
   * @return a snapshot copy of the root node of the underlying {@link Tree} structure.
   */
  @Override
  public Tree<DataType> getRoot() {
    lock.readLock().lock();
    try {
      return tree.map(dataType -> dataType);
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Get a snapshot of the underlying {@link Tree} node for the DataType identified by {@code
   * dataTypeId}.
   *
   * <p>Like {@link #getRoot()}, this method returns a node from a deep copy (snapshot) of the
   * current tree state rather than the live, lazily-mutated tree. The snapshot is taken from the
   * root, so the returned node's parent chain is intact.
   *
   * @param dataTypeId the {@link NodeId} of a DataType Node.
   * @return a snapshot of the {@link Tree} node for the DataType identified by {@code dataTypeId},
   *     or {@code null} if it is not present in the tree.
   */
  @Override
  public @Nullable Tree<DataType> getTreeNode(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      if (super.getTreeNode(dataTypeId) == null) {
        return null;
      }

      Tree<DataType> snapshot = tree.map(dataType -> dataType);

      var treeNode = new AtomicReference<Tree<DataType>>();
      snapshot.traverseNodes(
          node -> {
            if (node.getValue().getNodeId().equals(dataTypeId)) {
              treeNode.set(node);
            }
          });
      return treeNode.get();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean containsType(NodeId typeId) {
    return resolvedQuery(typeId, () -> super.containsType(typeId));
  }

  @Override
  public @Nullable DataType getType(NodeId nodeId) {
    return resolvedQuery(nodeId, () -> super.getType(nodeId));
  }

  @Override
  public Class<?> getBackingClass(NodeId dataTypeId) {
    if (hasStaticBackingClass(dataTypeId)) {
      // The superclass answers without consulting the tree; skip resolution and locking.
      return super.getBackingClass(dataTypeId);
    }
    return resolvedQuery(dataTypeId, () -> super.getBackingClass(dataTypeId));
  }

  @Override
  public OpcUaDataType getBuiltinType(NodeId dataTypeId) {
    if (OpcUaDataType.isBuiltin(dataTypeId)) {
      // The superclass answers without consulting the tree; skip resolution and locking.
      return super.getBuiltinType(dataTypeId);
    }
    return resolvedQuery(dataTypeId, () -> super.getBuiltinType(dataTypeId));
  }

  @Override
  public @Nullable DataType getDataType(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.getDataType(dataTypeId));
  }

  @Override
  public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.getBinaryEncodingId(dataTypeId));
  }

  @Override
  public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.getXmlEncodingId(dataTypeId));
  }

  @Override
  public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.getJsonEncodingId(dataTypeId));
  }

  @Override
  public @Nullable DataTypeDefinition getDataTypeDefinition(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.getDataTypeDefinition(dataTypeId));
  }

  @Override
  public boolean isAssignable(NodeId dataTypeId, Class<?> clazz) {
    // Resolution and locking are handled by the getBackingClass override.
    return super.isAssignable(dataTypeId, clazz);
  }

  @Override
  public boolean isEnumType(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.isEnumType(dataTypeId));
  }

  @Override
  public boolean isStructType(NodeId dataTypeId) {
    return resolvedQuery(dataTypeId, () -> super.isStructType(dataTypeId));
  }

  @Override
  public boolean isSubtypeOf(NodeId typeId, NodeId superTypeId) {
    ensureResolved(typeId);
    lock.readLock().lock();
    try {
      // Walk the parent chain directly rather than delegating to the superclass, which would
      // dispatch back through the overridden (snapshotting) getTreeNode.
      Tree<DataType> node = super.getTreeNode(typeId);

      while (node != null) {
        Tree<DataType> parent = node.getParent();
        if (parent == null) {
          return false;
        }
        if (parent.getValue().getNodeId().equals(superTypeId)) {
          return true;
        }
        node = parent;
      }

      return false;
    } finally {
      lock.readLock().unlock();
    }
  }

  private static boolean hasStaticBackingClass(NodeId dataTypeId) {
    return OpcUaDataType.isBuiltin(dataTypeId)
        || NodeIds.Enumeration.equals(dataTypeId)
        || NodeIds.Number.equals(dataTypeId)
        || NodeIds.Integer.equals(dataTypeId)
        || NodeIds.UInteger.equals(dataTypeId);
  }
}

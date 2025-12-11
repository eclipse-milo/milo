/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
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

  private final OpcUaClient client;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final Set<NodeId> attemptedResolution = ConcurrentHashMap.newKeySet();

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
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  private void resolvePath(NodeId dataTypeId) throws UaException {
    NamespaceTable nsTable = getNamespaceTable();
    OperationLimits limits = client.getOperationLimits();

    List<NodeId> pathToResolve =
        LazyTypeTreeUtils.browseInverseUntilKnown(client, dataTypeId, types.keySet(), nsTable);

    if (pathToResolve.isEmpty() || pathToResolve.size() < 2) {
      LOGGER.debug("Could not resolve path to known ancestor for DataType {}", dataTypeId);
      return;
    }

    // pathToResolve = [target, parent, ..., knownAncestor]
    List<NodeId> nodesToAdd = pathToResolve.subList(0, pathToResolve.size() - 1);
    NodeId knownAncestorId = pathToResolve.get(pathToResolve.size() - 1);

    List<ClientDataType> dataTypes = fetchDataTypeInfoBatch(nodesToAdd, nsTable, limits);

    // Add from ancestor toward target (reverse order)
    Tree<DataType> parentTree = types.get(knownAncestorId);

    for (int i = nodesToAdd.size() - 1; i >= 0; i--) {
      ClientDataType dataType = dataTypes.get(i);

      if (dataType != null && parentTree != null) {
        Tree<DataType> childTree = parentTree.addChild(dataType);
        types.put(dataType.getNodeId(), childTree);
        parentTree = childTree;

        LOGGER.debug("Resolved DataType: {}", dataType.getBrowseName().toParseableString());
      }
    }
  }

  private List<ClientDataType> fetchDataTypeInfoBatch(
      List<NodeId> nodeIds, NamespaceTable nsTable, OperationLimits limits) {

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
        LazyTypeTreeUtils.readWithOperationLimits(client, readValueIds, limits);

    // Browse encodings
    List<List<ReferenceDescription>> encodingRefs = browseEncodings(nodeIds, limits);

    var result = new ArrayList<ClientDataType>();

    for (int i = 0; i < nodeIds.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      int valueOffset = i * 3;

      QualifiedName browseName = extractBrowseName(values.get(valueOffset));
      Boolean isAbstract = extractIsAbstract(values.get(valueOffset + 1));
      DataTypeDefinition definition = extractDataTypeDefinition(values.get(valueOffset + 2));

      NodeId binaryEncodingId = null;
      NodeId xmlEncodingId = null;
      NodeId jsonEncodingId = null;

      for (ReferenceDescription r : encodingRefs.get(i)) {
        // Be lenient: also match on unqualified browse name (some servers use wrong namespace)
        if (r.getBrowseName().equals(DataTypeEncoding.BINARY_ENCODING_NAME)
            || Objects.equals(r.getBrowseName().name(), "Default Binary")) {
          binaryEncodingId = r.getNodeId().toNodeId(nsTable).orElse(null);
        } else if (r.getBrowseName().equals(DataTypeEncoding.XML_ENCODING_NAME)
            || Objects.equals(r.getBrowseName().name(), "Default XML")) {
          xmlEncodingId = r.getNodeId().toNodeId(nsTable).orElse(null);
        } else if (r.getBrowseName().equals(DataTypeEncoding.JSON_ENCODING_NAME)
            || Objects.equals(r.getBrowseName().name(), "Default JSON")) {
          jsonEncodingId = r.getNodeId().toNodeId(nsTable).orElse(null);
        }
      }

      result.add(
          new ClientDataType(
              browseName,
              nodeId,
              binaryEncodingId,
              xmlEncodingId,
              jsonEncodingId,
              definition,
              isAbstract));
    }

    return result;
  }

  private List<List<ReferenceDescription>> browseEncodings(
      List<NodeId> dataTypeIds, OperationLimits limits) {

    List<BrowseDescription> browseDescriptions =
        dataTypeIds.stream()
            .map(
                dataTypeId ->
                    new BrowseDescription(
                        dataTypeId,
                        BrowseDirection.Forward,
                        NodeIds.HasEncoding,
                        false,
                        uint(NodeClass.Object.getValue()),
                        uint(BrowseResultMask.All.getValue())))
            .toList();

    return LazyTypeTreeUtils.browseWithOperationLimits(client, browseDescriptions, limits);
  }

  private static QualifiedName extractBrowseName(DataValue value) {
    if (value.statusCode().isGood() && value.value().value() instanceof QualifiedName qn) {
      return qn;
    }
    return QualifiedName.NULL_VALUE;
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

  @Override
  public boolean containsType(NodeId typeId) {
    ensureResolved(typeId);
    lock.readLock().lock();
    try {
      return super.containsType(typeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable DataType getType(NodeId nodeId) {
    ensureResolved(nodeId);
    lock.readLock().lock();
    try {
      return super.getType(nodeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Class<?> getBackingClass(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getBackingClass(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public OpcUaDataType getBuiltinType(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getBuiltinType(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable DataType getDataType(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getDataType(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getBinaryEncodingId(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getXmlEncodingId(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getJsonEncodingId(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable DataTypeDefinition getDataTypeDefinition(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getDataTypeDefinition(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isAssignable(NodeId dataTypeId, Class<?> clazz) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.isAssignable(dataTypeId, clazz);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isEnumType(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.isEnumType(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isStructType(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.isStructType(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public @Nullable Tree<DataType> getTreeNode(NodeId dataTypeId) {
    ensureResolved(dataTypeId);
    lock.readLock().lock();
    try {
      return super.getTreeNode(dataTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isSubtypeOf(NodeId typeId, NodeId superTypeId) {
    ensureResolved(typeId);
    lock.readLock().lock();
    try {
      return super.isSubtypeOf(typeId, superTypeId);
    } finally {
      lock.readLock().unlock();
    }
  }
}

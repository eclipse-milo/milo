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
import java.util.concurrent.atomic.AtomicLong;
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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
 * #clearFailedResolutions()} is called. If the calling thread is interrupted before or during
 * resolution, the interrupt status is preserved and the failed attempt is not cached, allowing
 * subsequent lookups to retry.
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
  // Contains only ids whose resolution attempt completed without resolving the type.
  private final Set<NodeId> attemptedResolution = new HashSet<>();

  // Incremented under the write lock on clearFailedResolutions() so an in-flight failure cannot
  // re-insert a NodeId into attemptedResolution after a reentrant clearFailedResolutions().
  private final AtomicLong failedResolutionEpoch = new AtomicLong();

  // Guarded by `lock`: tracks ids currently being resolved on the write-lock holder thread so a
  // reentrant lookup during resolution does not recurse or publish duplicate tree nodes.
  private final Set<NodeId> inProgressResolution = new HashSet<>();

  // Scoped to virtual lookupDataType() / openRetryScope() calls on the current thread so subclass
  // overrides, DataTypeDefinition decoders, and dependent lookups (including through
  // LazyClientDataTypeManager) propagate retryable misses and aborts cleanly to callers.
  private static final ThreadLocal<@Nullable LookupCapture> ACTIVE_LOOKUP = new ThreadLocal<>();

  // Tracks nested LazyClientDataTypeTree read- and write-lock holds on the current thread so
  // LazyClientDataTypeManager avoids blocking on its own write lock while holding any tree lock,
  // preventing lock-inversion deadlocks with concurrent manager -> tree lookups.
  private static final ThreadLocal<@Nullable Integer> ACTIVE_LOCK_DEPTH = new ThreadLocal<>();

  private static final class LookupCapture {
    boolean retryable;
    boolean aborted;
  }

  private enum ResolutionOutcome {
    COMPLETED,
    REENTRANT,
    ABORTED
  }

  private static final class RetryableResolutionException extends UaException {
    final boolean aborted;

    RetryableResolutionException(String message, boolean aborted) {
      super(StatusCodes.Bad_UnexpectedError, message);
      this.aborted = aborted;
    }

    RetryableResolutionException(String message, Throwable cause, boolean aborted) {
      super(StatusCodes.Bad_UnexpectedError, message, cause);
      this.aborted = aborted;
    }
  }

  static final class RetryScope implements AutoCloseable {
    private final @Nullable LookupCapture previous;
    private final LookupCapture current;

    private RetryScope(@Nullable LookupCapture previous, LookupCapture current) {
      this.previous = previous;
      this.current = current;
    }

    boolean isRetryable() {
      return current.retryable;
    }

    boolean isAborted() {
      return current.aborted;
    }

    void close(boolean propagate) {
      if (previous != null) {
        if (propagate) {
          if (current.retryable) {
            previous.retryable = true;
          }
          if (current.aborted) {
            previous.aborted = true;
          }
        }
        ACTIVE_LOOKUP.set(previous);
      } else {
        ACTIVE_LOOKUP.remove();
      }
    }

    @Override
    public void close() {
      close(true);
    }
  }

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
    lockRead();
    try {
      return types.containsKey(typeId);
    } finally {
      unlockRead();
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
    lockWrite();
    try {
      attemptedResolution.clear();
      failedResolutionEpoch.incrementAndGet();
    } finally {
      unlockWrite();
    }
  }

  // ===== Core Resolution Logic =====

  record LookupResult(@Nullable DataType dataType, boolean retryable, boolean aborted) {}

  /**
   * Open a thread-scoped capture that records whether any tree or paired manager lookup skipped or
   * aborted resolution with a retryable outcome while the scope is active.
   */
  static RetryScope openRetryScope() {
    LookupCapture previous = ACTIVE_LOOKUP.get();
    var current = new LookupCapture();
    ACTIVE_LOOKUP.set(current);
    return new RetryScope(previous, current);
  }

  static void markCurrentRetryScopeRetryable() {
    LookupCapture capture = ACTIVE_LOOKUP.get();
    if (capture != null) {
      capture.retryable = true;
    }
  }

  static void markCurrentRetryScopeAborted() {
    LookupCapture capture = ACTIVE_LOOKUP.get();
    if (capture != null) {
      capture.retryable = true;
      capture.aborted = true;
    }
  }

  static boolean isCurrentThreadHoldingLock() {
    Integer depth = ACTIVE_LOCK_DEPTH.get();
    return depth != null && depth > 0;
  }

  private void lockRead() {
    lock.readLock().lock();
    enterLock();
  }

  private void unlockRead() {
    exitLock();
    lock.readLock().unlock();
  }

  private void lockWrite() {
    lock.writeLock().lock();
    enterLock();
  }

  private void unlockWrite() {
    exitLock();
    lock.writeLock().unlock();
  }

  private static void enterLock() {
    Integer depth = ACTIVE_LOCK_DEPTH.get();
    ACTIVE_LOCK_DEPTH.set(depth == null ? 1 : depth + 1);
  }

  private static void exitLock() {
    Integer depth = ACTIVE_LOCK_DEPTH.get();
    if (depth == null || depth <= 1) {
      ACTIVE_LOCK_DEPTH.remove();
    } else {
      ACTIVE_LOCK_DEPTH.set(depth - 1);
    }
  }

  /**
   * Look up {@code dataTypeId} via virtual {@link #getDataType(NodeId)} and report whether a {@code
   * null} outcome (or an aborted lookup) was skipped or aborted by a retryable interruption rather
   * than recorded as a negative cache entry.
   */
  LookupResult lookupDataType(NodeId dataTypeId) {
    RetryScope scope = openRetryScope();
    boolean propagate = true;
    try {
      DataType dataType = getDataType(dataTypeId);
      boolean aborted = Thread.currentThread().isInterrupted() || scope.isAborted();
      if (dataType != null) {
        if (!aborted) {
          propagate = false;
          return new LookupResult(dataType, false, false);
        }
        return new LookupResult(null, true, true);
      }
      return new LookupResult(null, aborted || scope.isRetryable(), aborted);
    } catch (RuntimeException e) {
      boolean aborted =
          Thread.currentThread().isInterrupted()
              || scope.isAborted()
              || ClientBrowseUtils.hasInterruptedCause(e);
      if (aborted || scope.isRetryable()) {
        LOGGER.debug(
            "DataType lookup for {} threw after retryable miss: {}", dataTypeId, e.getMessage());
        return new LookupResult(null, true, aborted);
      }
      throw e;
    } finally {
      scope.close(propagate);
    }
  }

  private void ensureResolved(NodeId dataTypeId) {
    ResolutionOutcome outcome = resolveIfNeeded(dataTypeId);
    if (outcome == ResolutionOutcome.ABORTED) {
      markCurrentRetryScopeAborted();
    } else if (outcome == ResolutionOutcome.REENTRANT) {
      markCurrentRetryScopeRetryable();
    }
  }

  /**
   * Ensure {@code dataTypeId} has been resolved if possible.
   *
   * @return the outcome of the resolution attempt: {@link ResolutionOutcome#COMPLETED} when the
   *     attempt finished normally (resolved or negatively cached), {@link
   *     ResolutionOutcome#REENTRANT} when skipped because {@code dataTypeId} is already in progress
   *     on this thread, or {@link ResolutionOutcome#ABORTED} when skipped or aborted due to
   *     interruption, an interrupt-caused failure, or an active read lock.
   */
  private ResolutionOutcome resolveIfNeeded(NodeId dataTypeId) {
    // Fast path under read lock
    lockRead();
    try {
      if (types.containsKey(dataTypeId) || attemptedResolution.contains(dataTypeId)) {
        return ResolutionOutcome.COMPLETED;
      }
      if (inProgressResolution.contains(dataTypeId)) {
        return ResolutionOutcome.REENTRANT;
      }
    } finally {
      unlockRead();
    }

    if (lock.getReadHoldCount() > 0) {
      // A thread holding this tree's read lock cannot upgrade to the write lock without
      // deadlocking permanently.
      LOGGER.warn(
          "Skipping lazy resolution of DataType {} while read lock is already held", dataTypeId);
      return ResolutionOutcome.ABORTED;
    }

    if (Thread.currentThread().isInterrupted()) {
      LOGGER.debug(
          "Skipping lazy resolution of DataType {} because current thread is interrupted",
          dataTypeId);
      return ResolutionOutcome.ABORTED;
    }

    // Slow path under write lock
    lockWrite();
    try {
      // Re-check under write lock
      if (types.containsKey(dataTypeId) || attemptedResolution.contains(dataTypeId)) {
        return ResolutionOutcome.COMPLETED;
      }
      if (inProgressResolution.contains(dataTypeId)) {
        return ResolutionOutcome.REENTRANT;
      }

      if (Thread.currentThread().isInterrupted()) {
        LOGGER.debug(
            "Skipping lazy resolution of DataType {} because current thread is interrupted",
            dataTypeId);
        return ResolutionOutcome.ABORTED;
      }

      long startEpoch = failedResolutionEpoch.get();
      ResolutionOutcome outcome = ResolutionOutcome.COMPLETED;
      inProgressResolution.add(dataTypeId);
      try {
        try {
          resolvePath(dataTypeId);
        } catch (RetryableResolutionException e) {
          outcome = e.aborted ? ResolutionOutcome.ABORTED : ResolutionOutcome.REENTRANT;
          LOGGER.debug("Aborted resolution of DataType {}: {}", dataTypeId, e.getMessage());
        } catch (UaException e) {
          if (ClientBrowseUtils.hasInterruptedCause(e)) {
            outcome = ResolutionOutcome.ABORTED;
          }
          LOGGER.debug("Failed to resolve DataType {}: {}", dataTypeId, e.getMessage());
        } catch (RuntimeException e) {
          // Query methods are documented not to throw on resolution failure; an unexpected
          // RuntimeException here usually indicates a non-compliant server response.
          if (ClientBrowseUtils.hasInterruptedCause(e)) {
            outcome = ResolutionOutcome.ABORTED;
          }
          LOGGER.warn("Unexpected error resolving DataType {}", dataTypeId, e);
        }
      } finally {
        inProgressResolution.remove(dataTypeId);
      }

      if (Thread.currentThread().isInterrupted()) {
        outcome = ResolutionOutcome.ABORTED;
      }

      if (!types.containsKey(dataTypeId) && outcome == ResolutionOutcome.COMPLETED) {
        if (failedResolutionEpoch.get() == startEpoch) {
          attemptedResolution.add(dataTypeId);
        } else {
          outcome = ResolutionOutcome.REENTRANT;
        }
      }

      return outcome;
    } finally {
      unlockWrite();
    }
  }

  private void resolvePath(NodeId dataTypeId) throws UaException {
    // Pin resolution to the session it started on so results assembled across a session change
    // (e.g. a reconnection mid-resolution) are never cached.
    NodeId sessionId = client.getSession().getSessionId();

    NamespaceTable nsTable = getNamespaceTable();
    OperationLimits limits = client.getOperationLimits();

    List<NodeId> pathToResolve = browseInverseUntilKnown(dataTypeId, types.keySet(), nsTable);

    if (Thread.currentThread().isInterrupted()) {
      return;
    }

    if (pathToResolve.size() < 2) {
      LOGGER.debug("Could not resolve path to known ancestor for DataType {}", dataTypeId);
      return;
    }

    // pathToResolve = [target, parent, ..., knownAncestor]
    List<NodeId> nodesToAdd = pathToResolve.subList(0, pathToResolve.size() - 1);
    NodeId knownAncestorId = pathToResolve.get(pathToResolve.size() - 1);

    List<ClientDataType> dataTypes = fetchDataTypeInfoBatch(nodesToAdd, nsTable, limits);

    if (Thread.currentThread().isInterrupted() || dataTypes.size() != nodesToAdd.size()) {
      return;
    }

    ClientBrowseUtils.checkSessionUnchanged(client, sessionId);

    if (Thread.currentThread().isInterrupted()) {
      return;
    }

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

      Tree<DataType> existing = types.get(dataType.getNodeId());
      if (existing != null) {
        parentTree = existing;
        continue;
      }

      Tree<DataType> childTree = parentTree.addChild(dataType);
      types.put(dataType.getNodeId(), childTree);
      attemptedResolution.remove(dataType.getNodeId());
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

    if (Thread.currentThread().isInterrupted()) {
      return List.of();
    }

    // Browse encodings
    List<List<ReferenceDescription>> encodingRefs =
        ClientBrowseUtils.browseEncodings(client, nodeIds, limits);

    if (Thread.currentThread().isInterrupted()) {
      return List.of();
    }

    var result = new ArrayList<@Nullable ClientDataType>();

    for (int i = 0; i < nodeIds.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      int valueOffset = i * 3;

      QualifiedName browseName = extractBrowseName(values.get(valueOffset));
      Boolean isAbstract = extractIsAbstract(values.get(valueOffset + 1));
      DataTypeDefinition definition = extractDataTypeDefinition(values.get(valueOffset + 2));

      if (Thread.currentThread().isInterrupted()) {
        return List.of();
      }

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
      NodeId startId, Set<NodeId> knownTypeIds, NamespaceTable namespaceTable) throws UaException {

    var visited = new HashSet<NodeId>();
    List<NodeId> path = new ArrayList<>();
    NodeId current = startId;

    while (current != null && !knownTypeIds.contains(current)) {
      if (Thread.currentThread().isInterrupted()) {
        return List.of();
      }
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
  private @Nullable NodeId browseInverseParent(NodeId nodeId, NamespaceTable namespaceTable)
      throws UaException {
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
      if (ClientBrowseUtils.hasInterruptedCause(e)) {
        throw e;
      }
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

  private @Nullable DataTypeDefinition extractDataTypeDefinition(DataValue value)
      throws UaException {
    if (value.statusCode().isGood()) {
      Object o = value.value().value();
      if (o instanceof ExtensionObject xo) {
        RetryScope decodeScope = openRetryScope();
        boolean propagate = true;
        try {
          Object decoded = xo.decode(client.getStaticEncodingContext());
          if (decoded instanceof DataTypeDefinition dtd) {
            if (Thread.currentThread().isInterrupted() || decodeScope.isAborted()) {
              throw new RetryableResolutionException(
                  "Interrupted while decoding DataTypeDefinition", true);
            }
            propagate = false;
            return dtd;
          }
          boolean aborted = Thread.currentThread().isInterrupted() || decodeScope.isAborted();
          if (aborted || decodeScope.isRetryable()) {
            throw new RetryableResolutionException(
                "Retryable miss while decoding DataTypeDefinition", aborted);
          }
        } catch (RetryableResolutionException e) {
          throw e;
        } catch (Exception e) {
          boolean aborted =
              Thread.currentThread().isInterrupted()
                  || decodeScope.isAborted()
                  || ClientBrowseUtils.hasInterruptedCause(e);
          if (aborted || decodeScope.isRetryable()) {
            throw new RetryableResolutionException(
                "Retryable failure while decoding DataTypeDefinition", e, aborted);
          }
          LOGGER.debug("Error decoding DataTypeDefinition: {}", e.getMessage());
        } finally {
          decodeScope.close(propagate);
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
    lockRead();
    try {
      return query.get();
    } finally {
      unlockRead();
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
    lockRead();
    try {
      return tree.map(dataType -> dataType);
    } finally {
      unlockRead();
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
    Tree<DataType> snapshot;
    lockRead();
    try {
      if (super.getTreeNode(dataTypeId) == null) {
        return null;
      }

      snapshot = tree.map(dataType -> dataType);
    } finally {
      unlockRead();
    }

    var treeNode = new AtomicReference<Tree<DataType>>();
    snapshot.traverseNodes(
        node -> {
          if (node.getValue().getNodeId().equals(dataTypeId)) {
            treeNode.set(node);
          }
        });
    return treeNode.get();
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
    return resolvedQuery(
        dataTypeId,
        () -> {
          // Walk the parent chain directly under this read lock rather than delegating to the
          // superclass, which would dispatch virtually back into getBackingClass().
          Tree<DataType> node = super.getTreeNode(dataTypeId);

          while (node != null) {
            Tree<DataType> parent = node.getParent();
            if (parent == null) {
              return Object.class;
            }
            NodeId parentId = parent.getValue().getNodeId();
            if (hasStaticBackingClass(parentId)) {
              return super.getBackingClass(parentId);
            }
            node = parent;
          }

          return Object.class;
        });
  }

  @Override
  public OpcUaDataType getBuiltinType(NodeId dataTypeId) {
    if (OpcUaDataType.isBuiltin(dataTypeId)) {
      // The superclass answers without consulting the tree; skip resolution and locking.
      return super.getBuiltinType(dataTypeId);
    }
    return resolvedQuery(
        dataTypeId,
        () -> {
          // Walk the parent chain directly under this read lock rather than delegating to the
          // superclass, which would dispatch virtually back into getBuiltinType().
          Tree<DataType> node = super.getTreeNode(dataTypeId);

          while (node != null) {
            Tree<DataType> parent = node.getParent();
            if (parent == null) {
              return OpcUaDataType.Variant;
            }
            NodeId parentId = parent.getValue().getNodeId();
            if (OpcUaDataType.isBuiltin(parentId)) {
              return super.getBuiltinType(parentId);
            }
            node = parent;
          }

          return OpcUaDataType.Variant;
        });
  }

  @Override
  public @Nullable DataType getDataType(NodeId dataTypeId) {
    return getType(dataTypeId);
  }

  @Override
  public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    DataType dataType = getDataType(dataTypeId);
    return dataType != null ? dataType.getBinaryEncodingId() : null;
  }

  @Override
  public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    DataType dataType = getDataType(dataTypeId);
    return dataType != null ? dataType.getXmlEncodingId() : null;
  }

  @Override
  public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    DataType dataType = getDataType(dataTypeId);
    return dataType != null ? dataType.getJsonEncodingId() : null;
  }

  @Override
  public @Nullable DataTypeDefinition getDataTypeDefinition(NodeId dataTypeId) {
    DataType dataType = getDataType(dataTypeId);
    return dataType != null ? dataType.getDataTypeDefinition() : null;
  }

  @Override
  public boolean isAssignable(NodeId dataTypeId, Class<?> clazz) {
    // Resolution and locking are handled by the getBackingClass override.
    return super.isAssignable(dataTypeId, clazz);
  }

  @Override
  public boolean isEnumType(NodeId dataTypeId) {
    return isSubtypeOf(dataTypeId, NodeIds.Enumeration);
  }

  @Override
  public boolean isStructType(NodeId dataTypeId) {
    return isSubtypeOf(dataTypeId, NodeIds.Structure);
  }

  @Override
  public boolean isSubtypeOf(NodeId typeId, NodeId superTypeId) {
    ensureResolved(typeId);
    lockRead();
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
      unlockRead();
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

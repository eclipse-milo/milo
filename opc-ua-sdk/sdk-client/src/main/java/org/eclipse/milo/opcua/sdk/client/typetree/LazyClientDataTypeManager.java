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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.DataTypeInitializer;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A lazy-loading {@link org.eclipse.milo.opcua.stack.core.types.DataTypeManager} that resolves
 * codecs on demand.
 *
 * <p>This implementation works in conjunction with {@link LazyClientDataTypeTree} to provide fully
 * lazy resolution of both type information and codecs. When a codec is requested for a type that
 * isn't already registered, this manager:
 *
 * <ol>
 *   <li>Uses the {@link DataTypeTree} to resolve the type (which triggers lazy browsing if using
 *       {@link LazyClientDataTypeTree})
 *   <li>Creates a codec using {@link DynamicCodecFactory}
 *   <li>Registers the codec for future use
 * </ol>
 *
 * <h2>Namespace 0 Types</h2>
 *
 * <p>Built-in types from namespace 0 are pre-initialized via {@link DataTypeInitializer} in the
 * constructor. Lazy resolution is only triggered for non-namespace-0 types.
 *
 * <h2>Thread Safety</h2>
 *
 * <p>This implementation is thread-safe. All read operations first check the parent class under no
 * lock, then acquire a write lock only when resolution is needed. Resolution attempts are tracked
 * to avoid repeated failures.
 *
 * <h2>Resolution Behavior</h2>
 *
 * <p>Resolution errors (e.g., network failures, non-existent types) do not cause exceptions.
 * Instead, {@code null} is returned. Once a resolution attempt has failed, it will not be retried
 * unless {@link #clearFailedResolutions()} is called. If the calling thread is interrupted before
 * or during resolution, the interrupt status is preserved and the failed attempt is not cached,
 * allowing subsequent lookups to retry.
 */
public class LazyClientDataTypeManager extends DefaultDataTypeManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(LazyClientDataTypeManager.class);

  private final OpcUaClient client;
  private final DataTypeTree dataTypeTree;
  private final BiFunction<DataType, DataTypeTree, DataTypeCodec> codecFactory;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  // Read without the lock on fast paths; contains only ids whose resolution attempt has
  // completed and failed, so an in-flight resolution is never mistaken for a failed one.
  private final Set<NodeId> attemptedResolution = ConcurrentHashMap.newKeySet();

  // Guarded by `lock`: tracks lookup ids currently being resolved on the write-lock holder thread
  // so a reentrant lookup does not recurse or poison attemptedResolution.
  private final Set<NodeId> inProgressResolution = new HashSet<>();

  // Guarded by `lock`: tracks canonical DataType ids currently executing codecFactory on the
  // write-lock holder thread so a reentrant lookup (for example via an encoding id) cannot run
  // codecFactory twice for the same DataType.
  private final Set<NodeId> inProgressCodecs = new HashSet<>();

  // Guarded by `this` monitor (matching DefaultDataTypeManager): mirrors DefaultDataTypeManager's
  // 5 registration maps (`types`, `binaryEncodings`, `xmlEncodings`, `jsonEncodings`, `encodings`)
  // for managed registrations created by acquireType() (as opposed to permanent registerType()
  // registrations, even when borrowed via acquireType()). Tracking forward (dataTypeId ->
  // encoding) and reverse (encodingId -> dataType) ownership separately matches
  // DefaultDataTypeManager.Handle.close() so a live temporary forward mapping left behind after
  // registerType() replaces the type and/or reverse encoding still keeps missing encodings
  // retryable until the handle closes.
  private final Map<NodeId, TemporaryRegistration> temporaryTypes = new HashMap<>();
  private final Map<NodeId, TemporaryRegistration> temporaryBinaryEncodings = new HashMap<>();
  private final Map<NodeId, TemporaryRegistration> temporaryXmlEncodings = new HashMap<>();
  private final Map<NodeId, TemporaryRegistration> temporaryJsonEncodings = new HashMap<>();
  private final Map<NodeId, TemporaryRegistration> temporaryReverseEncodings = new HashMap<>();

  // Monotonically incremented on explicit registration mutations (registerType, acquireType,
  // temporary handle close) and clearFailedResolutions() so an in-flight failure cannot re-insert
  // a stale negative-cache entry after invalidation.
  private final AtomicLong registrationEpoch = new AtomicLong();

  private static final class TemporaryRegistration {
    final @Nullable NodeId binaryEncodingId;
    final @Nullable NodeId xmlEncodingId;
    final @Nullable NodeId jsonEncodingId;
    int users = 1;

    TemporaryRegistration(
        @Nullable NodeId binaryEncodingId,
        @Nullable NodeId xmlEncodingId,
        @Nullable NodeId jsonEncodingId) {
      this.binaryEncodingId = normalizeEncodingId(binaryEncodingId);
      this.xmlEncodingId = normalizeEncodingId(xmlEncodingId);
      this.jsonEncodingId = normalizeEncodingId(jsonEncodingId);
    }
  }

  // Scoped to resolution calls on the current thread so nested manager lookups (for example from a
  // custom codecFactory or encoding-ID getter) propagate retryable misses (interrupts, reentrancy,
  // or temporary registration collisions) to outer lookups even with an eager DataTypeTree.
  private final ThreadLocal<@Nullable ManagerRetryCapture> activeManagerLookup =
      new ThreadLocal<>();

  private static final class ManagerRetryCapture {
    boolean retryable;
    boolean aborted;
  }

  private final class ManagerRetryScope implements AutoCloseable {
    private final @Nullable ManagerRetryCapture previous;
    private final ManagerRetryCapture current;

    private ManagerRetryScope(@Nullable ManagerRetryCapture previous, ManagerRetryCapture current) {
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
        activeManagerLookup.set(previous);
      } else {
        activeManagerLookup.remove();
      }
    }

    @Override
    public void close() {
      close(true);
    }
  }

  private enum ExistingRegistration {
    NONE,
    REGISTERED,
    PERMANENT_WITHOUT_ENCODINGS,
    TEMPORARY_COLLISION,
    PERMANENT_COLLISION
  }

  private enum CollisionKind {
    NONE,
    TEMPORARY,
    PERMANENT
  }

  private record ExistingEncodingLookup(
      ExistingRegistration state, @Nullable NodeId encodingId, boolean temporaryMissing) {}

  private record CodecResolution(
      @Nullable DataTypeCodec codec,
      boolean skipNegativeCache,
      boolean retryableAbort,
      boolean missingDefinition) {
    static final CodecResolution FAILED = new CodecResolution(null, false, false, false);
    static final CodecResolution NO_DEFINITION = new CodecResolution(null, false, false, true);
    static final CodecResolution REENTRANT = new CodecResolution(null, true, false, false);
    static final CodecResolution RETRYABLE = new CodecResolution(null, true, true, false);

    static CodecResolution resolved(DataTypeCodec codec) {
      return new CodecResolution(codec, false, false, false);
    }

    static CodecResolution existing(DataTypeCodec codec) {
      return new CodecResolution(codec, true, false, false);
    }
  }

  /**
   * Create a new {@link LazyClientDataTypeManager}.
   *
   * @param client a connected {@link OpcUaClient}.
   * @param namespaceTable the server's {@link NamespaceTable}.
   * @param dataTypeTree the {@link DataTypeTree} to use for type resolution. This is typically a
   *     {@link LazyClientDataTypeTree} for full lazy behavior.
   */
  public LazyClientDataTypeManager(
      OpcUaClient client, NamespaceTable namespaceTable, DataTypeTree dataTypeTree) {

    this(client, namespaceTable, dataTypeTree, DynamicCodecFactory::create);
  }

  /**
   * Create a new {@link LazyClientDataTypeManager} with a custom codec factory.
   *
   * @param client a connected {@link OpcUaClient}.
   * @param namespaceTable the server's {@link NamespaceTable}.
   * @param dataTypeTree the {@link DataTypeTree} to use for type resolution.
   * @param codecFactory a factory function that creates {@link DataTypeCodec}s from {@link
   *     DataType} and {@link DataTypeTree}.
   */
  public LazyClientDataTypeManager(
      OpcUaClient client,
      NamespaceTable namespaceTable,
      DataTypeTree dataTypeTree,
      BiFunction<DataType, DataTypeTree, DataTypeCodec> codecFactory) {

    this.client = client;
    this.dataTypeTree = dataTypeTree;
    this.codecFactory = codecFactory;

    // Pre-initialize namespace 0 codecs
    new DataTypeInitializer().initialize(namespaceTable, this);
  }

  /**
   * Clear failed resolution attempts, allowing retry.
   *
   * <p>When a codec resolution fails (e.g., due to network errors or non-existent types), the
   * failure is recorded to avoid repeated failed attempts. This method clears those records,
   * allowing later queries for those types to attempt resolution again.
   *
   * <p>This is useful after transient network issues have been resolved or when the server's type
   * system may have changed.
   */
  public synchronized void clearFailedResolutions() {
    attemptedResolution.clear();
    registrationEpoch.incrementAndGet();
  }

  @Override
  public @Nullable DataTypeCodec getCodec(NodeId id) {
    // Fast path: check if already registered
    DataTypeCodec codec = super.getCodec(id);
    if (codec != null) {
      return codec;
    }

    // Never lazily resolve namespace 0 types; they should already be initialized
    if (id.getNamespaceIndex().intValue() == 0) {
      return null;
    }

    // Skip if we've already tried and failed
    if (attemptedResolution.contains(id)) {
      return null;
    }

    if (Thread.currentThread().isInterrupted()) {
      LOGGER.debug(
          "Skipping lazy codec resolution for {} because current thread is interrupted", id);
      markManagerLookupAborted();
      return null;
    }

    // Slow path: attempt lazy resolution under write lock. Defer immediately if the current thread
    // already holds this manager's monitor (to avoid acquiring the tree lock while holding `this`),
    // and use a non-blocking tryLock if holding any LazyClientDataTypeTree lock so concurrent
    // manager -> tree lookups cannot deadlock.
    if (!acquireWriteLockForLookup()) {
      LOGGER.debug(
          "Deferring lazy codec resolution for {} to avoid lock-ordering inversion while holding"
              + " the manager monitor or a DataTypeTree lock",
          id);
      markManagerLookupAborted();
      return null;
    }
    try {
      // Re-check in case another thread just resolved it or just failed to
      codec = super.getCodec(id);
      if (codec != null) {
        return codec;
      }
      if (attemptedResolution.contains(id)) {
        return null;
      }
      if (inProgressResolution.contains(id) || inProgressCodecs.contains(id)) {
        markManagerLookupRetryable();
        return null;
      }
      if (Thread.currentThread().isInterrupted()) {
        LOGGER.debug(
            "Skipping lazy codec resolution for {} because current thread is interrupted", id);
        markManagerLookupAborted();
        return null;
      }

      long startEpoch = registrationEpoch.get();
      CodecResolution resolution;
      inProgressResolution.add(id);
      try {
        resolution = resolveAndRegisterCodec(id);
      } finally {
        inProgressResolution.remove(id);
      }

      if (resolution.codec() == null) {
        if (resolution.retryableAbort() || Thread.currentThread().isInterrupted()) {
          markManagerLookupAborted();
        } else if (resolution.skipNegativeCache()) {
          markManagerLookupRetryable();
        } else {
          boolean interrupted = false;
          boolean invalidated = false;
          synchronized (this) {
            if (Thread.currentThread().isInterrupted()) {
              interrupted = true;
            } else {
              invalidated = registrationEpoch.get() != startEpoch || super.getCodec(id) != null;
              if (!invalidated) {
                attemptedResolution.add(id);
              }
            }
          }
          if (interrupted) {
            markManagerLookupAborted();
          } else if (invalidated) {
            markManagerLookupRetryable();
          }
        }
      }

      return resolution.codec();
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public synchronized void registerType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {
    super.registerType(dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
    NodeId binary = normalizeEncodingId(binaryEncodingId);
    NodeId xml = normalizeEncodingId(xmlEncodingId);
    NodeId json = normalizeEncodingId(jsonEncodingId);
    temporaryTypes.remove(dataTypeId);
    if (binary != null) {
      temporaryBinaryEncodings.remove(dataTypeId);
      temporaryReverseEncodings.remove(binary);
    }
    if (xml != null) {
      temporaryXmlEncodings.remove(dataTypeId);
      temporaryReverseEncodings.remove(xml);
    }
    if (json != null) {
      temporaryJsonEncodings.remove(dataTypeId);
      temporaryReverseEncodings.remove(json);
    }
    registrationEpoch.incrementAndGet();
    attemptedResolution.remove(dataTypeId);
    removeAttemptedResolution(binary);
    removeAttemptedResolution(xml);
    removeAttemptedResolution(json);
  }

  @Override
  public synchronized RegistrationHandle acquireType(
      NodeId dataTypeId,
      DataTypeCodec codec,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {
    boolean existedBefore = super.getCodec(dataTypeId) != null;
    RegistrationHandle handle =
        super.acquireType(dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId);

    registrationEpoch.incrementAndGet();
    attemptedResolution.remove(dataTypeId);
    removeAttemptedResolution(binaryEncodingId);
    removeAttemptedResolution(xmlEncodingId);
    removeAttemptedResolution(jsonEncodingId);

    TemporaryRegistration tracked;
    if (!existedBefore) {
      tracked = new TemporaryRegistration(binaryEncodingId, xmlEncodingId, jsonEncodingId);
      temporaryTypes.put(dataTypeId, tracked);
      if (tracked.binaryEncodingId != null) {
        temporaryBinaryEncodings.put(dataTypeId, tracked);
        temporaryReverseEncodings.put(tracked.binaryEncodingId, tracked);
      }
      if (tracked.xmlEncodingId != null) {
        temporaryXmlEncodings.put(dataTypeId, tracked);
        temporaryReverseEncodings.put(tracked.xmlEncodingId, tracked);
      }
      if (tracked.jsonEncodingId != null) {
        temporaryJsonEncodings.put(dataTypeId, tracked);
        temporaryReverseEncodings.put(tracked.jsonEncodingId, tracked);
      }
    } else {
      tracked = temporaryTypes.get(dataTypeId);
      if (tracked != null) {
        tracked.users++;
      } else {
        // Borrowed an existing permanent registration from registerType(): closing this handle
        // leaves the permanent registration intact, so do not mark it temporary.
        return handle;
      }
    }

    TemporaryRegistration owned = tracked;
    return new RegistrationHandle() {
      private boolean closed;

      @Override
      public void close() {
        synchronized (LazyClientDataTypeManager.this) {
          if (closed) {
            return;
          }
          closed = true;
          try {
            handle.close();
          } finally {
            if (--owned.users == 0) {
              temporaryTypes.remove(dataTypeId, owned);
              temporaryBinaryEncodings.remove(dataTypeId, owned);
              temporaryXmlEncodings.remove(dataTypeId, owned);
              temporaryJsonEncodings.remove(dataTypeId, owned);
              removeTemporaryReverseEncoding(owned.binaryEncodingId, owned);
              removeTemporaryReverseEncoding(owned.xmlEncodingId, owned);
              removeTemporaryReverseEncoding(owned.jsonEncodingId, owned);
              registrationEpoch.incrementAndGet();
            }
          }
        }
      }
    };
  }

  @Override
  public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    return getEncodingId(dataTypeId, super::getBinaryEncodingId);
  }

  @Override
  public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    return getEncodingId(dataTypeId, super::getXmlEncodingId);
  }

  @Override
  public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    return getEncodingId(dataTypeId, super::getJsonEncodingId);
  }

  private @Nullable NodeId getEncodingId(
      NodeId dataTypeId, Function<NodeId, @Nullable NodeId> getter) {
    // Skip namespace 0 types
    if (dataTypeId.getNamespaceIndex().intValue() == 0) {
      return getter.apply(dataTypeId);
    }

    // Fast path: atomically inspect the existing registration and read the requested encoding id
    // under DefaultDataTypeManager's monitor so a concurrent temporary handle close cannot drop
    // both the encoding id and the temporary-registration signal between checks.
    ExistingEncodingLookup fastLookup = checkExistingEncoding(dataTypeId, getter);
    if (fastLookup.state() == ExistingRegistration.REGISTERED) {
      if (fastLookup.temporaryMissing()) {
        markManagerLookupRetryable();
      }
      return fastLookup.encodingId();
    }
    if (fastLookup.state() == ExistingRegistration.TEMPORARY_COLLISION) {
      markManagerLookupAborted();
      return null;
    }
    if (fastLookup.state() == ExistingRegistration.PERMANENT_COLLISION) {
      return null;
    }

    // Skip if a previous attempt already failed
    if (attemptedResolution.contains(dataTypeId)) {
      return getter.apply(dataTypeId);
    }

    if (Thread.currentThread().isInterrupted()) {
      LOGGER.debug(
          "Skipping lazy codec resolution for DataType {} because current thread is interrupted",
          dataTypeId);
      markManagerLookupAborted();
      return null;
    }

    // Slow path: attempt resolution under write lock (defer immediately if holding this manager's
    // monitor, and use non-blocking tryLock if holding any DataTypeTree lock to prevent
    // lock-inversion deadlocks with concurrent manager <-> tree lookups).
    if (!acquireWriteLockForLookup()) {
      LOGGER.debug(
          "Deferring lazy codec resolution for DataType {} to avoid lock-ordering inversion while"
              + " holding the manager monitor or a DataTypeTree lock",
          dataTypeId);
      markManagerLookupAborted();
      return null;
    }
    try {
      // Re-check atomically in case another thread just registered it or just failed to
      ExistingEncodingLookup existingLookup = checkExistingEncoding(dataTypeId, getter);
      if (existingLookup.state() == ExistingRegistration.REGISTERED) {
        if (existingLookup.temporaryMissing()) {
          markManagerLookupRetryable();
        }
        return existingLookup.encodingId();
      }
      if (existingLookup.state() == ExistingRegistration.TEMPORARY_COLLISION) {
        markManagerLookupAborted();
        return null;
      }
      if (existingLookup.state() == ExistingRegistration.PERMANENT_COLLISION) {
        return null;
      }
      if (attemptedResolution.contains(dataTypeId)) {
        return getter.apply(dataTypeId);
      }
      if (inProgressResolution.contains(dataTypeId) || inProgressCodecs.contains(dataTypeId)) {
        markManagerLookupRetryable();
        return null;
      }

      if (Thread.currentThread().isInterrupted()) {
        LOGGER.debug(
            "Skipping lazy codec resolution for DataType {} because current thread is interrupted",
            dataTypeId);
        markManagerLookupAborted();
        return null;
      }

      long startEpoch = registrationEpoch.get();
      CodecResolution resolution;
      inProgressResolution.add(dataTypeId);
      try {
        resolution = resolveAndRegisterCodecFromDataTypeId(dataTypeId);
      } finally {
        inProgressResolution.remove(dataTypeId);
      }

      ExistingEncodingLookup postLookup;
      boolean retryableAbort = false;
      boolean retryable = false;
      synchronized (this) {
        postLookup = checkExistingEncoding(dataTypeId, getter);
        boolean registered =
            resolution.codec() != null || postLookup.state() == ExistingRegistration.REGISTERED;

        if (postLookup.encodingId() == null
            && (resolution.retryableAbort() || Thread.currentThread().isInterrupted())) {
          retryableAbort = true;
        } else if (!registered) {
          if (resolution.skipNegativeCache()) {
            retryable = true;
          } else if (registrationEpoch.get() == startEpoch) {
            attemptedResolution.add(dataTypeId);
          } else {
            retryable = true;
          }
        } else if (postLookup.temporaryMissing()
            || (postLookup.encodingId() == null
                && (resolution.skipNegativeCache()
                    || postLookup.state() != ExistingRegistration.REGISTERED))) {
          retryable = true;
        }
      }
      if (retryableAbort) {
        markManagerLookupAborted();
      } else if (retryable) {
        markManagerLookupRetryable();
      }

      return postLookup.encodingId();
    } finally {
      lock.writeLock().unlock();
    }
  }

  private boolean acquireWriteLockForLookup() {
    if (Thread.holdsLock(this)) {
      return false;
    }
    if (LazyClientDataTypeTree.isCurrentThreadHoldingLock()) {
      return lock.writeLock().tryLock();
    }
    lock.writeLock().lock();
    return true;
  }

  private ManagerRetryScope openManagerRetryScope() {
    ManagerRetryCapture previous = activeManagerLookup.get();
    var current = new ManagerRetryCapture();
    activeManagerLookup.set(current);
    return new ManagerRetryScope(previous, current);
  }

  private void markManagerLookupRetryable() {
    ManagerRetryCapture capture = activeManagerLookup.get();
    if (capture != null) {
      capture.retryable = true;
    }
    LazyClientDataTypeTree.markCurrentRetryScopeRetryable();
  }

  private void markManagerLookupAborted() {
    ManagerRetryCapture capture = activeManagerLookup.get();
    if (capture != null) {
      capture.retryable = true;
      capture.aborted = true;
    }
    LazyClientDataTypeTree.markCurrentRetryScopeAborted();
  }

  private LazyClientDataTypeTree.LookupResult lookupDataType(NodeId id) {
    if (dataTypeTree instanceof LazyClientDataTypeTree lazyTree) {
      return lazyTree.lookupDataType(id);
    }
    return new LazyClientDataTypeTree.LookupResult(dataTypeTree.getDataType(id), false, false);
  }

  private CodecResolution resolveAndRegisterCodec(NodeId id) {
    LazyClientDataTypeTree.RetryScope treeRetryScope = LazyClientDataTypeTree.openRetryScope();
    ManagerRetryScope managerRetryScope = openManagerRetryScope();
    boolean succeeded = false;
    try {
      // First, try to treat `id` as a DataType NodeId
      LazyClientDataTypeTree.LookupResult lookup = lookupDataType(id);
      if (lookup.aborted() || isAborted(treeRetryScope, managerRetryScope)) {
        return CodecResolution.RETRYABLE;
      }
      if (lookup.retryable()) {
        return CodecResolution.REENTRANT;
      }

      DataType dataType = lookup.dataType();
      if (dataType == null && isRetryable(treeRetryScope, managerRetryScope)) {
        return CodecResolution.REENTRANT;
      }
      if (dataType != null) {
        CodecResolution direct = createAndRegisterCodec(id, dataType, false, null);
        if (!direct.missingDefinition()) {
          succeeded = direct.codec() != null;
          return direct;
        }
      }

      // Otherwise, treat `id` as an encoding NodeId and discover its DataType
      NodeId dataTypeId = browseDataTypeIdForEncoding(id);
      if (isAborted(treeRetryScope, managerRetryScope)) {
        return CodecResolution.RETRYABLE;
      }
      if (dataTypeId == null) {
        if (isRetryable(treeRetryScope, managerRetryScope)) {
          return CodecResolution.REENTRANT;
        }
        LOGGER.debug("No DataType found for encoding {}", id);
        return CodecResolution.FAILED;
      }
      if (inProgressCodecs.contains(dataTypeId) || !inProgressResolution.add(dataTypeId)) {
        return CodecResolution.REENTRANT;
      }
      try {
        boolean permanentMissingEncoding = isPermanentRegistrationMissingEncoding(dataTypeId, id);
        if (!permanentMissingEncoding) {
          CodecResolution existing = lookupExistingCanonicalRegistration(dataTypeId);
          if (existing != null) {
            succeeded = existing.codec() != null;
            return existing;
          }
        }

        LazyClientDataTypeTree.LookupResult resolvedLookup = lookupDataType(dataTypeId);
        if (resolvedLookup.aborted() || isAborted(treeRetryScope, managerRetryScope)) {
          return CodecResolution.RETRYABLE;
        }
        if (resolvedLookup.retryable()) {
          return CodecResolution.REENTRANT;
        }

        dataType = resolvedLookup.dataType();
        if (dataType == null) {
          if (isRetryable(treeRetryScope, managerRetryScope)) {
            return CodecResolution.REENTRANT;
          }
          if (permanentMissingEncoding) {
            CodecResolution existing = lookupExistingCanonicalRegistration(dataTypeId);
            if (existing != null) {
              succeeded = existing.codec() != null;
              return existing;
            }
          }
          LOGGER.debug("No DataTypeDefinition available for {}", id);
          return CodecResolution.FAILED;
        }

        // Create the codec and register it (or install discovered missing encoding(s) while
        // retaining an existing permanent application-owned codec).
        CodecResolution resolution = createAndRegisterCodec(dataTypeId, dataType, true, id);
        if (resolution.codec() == null && !resolution.skipNegativeCache()) {
          // If the resolved canonical DataType (or discovered DataType id) already has a canonical
          // codec (for example registered without default binary/XML/JSON encoding ids), return it
          // rather than failing and negatively caching `id`.
          NodeId canonicalId = dataType.getNodeId();
          if (isAborted(treeRetryScope, managerRetryScope)) {
            return CodecResolution.RETRYABLE;
          }
          CodecResolution existing =
              canonicalId != null ? lookupExistingCanonicalRegistration(canonicalId) : null;
          if ((existing == null || existing.codec() == null) && !dataTypeId.equals(canonicalId)) {
            existing = lookupExistingCanonicalRegistration(dataTypeId);
          }
          if (existing != null && existing.codec() != null) {
            succeeded = true;
            return existing;
          }
          if (isRetryable(treeRetryScope, managerRetryScope)) {
            return CodecResolution.REENTRANT;
          }
          LOGGER.debug("No DataTypeDefinition available for {}", id);
        }
        succeeded = resolution.codec() != null;
        return resolution;
      } finally {
        inProgressResolution.remove(dataTypeId);
      }
    } catch (Exception e) {
      LOGGER.debug("Error resolving codec for {}: {}", id, e.getMessage());
      if (isAborted(treeRetryScope, managerRetryScope)
          || ClientBrowseUtils.hasInterruptedCause(e)) {
        return CodecResolution.RETRYABLE;
      }
      return isRetryable(treeRetryScope, managerRetryScope)
          ? CodecResolution.REENTRANT
          : CodecResolution.FAILED;
    } finally {
      managerRetryScope.close(!succeeded);
      treeRetryScope.close(!succeeded);
    }
  }

  private CodecResolution resolveAndRegisterCodecFromDataTypeId(NodeId dataTypeId) {
    LazyClientDataTypeTree.RetryScope treeRetryScope = LazyClientDataTypeTree.openRetryScope();
    ManagerRetryScope managerRetryScope = openManagerRetryScope();
    boolean succeeded = false;
    try {
      LazyClientDataTypeTree.LookupResult lookup = lookupDataType(dataTypeId);
      if (lookup.aborted() || isAborted(treeRetryScope, managerRetryScope)) {
        return CodecResolution.RETRYABLE;
      }
      if (lookup.retryable()) {
        return CodecResolution.REENTRANT;
      }

      DataType dataType = lookup.dataType();
      if (dataType != null) {
        CodecResolution resolution = createAndRegisterCodec(dataTypeId, dataType, true, null);
        succeeded = resolution.codec() != null;
        return resolution;
      }
    } catch (Exception e) {
      LOGGER.debug("Error resolving codec for DataType {}: {}", dataTypeId, e.getMessage());
      if (isAborted(treeRetryScope, managerRetryScope)
          || ClientBrowseUtils.hasInterruptedCause(e)) {
        return CodecResolution.RETRYABLE;
      }
      return isRetryable(treeRetryScope, managerRetryScope)
          ? CodecResolution.REENTRANT
          : CodecResolution.FAILED;
    } finally {
      managerRetryScope.close(!succeeded);
      treeRetryScope.close(!succeeded);
    }
    return isRetryable(treeRetryScope, managerRetryScope)
        ? CodecResolution.REENTRANT
        : CodecResolution.FAILED;
  }

  private CodecResolution createAndRegisterCodec(
      NodeId lookupId,
      DataType dataType,
      boolean resolveMissingEncodingsForPermanent,
      @Nullable NodeId requestedEncodingId) {
    LazyClientDataTypeTree.RetryScope treeRetryScope = LazyClientDataTypeTree.openRetryScope();
    ManagerRetryScope managerRetryScope = openManagerRetryScope();
    boolean succeeded = false;
    NodeId dataTypeId = null;
    boolean addedInProgress = false;
    try {
      dataTypeId = dataType.getNodeId();
      if (isAborted(treeRetryScope, managerRetryScope)) {
        return CodecResolution.RETRYABLE;
      }
      if (dataTypeId == null) {
        return isRetryable(treeRetryScope, managerRetryScope)
            ? CodecResolution.REENTRANT
            : CodecResolution.FAILED;
      }
      if ((!dataTypeId.equals(lookupId) && inProgressResolution.contains(dataTypeId))
          || !inProgressCodecs.add(dataTypeId)) {
        return CodecResolution.REENTRANT;
      }
      addedInProgress = true;

      if (resolveMissingEncodingsForPermanent
          && isPermanentRegistrationMissingEncoding(dataTypeId, requestedEncodingId)) {
        CodecResolution resolution =
            registerMissingEncodingsForPermanent(
                dataType, dataTypeId, requestedEncodingId, treeRetryScope, managerRetryScope);
        succeeded = resolution.codec() != null;
        return resolution;
      }

      CodecResolution existing = lookupExistingCanonicalRegistration(dataTypeId);
      if (existing != null) {
        succeeded = existing.codec() != null;
        return existing;
      }

      if (dataType.getDataTypeDefinition() == null) {
        if (isAborted(treeRetryScope, managerRetryScope)) {
          return CodecResolution.RETRYABLE;
        }
        return isRetryable(treeRetryScope, managerRetryScope)
            ? CodecResolution.REENTRANT
            : CodecResolution.NO_DEFINITION;
      }
      if (isAborted(treeRetryScope, managerRetryScope)) {
        return CodecResolution.RETRYABLE;
      }

      DataTypeCodec codec = codecFactory.apply(dataType, dataTypeTree);
      if (codec == null) {
        if (isAborted(treeRetryScope, managerRetryScope)) {
          return CodecResolution.RETRYABLE;
        }
        return isRetryable(treeRetryScope, managerRetryScope)
            ? CodecResolution.REENTRANT
            : CodecResolution.FAILED;
      }
      if (isAborted(treeRetryScope, managerRetryScope)) {
        return CodecResolution.RETRYABLE;
      }

      EncodingIdLookup binaryLookup = readBinaryEncodingId(dataType);
      if (binaryLookup.deferred() != null) {
        return binaryLookup.deferred();
      }
      EncodingIdLookup xmlLookup = readEncodingId(dataType::getXmlEncodingId);
      if (xmlLookup.deferred() != null) {
        return xmlLookup.deferred();
      }
      EncodingIdLookup jsonLookup = readEncodingId(dataType::getJsonEncodingId);
      if (jsonLookup.deferred() != null) {
        return jsonLookup.deferred();
      }

      NodeId binaryEncodingId = binaryLookup.encodingId();
      NodeId xmlEncodingId = xmlLookup.encodingId();
      NodeId jsonEncodingId = jsonLookup.encodingId();

      // Atomically re-check against DefaultDataTypeManager's monitor so a concurrent or reentrant
      // acquireType() or registerType() registration (on the DataType id or any of its encoding
      // ids) is never overwritten by lazy registration, and an interrupt arriving while waiting on
      // the monitor aborts before publishing.
      synchronized (this) {
        if (isAborted(treeRetryScope, managerRetryScope)) {
          markManagerLookupAborted();
          return CodecResolution.RETRYABLE;
        }
        CodecResolution concurrent = lookupExistingCanonicalRegistration(dataTypeId);
        if (concurrent != null) {
          succeeded = concurrent.codec() != null;
          return concurrent;
        }
        CodecResolution encodingCollision =
            checkEncodingCollision(null, binaryEncodingId, xmlEncodingId, jsonEncodingId);
        if (encodingCollision != null) {
          return encodingCollision;
        }
        super.registerType(dataTypeId, codec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
      }

      LOGGER.debug("Lazily registered codec for dataTypeId={}", dataTypeId);

      succeeded = true;
      return CodecResolution.resolved(codec);
    } catch (Exception e) {
      LOGGER.debug(
          "Error creating codec for DataType {}: {}",
          dataTypeId != null ? dataTypeId : lookupId,
          e.getMessage());
      if (isAborted(treeRetryScope, managerRetryScope)
          || ClientBrowseUtils.hasInterruptedCause(e)) {
        return CodecResolution.RETRYABLE;
      }
      return isRetryable(treeRetryScope, managerRetryScope)
          ? CodecResolution.REENTRANT
          : CodecResolution.FAILED;
    } finally {
      managerRetryScope.close(!succeeded);
      treeRetryScope.close(!succeeded);
      if (addedInProgress) {
        inProgressCodecs.remove(dataTypeId);
      }
    }
  }

  private CodecResolution registerMissingEncodingsForPermanent(
      DataType dataType,
      NodeId dataTypeId,
      @Nullable NodeId requestedEncodingId,
      LazyClientDataTypeTree.RetryScope treeRetryScope,
      ManagerRetryScope managerRetryScope) {
    EncodingIdLookup binaryLookup = readBinaryEncodingId(dataType);
    if (binaryLookup.deferred() != null) {
      return binaryLookup.deferred();
    }
    EncodingIdLookup xmlLookup = readEncodingId(dataType::getXmlEncodingId);
    if (xmlLookup.deferred() != null) {
      return xmlLookup.deferred();
    }
    EncodingIdLookup jsonLookup = readEncodingId(dataType::getJsonEncodingId);
    if (jsonLookup.deferred() != null) {
      return jsonLookup.deferred();
    }

    NodeId binaryEncodingId = binaryLookup.encodingId();
    NodeId xmlEncodingId = xmlLookup.encodingId();
    NodeId jsonEncodingId = jsonLookup.encodingId();

    boolean hasDiscoveredEncoding =
        (binaryEncodingId != null && !binaryEncodingId.isNull())
            || (xmlEncodingId != null && !xmlEncodingId.isNull())
            || (jsonEncodingId != null && !jsonEncodingId.isNull());
    if (!hasDiscoveredEncoding) {
      return CodecResolution.FAILED;
    }

    synchronized (this) {
      if (isAborted(treeRetryScope, managerRetryScope)) {
        markManagerLookupAborted();
        return CodecResolution.RETRYABLE;
      }
      DataTypeCodec currentCodec = super.getCodec(dataTypeId);
      if (currentCodec == null) {
        return CodecResolution.RETRYABLE;
      }
      if (!dataTypeId.equals(super.getDataTypeId(dataTypeId))) {
        return isTemporarilyRegistered(dataTypeId)
            ? CodecResolution.RETRYABLE
            : CodecResolution.FAILED;
      }
      if (temporaryTypes.containsKey(dataTypeId)) {
        return CodecResolution.existing(currentCodec);
      }

      if (!hasAnyEncoding(dataTypeId)) {
        CodecResolution encodingCollision =
            checkEncodingCollision(dataTypeId, binaryEncodingId, xmlEncodingId, jsonEncodingId);
        if (encodingCollision != null) {
          return encodingCollision;
        }
        super.registerType(
            dataTypeId, currentCodec, binaryEncodingId, xmlEncodingId, jsonEncodingId);
        return CodecResolution.resolved(currentCodec);
      }

      if (requestedEncodingId == null || super.getCodec(requestedEncodingId) != null) {
        return CodecResolution.existing(currentCodec);
      }

      // `dataTypeId` already has partial encoding(s) (e.g. XML-only). Verify which missing slot
      // matches `requestedEncodingId` and install it while preserving the application codec and
      // all existing encodings.
      NodeId existingBinary = super.getBinaryEncodingId(dataTypeId);
      NodeId existingXml = super.getXmlEncodingId(dataTypeId);
      NodeId existingJson = super.getJsonEncodingId(dataTypeId);

      NodeId newBinary =
          existingBinary == null && requestedEncodingId.equals(binaryEncodingId)
              ? binaryEncodingId
              : null;
      NodeId newXml =
          existingXml == null && requestedEncodingId.equals(xmlEncodingId) ? xmlEncodingId : null;
      NodeId newJson =
          existingJson == null && requestedEncodingId.equals(jsonEncodingId)
              ? jsonEncodingId
              : null;

      if (newBinary == null && newXml == null && newJson == null) {
        return CodecResolution.existing(currentCodec);
      }

      CodecResolution encodingCollision =
          checkEncodingCollision(dataTypeId, newBinary, newXml, newJson);
      if (encodingCollision != null) {
        return encodingCollision;
      }

      NodeId finalBinary =
          newBinary != null
              ? newBinary
              : preservePermanentEncoding(
                  dataTypeId, currentCodec, existingBinary, temporaryBinaryEncodings);
      NodeId finalXml =
          newXml != null
              ? newXml
              : preservePermanentEncoding(
                  dataTypeId, currentCodec, existingXml, temporaryXmlEncodings);
      NodeId finalJson =
          newJson != null
              ? newJson
              : preservePermanentEncoding(
                  dataTypeId, currentCodec, existingJson, temporaryJsonEncodings);

      super.registerType(dataTypeId, currentCodec, finalBinary, finalXml, finalJson);
      return CodecResolution.resolved(currentCodec);
    }
  }

  private @Nullable NodeId preservePermanentEncoding(
      NodeId dataTypeId,
      DataTypeCodec currentTypeCodec,
      @Nullable NodeId existingEncodingId,
      Map<NodeId, TemporaryRegistration> temporaryForwardMap) {
    if (existingEncodingId == null
        || temporaryForwardMap.containsKey(dataTypeId)
        || temporaryReverseEncodings.containsKey(existingEncodingId)
        || !dataTypeId.equals(super.getDataTypeId(existingEncodingId))
        || super.getCodec(existingEncodingId) != currentTypeCodec) {
      return null;
    }
    return existingEncodingId;
  }

  private record EncodingIdLookup(
      @Nullable NodeId encodingId, @Nullable CodecResolution deferred) {}

  private EncodingIdLookup readBinaryEncodingId(DataType dataType) {
    EncodingIdLookup direct = readEncodingId(dataType::getBinaryEncodingId);
    if (direct.deferred() != null || direct.encodingId() != null) {
      return direct;
    }
    return readEncodingId(
        () ->
            dataType.getDataTypeDefinition() instanceof StructureDefinition definition
                ? definition.getDefaultEncodingId()
                : null);
  }

  private EncodingIdLookup readEncodingId(Supplier<@Nullable NodeId> getter) {
    LazyClientDataTypeTree.RetryScope getterTreeScope = LazyClientDataTypeTree.openRetryScope();
    ManagerRetryScope getterManagerScope = openManagerRetryScope();
    boolean propagate = true;
    try {
      NodeId encodingId = getter.get();
      if (isAborted(getterTreeScope, getterManagerScope)) {
        return new EncodingIdLookup(null, CodecResolution.RETRYABLE);
      }
      if ((encodingId == null || encodingId.isNull())
          && isRetryable(getterTreeScope, getterManagerScope)) {
        return new EncodingIdLookup(null, CodecResolution.REENTRANT);
      }
      propagate = false;
      return new EncodingIdLookup(encodingId, null);
    } finally {
      getterManagerScope.close(propagate);
      getterTreeScope.close(propagate);
    }
  }

  private ExistingEncodingLookup checkExistingEncoding(
      NodeId dataTypeId, Function<NodeId, @Nullable NodeId> getter) {
    synchronized (this) {
      NodeId encodingId = getter.apply(dataTypeId);
      if (encodingId != null) {
        return new ExistingEncodingLookup(ExistingRegistration.REGISTERED, encodingId, false);
      }
      if (hasAnyEncoding(dataTypeId)) {
        return new ExistingEncodingLookup(
            ExistingRegistration.REGISTERED,
            null,
            temporaryTypes.containsKey(dataTypeId) || hasTemporaryForwardEncoding(dataTypeId));
      }
      if (super.getCodec(dataTypeId) != null) {
        if (dataTypeId.equals(super.getDataTypeId(dataTypeId))) {
          return temporaryTypes.containsKey(dataTypeId)
              ? new ExistingEncodingLookup(ExistingRegistration.REGISTERED, null, true)
              : new ExistingEncodingLookup(
                  ExistingRegistration.PERMANENT_WITHOUT_ENCODINGS, null, false);
        }
        return new ExistingEncodingLookup(
            isTemporarilyRegistered(dataTypeId)
                ? ExistingRegistration.TEMPORARY_COLLISION
                : ExistingRegistration.PERMANENT_COLLISION,
            null,
            false);
      }
      return new ExistingEncodingLookup(ExistingRegistration.NONE, null, false);
    }
  }

  private boolean hasAnyEncoding(NodeId dataTypeId) {
    return super.getBinaryEncodingId(dataTypeId) != null
        || super.getXmlEncodingId(dataTypeId) != null
        || super.getJsonEncodingId(dataTypeId) != null;
  }

  private boolean hasAllEncodings(NodeId dataTypeId) {
    return super.getBinaryEncodingId(dataTypeId) != null
        && super.getXmlEncodingId(dataTypeId) != null
        && super.getJsonEncodingId(dataTypeId) != null;
  }

  private boolean hasTemporaryForwardEncoding(NodeId dataTypeId) {
    return temporaryBinaryEncodings.containsKey(dataTypeId)
        || temporaryXmlEncodings.containsKey(dataTypeId)
        || temporaryJsonEncodings.containsKey(dataTypeId);
  }

  private boolean isPermanentRegistrationMissingEncoding(
      NodeId dataTypeId, @Nullable NodeId requestedEncodingId) {
    synchronized (this) {
      if (temporaryTypes.containsKey(dataTypeId)
          || super.getCodec(dataTypeId) == null
          || !dataTypeId.equals(super.getDataTypeId(dataTypeId))) {
        return false;
      }
      if (!hasAnyEncoding(dataTypeId)) {
        return true;
      }
      return requestedEncodingId != null
          && !hasAllEncodings(dataTypeId)
          && super.getCodec(requestedEncodingId) == null;
    }
  }

  private @Nullable CodecResolution lookupExistingCanonicalRegistration(NodeId dataTypeId) {
    synchronized (this) {
      DataTypeCodec existing = super.getCodec(dataTypeId);
      if (existing == null) {
        return null;
      }
      // Verify `dataTypeId` is actually registered as a canonical DataTypeId rather than merely as
      // an encoding id owned by another type (for example via an active acquireType() handle).
      if (dataTypeId.equals(super.getDataTypeId(dataTypeId))) {
        return CodecResolution.existing(existing);
      }
      return isTemporarilyRegistered(dataTypeId)
          ? CodecResolution.RETRYABLE
          : CodecResolution.FAILED;
    }
  }

  private @Nullable CodecResolution checkEncodingCollision(
      @Nullable NodeId allowedSelfId,
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {
    CollisionKind binary = encodingCollisionKind(allowedSelfId, binaryEncodingId);
    CollisionKind xml = encodingCollisionKind(allowedSelfId, xmlEncodingId);
    CollisionKind json = encodingCollisionKind(allowedSelfId, jsonEncodingId);

    if (binary == CollisionKind.PERMANENT
        || xml == CollisionKind.PERMANENT
        || json == CollisionKind.PERMANENT) {
      return CodecResolution.FAILED;
    }
    if (binary == CollisionKind.TEMPORARY
        || xml == CollisionKind.TEMPORARY
        || json == CollisionKind.TEMPORARY) {
      return CodecResolution.RETRYABLE;
    }
    return null;
  }

  private CollisionKind encodingCollisionKind(@Nullable NodeId allowedSelfId, @Nullable NodeId id) {
    if (id == null || id.isNull() || id.equals(allowedSelfId) || super.getCodec(id) == null) {
      return CollisionKind.NONE;
    }
    return isTemporarilyRegistered(id) ? CollisionKind.TEMPORARY : CollisionKind.PERMANENT;
  }

  private boolean isTemporarilyRegistered(NodeId id) {
    if (temporaryReverseEncodings.containsKey(id)) {
      return true;
    }
    NodeId owner = super.getDataTypeId(id);
    return id.equals(owner) && temporaryTypes.containsKey(id);
  }

  private void removeTemporaryReverseEncoding(
      @Nullable NodeId encodingId, TemporaryRegistration tracked) {
    if (encodingId != null) {
      temporaryReverseEncodings.remove(encodingId, tracked);
    }
  }

  private void removeAttemptedResolution(@Nullable NodeId id) {
    NodeId normalized = normalizeEncodingId(id);
    if (normalized != null) {
      attemptedResolution.remove(normalized);
    }
  }

  private static @Nullable NodeId normalizeEncodingId(@Nullable NodeId id) {
    return id == null || id.isNull() ? null : id;
  }

  private static boolean isRetryable(
      LazyClientDataTypeTree.RetryScope treeRetryScope, ManagerRetryScope managerRetryScope) {
    return Thread.currentThread().isInterrupted()
        || treeRetryScope.isRetryable()
        || managerRetryScope.isRetryable();
  }

  private static boolean isAborted(
      LazyClientDataTypeTree.RetryScope treeRetryScope, ManagerRetryScope managerRetryScope) {
    return Thread.currentThread().isInterrupted()
        || treeRetryScope.isAborted()
        || managerRetryScope.isAborted();
  }

  private @Nullable NodeId browseDataTypeIdForEncoding(NodeId encodingId) throws UaException {
    try {
      BrowseDescription bd =
          new BrowseDescription(
              encodingId,
              BrowseDirection.Inverse,
              NodeIds.HasEncoding,
              false,
              uint(NodeClass.DataType.getValue()),
              uint(BrowseResultMask.All.getValue()));

      BrowseResult result = client.browse(bd);

      if (result.getStatusCode().isGood()
          && result.getReferences() != null
          && result.getReferences().length > 0) {

        return result
            .getReferences()[0]
            .getNodeId()
            .toNodeId(client.getNamespaceTable())
            .orElse(null);
      }

      LOGGER.debug("No DataType found via inverse HasEncoding for encoding {}", encodingId);
    } catch (UaException e) {
      if (ClientBrowseUtils.hasInterruptedCause(e)) {
        throw e;
      }
      LOGGER.debug("Failed to browse DataType for encoding {}: {}", encodingId, e.getMessage());
    }

    return null;
  }
}

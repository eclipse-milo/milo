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

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
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
 * unless {@link #clearFailedResolutions()} is called.
 */
public class LazyClientDataTypeManager extends DefaultDataTypeManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(LazyClientDataTypeManager.class);

  private final OpcUaClient client;
  private final DataTypeTree dataTypeTree;
  private final BiFunction<DataType, DataTypeTree, DataTypeCodec> codecFactory;
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final Set<NodeId> attemptedResolution = ConcurrentHashMap.newKeySet();

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
  public void clearFailedResolutions() {
    lock.writeLock().lock();
    try {
      attemptedResolution.clear();
    } finally {
      lock.writeLock().unlock();
    }
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

    // Slow path: attempt lazy resolution under write lock
    lock.writeLock().lock();
    try {
      // Re-check in case another thread just resolved it
      codec = super.getCodec(id);
      if (codec != null) {
        return codec;
      }

      // Mark as attempted before trying resolution
      if (!attemptedResolution.add(id)) {
        return null;
      }

      return resolveAndRegisterCodec(id);
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public @Nullable NodeId getBinaryEncodingId(NodeId dataTypeId) {
    ensureRegisteredForDataType(dataTypeId);
    return super.getBinaryEncodingId(dataTypeId);
  }

  @Override
  public @Nullable NodeId getXmlEncodingId(NodeId dataTypeId) {
    ensureRegisteredForDataType(dataTypeId);
    return super.getXmlEncodingId(dataTypeId);
  }

  @Override
  public @Nullable NodeId getJsonEncodingId(NodeId dataTypeId) {
    ensureRegisteredForDataType(dataTypeId);
    return super.getJsonEncodingId(dataTypeId);
  }

  private void ensureRegisteredForDataType(NodeId dataTypeId) {
    // Skip namespace 0 types
    if (dataTypeId.getNamespaceIndex().intValue() == 0) {
      return;
    }

    // Fast path: already registered
    if (super.getBinaryEncodingId(dataTypeId) != null
        || super.getXmlEncodingId(dataTypeId) != null
        || super.getJsonEncodingId(dataTypeId) != null) {
      return;
    }

    // Skip if already attempted
    if (attemptedResolution.contains(dataTypeId)) {
      return;
    }

    // Slow path: attempt resolution under write lock
    lock.writeLock().lock();
    try {
      // Re-check after acquiring lock
      if (attemptedResolution.contains(dataTypeId)) {
        return;
      }

      attemptedResolution.add(dataTypeId);
      resolveAndRegisterCodecFromDataTypeId(dataTypeId);
    } finally {
      lock.writeLock().unlock();
    }
  }

  private @Nullable DataTypeCodec resolveAndRegisterCodec(NodeId id) {
    try {
      // First, try to treat `id` as a DataType NodeId
      DataType dataType = dataTypeTree.getDataType(id);

      if (dataType == null || dataType.getDataTypeDefinition() == null) {
        // Otherwise, treat `id` as an encoding NodeId and discover its DataType
        NodeId dataTypeId = browseDataTypeIdForEncoding(id);
        if (dataTypeId == null) {
          LOGGER.debug("No DataType found for encoding {}", id);
          return null;
        }

        dataType = dataTypeTree.getDataType(dataTypeId);
      }

      if (dataType == null || dataType.getDataTypeDefinition() == null) {
        LOGGER.debug("No DataTypeDefinition available for {}", id);
        return null;
      }

      // Create the codec and register it
      return createAndRegisterCodec(dataType);
    } catch (Exception e) {
      LOGGER.debug("Error resolving codec for {}: {}", id, e.getMessage());
      return null;
    }
  }

  private void resolveAndRegisterCodecFromDataTypeId(NodeId dataTypeId) {
    try {
      DataType dataType = dataTypeTree.getDataType(dataTypeId);

      if (dataType != null && dataType.getDataTypeDefinition() != null) {
        createAndRegisterCodec(dataType);
      }
    } catch (Exception e) {
      LOGGER.debug("Error resolving codec for DataType {}: {}", dataTypeId, e.getMessage());
    }
  }

  private @Nullable DataTypeCodec createAndRegisterCodec(DataType dataType) {
    NodeId binaryEncodingId = getBinaryEncodingIdFromDataType(dataType);

    DataTypeCodec codec = codecFactory.apply(dataType, dataTypeTree);

    super.registerType(
        dataType.getNodeId(),
        codec,
        binaryEncodingId,
        dataType.getXmlEncodingId(),
        dataType.getJsonEncodingId());

    LOGGER.debug(
        "Lazily registered codec for: name={}, dataTypeId={}",
        dataType.getBrowseName(),
        dataType.getNodeId());

    return codec;
  }

  private static @Nullable NodeId getBinaryEncodingIdFromDataType(DataType dataType) {
    NodeId binaryEncodingId = dataType.getBinaryEncodingId();

    if (binaryEncodingId == null
        && dataType.getDataTypeDefinition() instanceof StructureDefinition definition) {

      // Workaround for non-compliant servers that don't have encoding nodes.
      // The DefaultEncodingId in a StructureDefinition shall always be the Default Binary
      // encoding.
      // See https://reference.opcfoundation.org/Core/Part3/v105/docs/8.48
      binaryEncodingId = definition.getDefaultEncodingId();
    }

    return binaryEncodingId;
  }

  private @Nullable NodeId browseDataTypeIdForEncoding(NodeId encodingId) {
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
      LOGGER.debug("Failed to browse DataType for encoding {}: {}", encodingId, e.getMessage());
    }

    return null;
  }
}

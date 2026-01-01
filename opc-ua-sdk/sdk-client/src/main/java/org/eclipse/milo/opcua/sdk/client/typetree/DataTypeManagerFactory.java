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

import org.eclipse.milo.opcua.sdk.client.CodecFactory;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A factory that creates a {@link DataTypeManager} for a given {@link OpcUaClient}.
 *
 * <p>Implementations can create different types of DataTypeManagers, such as:
 *
 * <ul>
 *   <li>An eagerly initialized manager via {@link DefaultDataTypeManager#createAndInitialize}
 *   <li>A lazily-loading manager via {@link LazyClientDataTypeManager}
 * </ul>
 *
 * @see DefaultDataTypeManager
 * @see LazyClientDataTypeManager
 */
@FunctionalInterface
public interface DataTypeManagerFactory {

  /**
   * Creates a {@link DataTypeManager} for the given client.
   *
   * @param client the {@link OpcUaClient} to create a DataTypeManager for.
   * @param namespaceTable the server's {@link NamespaceTable}.
   * @param dataTypeTree the {@link DataTypeTree} to use for type resolution.
   * @return a new {@link DataTypeManager} instance.
   * @throws UaException if an error occurs while creating the manager.
   */
  DataTypeManager create(
      OpcUaClient client, NamespaceTable namespaceTable, DataTypeTree dataTypeTree)
      throws UaException;

  /**
   * Returns the default factory that eagerly initializes a {@link DataTypeManager} using {@link
   * DefaultDataTypeManager#createAndInitialize(NamespaceTable)}.
   *
   * <p>The eager factory creates and initializes all codecs for known structure types in the {@link
   * DataTypeTree} at creation time, using {@link DynamicCodecFactory} and the default initializer.
   *
   * @return the default {@link DataTypeManagerFactory}.
   */
  static DataTypeManagerFactory eager() {
    return eager(new DefaultInitializer());
  }

  /**
   * Returns an eager factory that uses a {@link DefaultInitializer} with the provided {@link
   * CodecFactory}.
   *
   * <p>The eager factory creates and initializes all codecs for known structure types in the {@link
   * DataTypeTree} at creation time.
   *
   * @param codecFactory the {@link CodecFactory} to use when creating codecs.
   * @return a {@link DataTypeManagerFactory} that eagerly initializes types.
   */
  static DataTypeManagerFactory eager(CodecFactory codecFactory) {
    return eager(new DefaultInitializer(codecFactory));
  }

  /**
   * Returns an eager factory that uses the provided {@link Initializer}.
   *
   * <p>The eager factory creates and initializes all codecs for known structure types in the {@link
   * DataTypeTree} at creation time.
   *
   * @param initializer the {@link Initializer} to use for registering codecs.
   * @return a {@link DataTypeManagerFactory} that eagerly initializes types.
   */
  static DataTypeManagerFactory eager(Initializer initializer) {
    return (client, namespaceTable, dataTypeTree) -> {
      DataTypeManager dataTypeManager = DefaultDataTypeManager.createAndInitialize(namespaceTable);
      initializer.initialize(namespaceTable, dataTypeTree, dataTypeManager);
      return dataTypeManager;
    };
  }

  /**
   * Returns a factory that creates a {@link LazyClientDataTypeManager}.
   *
   * <p>The lazy factory creates a manager that resolves codecs on demand when they are first
   * requested, rather than eagerly registering all codecs at creation time. Uses {@link
   * DynamicCodecFactory} by default.
   *
   * @return a {@link DataTypeManagerFactory} that creates lazy-loading managers.
   */
  static DataTypeManagerFactory lazy() {
    return lazy(DynamicCodecFactory::create);
  }

  /**
   * Returns a factory that creates a {@link LazyClientDataTypeManager} with a custom {@link
   * CodecFactory}.
   *
   * <p>The lazy factory creates a manager that resolves codecs on demand when they are first
   * requested, rather than eagerly registering all codecs at creation time.
   *
   * @param codecFactory the {@link CodecFactory} to use when creating codecs on demand.
   * @return a {@link DataTypeManagerFactory} that creates lazy-loading managers.
   */
  static DataTypeManagerFactory lazy(CodecFactory codecFactory) {
    return (client, namespaceTable, dataTypeTree) ->
        new LazyClientDataTypeManager(client, namespaceTable, dataTypeTree, codecFactory::create);
  }

  /**
   * An initializer that registers codecs for custom data types with a {@link DataTypeManager}.
   *
   * <p>Implementations traverse the {@link DataTypeTree} and register codecs for structure types.
   */
  interface Initializer {

    /**
     * Register codecs for custom data types.
     *
     * @param namespaceTable the Server's {@link NamespaceTable}.
     * @param dataTypeTree the Client's {@link DataTypeTree}.
     * @param dataTypeManager the {@link DataTypeManager} to register codecs with.
     * @throws UaException if an error occurs during initialization.
     */
    void initialize(
        NamespaceTable namespaceTable, DataTypeTree dataTypeTree, DataTypeManager dataTypeManager)
        throws UaException;
  }

  /**
   * The default {@link Initializer} implementation that traverses the DataType hierarchy and
   * registers codecs for all structure types with a {@link DataTypeManager}.
   *
   * <p>Uses {@link DynamicCodecFactory} by default, but can be configured with a custom {@link
   * CodecFactory}.
   */
  class DefaultInitializer implements Initializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInitializer.class);

    private final CodecFactory codecFactory;

    /** Create a new {@link DefaultInitializer} that uses {@link DynamicCodecFactory}. */
    public DefaultInitializer() {
      this(DynamicCodecFactory::create);
    }

    /**
     * Create a new {@link DefaultInitializer} using the provided {@link CodecFactory}.
     *
     * @param codecFactory the {@link CodecFactory} to use when creating codecs.
     */
    public DefaultInitializer(CodecFactory codecFactory) {
      this.codecFactory = codecFactory;
    }

    @Override
    public void initialize(
        NamespaceTable namespaceTable, DataTypeTree dataTypeTree, DataTypeManager dataTypeManager) {

      Tree<DataType> structureNode = dataTypeTree.getTreeNode(NodeIds.Structure);

      if (structureNode != null) {
        structureNode.traverse(
            dataType -> {
              if (dataType.getDataTypeDefinition() != null) {
                LOGGER.debug(
                    "Registering type: name={}, dataTypeId={}",
                    dataType.getBrowseName(),
                    dataType.getNodeId());

                String namespaceUri = namespaceTable.get(dataType.getNodeId().getNamespaceIndex());

                if (namespaceUri == null) {
                  throw new UaRuntimeException(
                      StatusCodes.Bad_UnexpectedError,
                      "DataType namespace not registered: "
                          + dataType.getNodeId().toParseableString());
                }

                dataTypeManager.registerType(
                    dataType.getNodeId(),
                    codecFactory.create(dataType, dataTypeTree),
                    getBinaryEncodingId(dataType),
                    dataType.getXmlEncodingId(),
                    dataType.getJsonEncodingId());
              }
            });
      } else {
        LOGGER.warn(
            "Structure (i=22) not found in the DataType tree; is the Server's DataType"
                + " hierarchy sane?");
      }
    }

    private static NodeId getBinaryEncodingId(DataType dataType) {
      NodeId binaryEncodingId = dataType.getBinaryEncodingId();

      if (binaryEncodingId == null
          && dataType.getDataTypeDefinition() instanceof StructureDefinition definition) {

        // Hail mary work around for non-compliant Servers that don't have encoding nodes
        // in their address space. The DefaultEncodingId in a StructureDefinition shall
        // always be the Default Binary encoding, so let's see if the Server at least set
        // this correctly.
        // See https://reference.opcfoundation.org/Core/Part3/v105/docs/8.48

        binaryEncodingId = definition.getDefaultEncodingId();
      }

      return binaryEncodingId;
    }
  }
}

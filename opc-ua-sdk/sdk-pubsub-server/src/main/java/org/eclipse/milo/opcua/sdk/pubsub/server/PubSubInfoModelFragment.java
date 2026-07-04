/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.udp.UdpTransportProvider;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpaceFragmentWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NetworkAddressUrlTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishSubscribeType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedDataItemsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TargetVariablesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exposes the PublishSubscribe information model for an attached PubSub runtime: populates and
 * animates the ns0 PublishSubscribe subtree and grafts connection, group, writer, reader, and
 * published-dataset objects reflecting the current configuration.
 *
 * <p>The fragment is a {@link ManagedAddressSpaceFragmentWithLifecycle} with its own {@code
 * UaNodeManager} and {@link SubscriptionModel}, registered with the server's {@code
 * AddressSpaceManager} on startup and unregistered on shutdown. References grafting the new
 * subtrees onto ns0 nodes (PublishSubscribe {@code i=14443} via HasPubSubConnection, the
 * PublishedDataSets folder {@code i=17371} via HasComponent) are stored, with their inverses, in
 * this fragment's node manager only; ns0's node manager is never structurally modified.
 *
 * <p>Node identity: every node created by this fragment has a deterministic string {@link NodeId}
 * per {@link PubSubNodeIds} (pinned decision R11), stable across server restarts for an unchanged
 * configuration and across rebuilds for restarted components — the path-stability guarantee the
 * diagnostics exposure relies on ({@code "PubSub/<path>/Diagnostics/..."} ids never change while
 * the component keeps its name path), matching the engine's path-stable counter preservation.
 *
 * <p>All variables are read-only ({@code AccessLevel.CurrentRead}, the {@code UaVariableNode}
 * default). Method nodes are created only when {@link
 * ServerPubSubOptions#isAllowRemoteConfiguration()} is {@code true}: an {@code Enable} and {@code
 * Disable} pair on every component Status object (see {@link PubSubStatusMethods}). The ns0 root
 * Status object ({@code i=17405}) never gets the pair — its Enable/Disable members are Optional,
 * the Foundation NodeSet omits them, and Call dispatch routes by objectId to the ns0 namespace,
 * which cannot see fragment-held method nodes (D23). Pre-existing ns0 method nodes (AddConnection,
 * RemoveConnection, the SKS methods, ...) and unbacked optional ns0 components (Diagnostics,
 * PubSubConfiguration, PubSubCapablities, ...) are left exactly as the ns0 loader created them.
 * PublishedDataSet nodes carry no Status child (Part 14 defines none); published-dataset runtime
 * state is not surfaced.
 *
 * <p>Values are populated from the current {@link PubSubConfig}, normalized through {@code
 * PubSubConfig.toDataType} so defaults, transport profile URIs, and derived DataSetMetaData match
 * what the runtime publishes. Existing ns0 children of PublishSubscribe (Status/State {@code
 * i=17406}, SupportedTransportProfiles {@code i=17481}, ConfigurationVersion, and
 * ConfigurationProperties) are populated by looking up the existing nodes and setting values only;
 * property create-on-set against ns0 parents is never triggered. The ConfigurationVersion value is
 * read from the mediator-owned single source (D26), never a locally sampled clock.
 *
 * <p>Live state: component Status/State variables are updated from {@link
 * PubSubService#addStateListener}, keyed by component name path so that reconfiguration (which
 * invalidates handles) does not break tracking; reader DataSetMetaData values are updated from
 * {@link PubSubService#addMetaDataListener}. A state event emitted before a build's {@code
 * initialState} read can be delivered after it, leaving a stale State value until the next
 * transition — display-only and self-correcting; no locking against the event dispatcher.
 *
 * <p>Reconfiguration: {@link #applyReconfigure} — driven as the first {@link ManagedPubSubService}
 * post-apply hook — incrementally deletes and rebuilds the config-derived subtrees affected by a
 * successful apply (R10), keyed by name path, so {@code ServerPubSub.runtime()} reconfigures no
 * longer desync the exposed model. {@link ComponentNodeListener}s observe the per-component subtree
 * builds and removals.
 *
 * <p>Created by {@link ServerPubSub} when {@link ServerPubSubOptions#isExposeInformationModel()} is
 * {@code true}; {@link #startup()} and {@link #shutdown()} are driven by the owning {@link
 * ServerPubSub}'s lifecycle.
 */
final class PubSubInfoModelFragment extends ManagedAddressSpaceFragmentWithLifecycle
    implements ConfigurationObjectIds {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubInfoModelFragment.class);

  private final AddressSpaceFilter filter =
      SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);

  private final SubscriptionModel subscriptionModel;

  /**
   * Status/State variable nodes, keyed by component name path, e.g. {@code "conn/group/writer"}.
   */
  private final Map<String, UaVariableNode> stateVariables = new ConcurrentHashMap<>();

  /** Reader DataSetMetaData property nodes, keyed by reader name path. */
  private final Map<String, UaVariableNode> metaDataVariables = new ConcurrentHashMap<>();

  /**
   * Guards listener callbacks; set on startup, cleared on shutdown. Listeners cannot be removed.
   */
  private volatile boolean active = false;

  /**
   * The {@link ComponentNodeListener}s observing per-component subtree builds and removals.
   * Registration must complete before {@code ServerPubSub.startup()}.
   */
  private final List<ComponentNodeListener> componentNodeListeners = new CopyOnWriteArrayList<>();

  /** The configuration the exposed model reflects; swapped by {@link #applyReconfigure}. */
  private volatile PubSubConfig config;

  private final PubSubService service;
  private final boolean allowRemoteConfiguration;
  private final PubSubMethodAuthorizer authorizer;
  private final Supplier<UInteger> configurationVersion;
  private final UShort namespaceIndex;

  /**
   * Create a fragment exposing {@code config}.
   *
   * @param authorizer the effective {@link PubSubMethodAuthorizer} resolved by {@link
   *     ServerPubSub}, consulted by the Enable/Disable handlers.
   * @param configurationVersion the mediator-owned ConfigurationVersion single source (D26), read
   *     for the ns0 {@code ConfigurationVersion} property at startup and per apply. Deferred: only
   *     invoked once the owning {@link ServerPubSub}'s construction has completed.
   */
  PubSubInfoModelFragment(
      OpcUaServer server,
      PubSubConfig config,
      PubSubService service,
      ServerPubSubOptions options,
      PubSubMethodAuthorizer authorizer,
      Supplier<UInteger> configurationVersion) {

    super(server);

    this.config = config;
    this.service = service;
    this.allowRemoteConfiguration = options.isAllowRemoteConfiguration();
    this.authorizer = authorizer;
    this.configurationVersion = configurationVersion;

    namespaceIndex = server.getServerNamespace().getNamespaceIndex();

    subscriptionModel = new SubscriptionModel(server, this);
    getLifecycleManager().addLifecycle(subscriptionModel);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                // read the FIELD, not the shadowing constructor parameter: a reconfigure applied
                // between attach and startup swaps the field (applyReconfigure's inactive path),
                // and startup must build the configuration the engine actually runs
                PubSubConfiguration2DataType configuration =
                    PubSubInfoModelFragment.this.config.toDataType(getServer().getNamespaceTable());

                buildNodes(configuration);
                populateExistingNs0Nodes(configuration);

                active = true;
                registerListeners();
              }

              @Override
              public void shutdown() {
                active = false;
                setRootState(PubSubState.Disabled);
              }
            });
  }

  @Override
  public AddressSpaceFilter getFilter() {
    return filter;
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

  // region node construction

  private void buildNodes(PubSubConfiguration2DataType configuration) {
    PublishedDataSetDataType[] publishedDataSets =
        orEmpty(configuration.getPublishedDataSets(), PublishedDataSetDataType[]::new);

    for (PublishedDataSetDataType dataSet : publishedDataSets) {
      buildPublishedDataSetNodes(dataSet);
    }

    PubSubConnectionDataType[] connections =
        orEmpty(configuration.getConnections(), PubSubConnectionDataType[]::new);

    for (PubSubConnectionDataType connection : connections) {
      buildConnectionNodes(connection);
    }
  }

  private void buildPublishedDataSetNodes(PublishedDataSetDataType dataSet) {
    String name = nullToEmpty(dataSet.getName());

    NodeId nodeId = PubSubNodeIds.publishedDataSetNodeId(namespaceIndex, name);

    PublishedDataItemsTypeNode node =
        addObjectNode(
            PublishedDataItemsTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.PublishedDataItemsType,
            NodeIds.HasComponent,
            NodeIds.PublishSubscribe_PublishedDataSets);

    DataSetMetaDataType metaData = dataSet.getDataSetMetaData();

    addPropertyNode(
        node,
        "ConfigurationVersion",
        NodeIds.ConfigurationVersionDataType,
        ValueRanks.Scalar,
        new Variant(metaData != null ? metaData.getConfigurationVersion() : null));

    addPropertyNode(
        node,
        "DataSetMetaData",
        NodeIds.DataSetMetaDataType,
        ValueRanks.Scalar,
        new Variant(metaData));

    PublishedVariableDataType[] publishedData =
        dataSet.getDataSetSource() instanceof PublishedDataItemsDataType items
            ? orEmpty(items.getPublishedData(), PublishedVariableDataType[]::new)
            : new PublishedVariableDataType[0];

    addPropertyNode(
        node,
        "PublishedData",
        NodeIds.PublishedVariableDataType,
        ValueRanks.OneDimension,
        new Variant(publishedData));

    UUID dataSetClassId = metaData != null ? metaData.getDataSetClassId() : null;
    if (dataSetClassId != null
        && (dataSetClassId.getMostSignificantBits() != 0L
            || dataSetClassId.getLeastSignificantBits() != 0L)) {

      addPropertyNode(
          node, "DataSetClassId", NodeIds.Guid, ValueRanks.Scalar, new Variant(dataSetClassId));
    }
  }

  private void buildConnectionNodes(PubSubConnectionDataType connection) {
    String name = nullToEmpty(connection.getName());

    NodeId nodeId = PubSubNodeIds.componentNodeId(namespaceIndex, name);

    PubSubConnectionTypeNode node =
        addObjectNode(
            PubSubConnectionTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.PubSubConnectionType,
            NodeIds.HasPubSubConnection,
            NodeIds.PublishSubscribe);

    Variant publisherId =
        connection.getPublisherId() != null ? connection.getPublisherId() : Variant.NULL_VALUE;

    addPropertyNode(node, "PublisherId", NodeIds.BaseDataType, ValueRanks.Scalar, publisherId);

    addSelectionListNode(
        node,
        "TransportProfileUri",
        connection.getTransportProfileUri(),
        new String[] {UdpTransportProvider.TRANSPORT_PROFILE_URI});

    addPropertyNode(
        node,
        "ConnectionProperties",
        NodeIds.KeyValuePair,
        ValueRanks.OneDimension,
        new Variant(orEmpty(connection.getConnectionProperties(), KeyValuePair[]::new)));

    buildAddressNodes(node, connection.getAddress());

    addStatusNodes(node, name, () -> service.components().connection(name));

    WriterGroupDataType[] writerGroups =
        orEmpty(connection.getWriterGroups(), WriterGroupDataType[]::new);

    for (WriterGroupDataType group : writerGroups) {
      buildWriterGroupNodes(nodeId, name, group);
    }

    ReaderGroupDataType[] readerGroups =
        orEmpty(connection.getReaderGroups(), ReaderGroupDataType[]::new);

    for (ReaderGroupDataType group : readerGroups) {
      buildReaderGroupNodes(nodeId, name, group);
    }

    notifyBuilt(ComponentType.CONNECTION, name, node);
  }

  /**
   * Build the Mandatory Address child as a concrete NetworkAddressUrlType instance with Url and
   * NetworkInterface variables (the declared NetworkAddressType is abstract and has no Url).
   */
  private void buildAddressNodes(
      PubSubConnectionTypeNode connectionNode, @Nullable NetworkAddressDataType address) {

    NetworkAddressUrlTypeNode addressNode =
        addObjectNode(
            NetworkAddressUrlTypeNode::new,
            childNodeId(connectionNode, "Address"),
            new QualifiedName(0, "Address"),
            NodeIds.NetworkAddressUrlType,
            NodeIds.HasComponent,
            connectionNode.getNodeId());

    String networkInterface = address != null ? nullToEmpty(address.getNetworkInterface()) : "";

    addSelectionListNode(addressNode, "NetworkInterface", networkInterface, new String[0]);

    String url =
        address instanceof NetworkAddressUrlDataType urlAddress ? urlAddress.getUrl() : null;

    addVariableNode(addressNode, "Url", NodeIds.String, new Variant(url));
  }

  private void buildWriterGroupNodes(
      NodeId connectionNodeId, String connectionName, WriterGroupDataType group) {

    String name = nullToEmpty(group.getName());
    String path = connectionName + "/" + name;

    NodeId nodeId = PubSubNodeIds.componentNodeId(namespaceIndex, path);

    WriterGroupTypeNode node =
        addObjectNode(
            WriterGroupTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.WriterGroupType,
            NodeIds.HasWriterGroup,
            connectionNodeId);

    addGroupPropertyNodes(node, group);

    addPropertyNode(
        node,
        "WriterGroupId",
        NodeIds.UInt16,
        ValueRanks.Scalar,
        new Variant(group.getWriterGroupId()));

    addPropertyNode(
        node,
        "PublishingInterval",
        NodeIds.Duration,
        ValueRanks.Scalar,
        new Variant(group.getPublishingInterval()));

    addPropertyNode(
        node,
        "KeepAliveTime",
        NodeIds.Duration,
        ValueRanks.Scalar,
        new Variant(group.getKeepAliveTime()));

    addPropertyNode(
        node, "Priority", NodeIds.Byte, ValueRanks.Scalar, new Variant(group.getPriority()));

    addPropertyNode(
        node,
        "LocaleIds",
        NodeIds.LocaleId,
        ValueRanks.OneDimension,
        new Variant(orEmpty(group.getLocaleIds(), String[]::new)));

    addPropertyNode(
        node,
        "HeaderLayoutUri",
        NodeIds.String,
        ValueRanks.Scalar,
        new Variant(nullToEmpty(group.getHeaderLayoutUri())));

    addStatusNodes(node, path, () -> service.components().writerGroup(connectionName, name));

    if (group.getMessageSettings() instanceof UadpWriterGroupMessageDataType uadp) {
      UadpWriterGroupMessageTypeNode messageSettings =
          addObjectNode(
              UadpWriterGroupMessageTypeNode::new,
              childNodeId(node, "MessageSettings"),
              new QualifiedName(0, "MessageSettings"),
              NodeIds.UadpWriterGroupMessageType,
              NodeIds.HasComponent,
              nodeId);

      addPropertyNode(
          messageSettings,
          "GroupVersion",
          NodeIds.VersionTime,
          ValueRanks.Scalar,
          new Variant(uadp.getGroupVersion()));

      addPropertyNode(
          messageSettings,
          "DataSetOrdering",
          NodeIds.DataSetOrderingType,
          ValueRanks.Scalar,
          new Variant(uadp.getDataSetOrdering()));

      addPropertyNode(
          messageSettings,
          "NetworkMessageContentMask",
          NodeIds.UadpNetworkMessageContentMask,
          ValueRanks.Scalar,
          new Variant(uadp.getNetworkMessageContentMask()));

      addPropertyNode(
          messageSettings,
          "PublishingOffset",
          NodeIds.Duration,
          ValueRanks.OneDimension,
          new Variant(orEmpty(uadp.getPublishingOffset(), Double[]::new)));
    }

    DataSetWriterDataType[] writers =
        orEmpty(group.getDataSetWriters(), DataSetWriterDataType[]::new);

    for (DataSetWriterDataType writer : writers) {
      buildDataSetWriterNodes(nodeId, connectionName, name, writer);
    }

    notifyBuilt(ComponentType.WRITER_GROUP, path, node);
  }

  private void buildDataSetWriterNodes(
      NodeId groupNodeId, String connectionName, String groupName, DataSetWriterDataType writer) {

    String name = nullToEmpty(writer.getName());
    String path = connectionName + "/" + groupName + "/" + name;

    NodeId nodeId = PubSubNodeIds.componentNodeId(namespaceIndex, path);

    DataSetWriterTypeNode node =
        addObjectNode(
            DataSetWriterTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.DataSetWriterType,
            NodeIds.HasDataSetWriter,
            groupNodeId);

    addPropertyNode(
        node,
        "DataSetWriterId",
        NodeIds.UInt16,
        ValueRanks.Scalar,
        new Variant(writer.getDataSetWriterId()));

    addPropertyNode(
        node,
        "DataSetFieldContentMask",
        NodeIds.DataSetFieldContentMask,
        ValueRanks.Scalar,
        new Variant(writer.getDataSetFieldContentMask()));

    addPropertyNode(
        node,
        "KeyFrameCount",
        NodeIds.UInt32,
        ValueRanks.Scalar,
        new Variant(writer.getKeyFrameCount()));

    addPropertyNode(
        node,
        "DataSetWriterProperties",
        NodeIds.KeyValuePair,
        ValueRanks.OneDimension,
        new Variant(orEmpty(writer.getDataSetWriterProperties(), KeyValuePair[]::new)));

    addStatusNodes(
        node, path, () -> service.components().dataSetWriter(connectionName, groupName, name));

    if (writer.getMessageSettings() instanceof UadpDataSetWriterMessageDataType uadp) {
      UadpDataSetWriterMessageTypeNode messageSettings =
          addObjectNode(
              UadpDataSetWriterMessageTypeNode::new,
              childNodeId(node, "MessageSettings"),
              new QualifiedName(0, "MessageSettings"),
              NodeIds.UadpDataSetWriterMessageType,
              NodeIds.HasComponent,
              nodeId);

      addPropertyNode(
          messageSettings,
          "DataSetMessageContentMask",
          NodeIds.UadpDataSetMessageContentMask,
          ValueRanks.Scalar,
          new Variant(uadp.getDataSetMessageContentMask()));

      addPropertyNode(
          messageSettings,
          "ConfiguredSize",
          NodeIds.UInt16,
          ValueRanks.Scalar,
          new Variant(uadp.getConfiguredSize()));

      addPropertyNode(
          messageSettings,
          "NetworkMessageNumber",
          NodeIds.UInt16,
          ValueRanks.Scalar,
          new Variant(uadp.getNetworkMessageNumber()));

      addPropertyNode(
          messageSettings,
          "DataSetOffset",
          NodeIds.UInt16,
          ValueRanks.Scalar,
          new Variant(uadp.getDataSetOffset()));
    }

    NodeId dataSetNodeId =
        writer.getDataSetName() != null ? publishedDataSetNodeId(writer.getDataSetName()) : null;

    if (dataSetNodeId != null) {
      // stored with its invert, so the dataset node browses a forward DataSetToWriter reference
      node.addReference(
          new Reference(
              nodeId,
              NodeIds.DataSetToWriter,
              dataSetNodeId.expanded(),
              Reference.Direction.INVERSE));
    }

    notifyBuilt(ComponentType.DATA_SET_WRITER, path, node);
  }

  private void buildReaderGroupNodes(
      NodeId connectionNodeId, String connectionName, ReaderGroupDataType group) {

    String name = nullToEmpty(group.getName());
    String path = connectionName + "/" + name;

    NodeId nodeId = PubSubNodeIds.componentNodeId(namespaceIndex, path);

    ReaderGroupTypeNode node =
        addObjectNode(
            ReaderGroupTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.ReaderGroupType,
            NodeIds.HasReaderGroup,
            connectionNodeId);

    addGroupPropertyNodes(node, group);

    addStatusNodes(node, path, () -> service.components().readerGroup(connectionName, name));

    DataSetReaderDataType[] readers =
        orEmpty(group.getDataSetReaders(), DataSetReaderDataType[]::new);

    for (DataSetReaderDataType reader : readers) {
      buildDataSetReaderNodes(nodeId, connectionName, name, reader);
    }

    notifyBuilt(ComponentType.READER_GROUP, path, node);
  }

  private void buildDataSetReaderNodes(
      NodeId groupNodeId, String connectionName, String groupName, DataSetReaderDataType reader) {

    String name = nullToEmpty(reader.getName());
    String path = connectionName + "/" + groupName + "/" + name;

    NodeId nodeId = PubSubNodeIds.componentNodeId(namespaceIndex, path);

    DataSetReaderTypeNode node =
        addObjectNode(
            DataSetReaderTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.DataSetReaderType,
            NodeIds.HasDataSetReader,
            groupNodeId);

    Variant publisherId =
        reader.getPublisherId() != null ? reader.getPublisherId() : Variant.NULL_VALUE;

    addPropertyNode(node, "PublisherId", NodeIds.BaseDataType, ValueRanks.Scalar, publisherId);

    addPropertyNode(
        node,
        "WriterGroupId",
        NodeIds.UInt16,
        ValueRanks.Scalar,
        new Variant(reader.getWriterGroupId()));

    addPropertyNode(
        node,
        "DataSetWriterId",
        NodeIds.UInt16,
        ValueRanks.Scalar,
        new Variant(reader.getDataSetWriterId()));

    PropertyTypeNode metaDataNode =
        addPropertyNode(
            node,
            "DataSetMetaData",
            NodeIds.DataSetMetaDataType,
            ValueRanks.Scalar,
            new Variant(reader.getDataSetMetaData()));

    metaDataVariables.put(path, metaDataNode);

    addPropertyNode(
        node,
        "DataSetFieldContentMask",
        NodeIds.DataSetFieldContentMask,
        ValueRanks.Scalar,
        new Variant(reader.getDataSetFieldContentMask()));

    addPropertyNode(
        node,
        "MessageReceiveTimeout",
        NodeIds.Duration,
        ValueRanks.Scalar,
        new Variant(reader.getMessageReceiveTimeout()));

    addPropertyNode(
        node,
        "KeyFrameCount",
        NodeIds.UInt32,
        ValueRanks.Scalar,
        new Variant(reader.getKeyFrameCount()));

    addPropertyNode(
        node,
        "HeaderLayoutUri",
        NodeIds.String,
        ValueRanks.Scalar,
        new Variant(nullToEmpty(reader.getHeaderLayoutUri())));

    addPropertyNode(
        node,
        "DataSetReaderProperties",
        NodeIds.KeyValuePair,
        ValueRanks.OneDimension,
        new Variant(orEmpty(reader.getDataSetReaderProperties(), KeyValuePair[]::new)));

    // reader-level security members are all Optional; omitted unless security is configured.
    // Invalid is the §6.2.9.9 no-override sentinel emitted by the config mapper for readers
    // without a reader-level security override (the group's settings apply), so it is treated
    // the same as None here.
    if (reader.getSecurityMode() != null
        && reader.getSecurityMode() != MessageSecurityMode.None
        && reader.getSecurityMode() != MessageSecurityMode.Invalid) {
      addPropertyNode(
          node,
          "SecurityMode",
          NodeIds.MessageSecurityMode,
          ValueRanks.Scalar,
          new Variant(reader.getSecurityMode()));
    }
    if (reader.getSecurityGroupId() != null) {
      addPropertyNode(
          node,
          "SecurityGroupId",
          NodeIds.String,
          ValueRanks.Scalar,
          new Variant(reader.getSecurityGroupId()));
    }
    EndpointDescription[] securityKeyServices = reader.getSecurityKeyServices();
    if (securityKeyServices != null && securityKeyServices.length > 0) {
      addPropertyNode(
          node,
          "SecurityKeyServices",
          NodeIds.EndpointDescription,
          ValueRanks.OneDimension,
          new Variant(securityKeyServices));
    }

    addStatusNodes(
        node, path, () -> service.components().dataSetReader(connectionName, groupName, name));

    if (reader.getMessageSettings() instanceof UadpDataSetReaderMessageDataType uadp) {
      buildReaderMessageSettingsNodes(node, nodeId, uadp);
    }

    buildSubscribedDataSetNodes(node, nodeId, reader);

    notifyBuilt(ComponentType.DATA_SET_READER, path, node);
  }

  private void buildReaderMessageSettingsNodes(
      DataSetReaderTypeNode readerNode,
      NodeId readerNodeId,
      UadpDataSetReaderMessageDataType uadp) {

    UadpDataSetReaderMessageTypeNode messageSettings =
        addObjectNode(
            UadpDataSetReaderMessageTypeNode::new,
            childNodeId(readerNode, "MessageSettings"),
            new QualifiedName(0, "MessageSettings"),
            NodeIds.UadpDataSetReaderMessageType,
            NodeIds.HasComponent,
            readerNodeId);

    addPropertyNode(
        messageSettings,
        "GroupVersion",
        NodeIds.VersionTime,
        ValueRanks.Scalar,
        new Variant(uadp.getGroupVersion()));

    addPropertyNode(
        messageSettings,
        "NetworkMessageNumber",
        NodeIds.UInt16,
        ValueRanks.Scalar,
        new Variant(uadp.getNetworkMessageNumber()));

    addPropertyNode(
        messageSettings,
        "DataSetOffset",
        NodeIds.UInt16,
        ValueRanks.Scalar,
        new Variant(uadp.getDataSetOffset()));

    addPropertyNode(
        messageSettings,
        "DataSetClassId",
        NodeIds.Guid,
        ValueRanks.Scalar,
        new Variant(uadp.getDataSetClassId()));

    addPropertyNode(
        messageSettings,
        "NetworkMessageContentMask",
        NodeIds.UadpNetworkMessageContentMask,
        ValueRanks.Scalar,
        new Variant(uadp.getNetworkMessageContentMask()));

    addPropertyNode(
        messageSettings,
        "DataSetMessageContentMask",
        NodeIds.UadpDataSetMessageContentMask,
        ValueRanks.Scalar,
        new Variant(uadp.getDataSetMessageContentMask()));

    addPropertyNode(
        messageSettings,
        "PublishingInterval",
        NodeIds.Duration,
        ValueRanks.Scalar,
        new Variant(uadp.getPublishingInterval()));

    addPropertyNode(
        messageSettings,
        "ProcessingOffset",
        NodeIds.Duration,
        ValueRanks.Scalar,
        new Variant(uadp.getProcessingOffset()));

    addPropertyNode(
        messageSettings,
        "ReceiveOffset",
        NodeIds.Duration,
        ValueRanks.Scalar,
        new Variant(uadp.getReceiveOffset()));
  }

  /**
   * Build the Mandatory SubscribedDataSet child: a TargetVariablesType instance when the reader is
   * configured with TargetVariables, otherwise the member-less SubscribedDataSetType base.
   */
  private void buildSubscribedDataSetNodes(
      DataSetReaderTypeNode readerNode, NodeId readerNodeId, DataSetReaderDataType reader) {

    if (reader.getSubscribedDataSet() instanceof TargetVariablesDataType targetVariables) {
      TargetVariablesTypeNode subscribedDataSet =
          addObjectNode(
              TargetVariablesTypeNode::new,
              childNodeId(readerNode, "SubscribedDataSet"),
              new QualifiedName(0, "SubscribedDataSet"),
              NodeIds.TargetVariablesType,
              NodeIds.HasComponent,
              readerNodeId);

      addPropertyNode(
          subscribedDataSet,
          "TargetVariables",
          NodeIds.FieldTargetDataType,
          ValueRanks.OneDimension,
          new Variant(orEmpty(targetVariables.getTargetVariables(), FieldTargetDataType[]::new)));
    } else {
      addObjectNode(
          SubscribedDataSetTypeNode::new,
          childNodeId(readerNode, "SubscribedDataSet"),
          new QualifiedName(0, "SubscribedDataSet"),
          NodeIds.SubscribedDataSetType,
          NodeIds.HasComponent,
          readerNodeId);
    }
  }

  /** Add the PubSubGroupType members shared by WriterGroupType and ReaderGroupType instances. */
  private void addGroupPropertyNodes(UaNode node, PubSubGroupDataType group) {
    addPropertyNode(
        node,
        "SecurityMode",
        NodeIds.MessageSecurityMode,
        ValueRanks.Scalar,
        new Variant(group.getSecurityMode()));

    addPropertyNode(
        node,
        "MaxNetworkMessageSize",
        NodeIds.UInt32,
        ValueRanks.Scalar,
        new Variant(group.getMaxNetworkMessageSize()));

    addPropertyNode(
        node,
        "GroupProperties",
        NodeIds.KeyValuePair,
        ValueRanks.OneDimension,
        new Variant(orEmpty(group.getGroupProperties(), KeyValuePair[]::new)));

    if (group.getSecurityGroupId() != null) {
      addPropertyNode(
          node,
          "SecurityGroupId",
          NodeIds.String,
          ValueRanks.Scalar,
          new Variant(group.getSecurityGroupId()));
    }

    EndpointDescription[] securityKeyServices = group.getSecurityKeyServices();
    if (securityKeyServices != null && securityKeyServices.length > 0) {
      addPropertyNode(
          node,
          "SecurityKeyServices",
          NodeIds.EndpointDescription,
          ValueRanks.OneDimension,
          new Variant(securityKeyServices));
    }
  }

  /**
   * Add a Status object with a State variable to {@code parent} and register the State variable
   * under {@code componentPath} for live updates. When remote configuration is allowed, the
   * Optional Enable/Disable Method pair is minted on the Status object (see {@link
   * PubSubStatusMethods}); {@code handleLookup} resolves the component's handle per invocation
   * (name components captured at mint time — handles are invalidated by reconfiguration and paths
   * are never parsed, R11).
   */
  private void addStatusNodes(
      UaNode parent, String componentPath, Supplier<Optional<PubSubHandle>> handleLookup) {

    PubSubStatusTypeNode statusNode =
        addObjectNode(
            PubSubStatusTypeNode::new,
            childNodeId(parent, "Status"),
            new QualifiedName(0, "Status"),
            NodeIds.PubSubStatusType,
            NodeIds.HasComponent,
            parent.getNodeId());

    BaseDataVariableTypeNode stateNode =
        addVariableNode(
            statusNode, "State", NodeIds.PubSubState, new Variant(initialState(handleLookup)));

    stateVariables.put(componentPath, stateNode);

    if (allowRemoteConfiguration) {
      UaMethodNode enableNode = addMethodNode(statusNode, "Enable");
      enableNode.setInvocationHandler(
          new PubSubStatusMethods.EnableHandler(enableNode, service, authorizer, handleLookup));

      UaMethodNode disableNode = addMethodNode(statusNode, "Disable");
      disableNode.setInvocationHandler(
          new PubSubStatusMethods.DisableHandler(disableNode, service, authorizer, handleLookup));
    }
  }

  /**
   * Create a no-argument method node under {@code parent} (HasComponent), matching the type
   * declaration's BrowseName in ns0. No InputArguments/OutputArguments properties are created (both
   * are empty), methods carry no HasTypeDefinition, and no RolePermissions are minted (the
   * authorizer is the gate).
   */
  private UaMethodNode addMethodNode(UaNode parent, String name) {
    NodeId nodeId = childNodeId(parent, name);

    var node =
        new UaMethodNode(
            getNodeContext(),
            nodeId,
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            true,
            true);

    getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasComponent,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    return node;
  }

  // endregion

  // region node helpers

  /** The shared shape of the generated object TypeNode constructors (without event notifier). */
  @FunctionalInterface
  private interface ObjectNodeConstructor<T extends UaObjectNode> {
    T create(
        UaNodeContext context,
        NodeId nodeId,
        QualifiedName browseName,
        LocalizedText displayName,
        LocalizedText description,
        UInteger writeMask,
        UInteger userWriteMask,
        RolePermissionType @Nullable [] rolePermissions,
        RolePermissionType @Nullable [] userRolePermissions,
        @Nullable AccessRestrictionType accessRestrictions);
  }

  /**
   * Create an object node, type-define it, add it to this fragment's node manager, and graft it
   * under {@code parentNodeId} with an inverse {@code referenceTypeId} reference (the node manager
   * stores both directions, so the parent browses the forward reference without being modified).
   *
   * <p>Package-private for the diagnostics exposure (WP-Z).
   */
  <T extends UaObjectNode> T addObjectNode(
      ObjectNodeConstructor<T> constructor,
      NodeId nodeId,
      QualifiedName browseName,
      NodeId typeDefinitionId,
      NodeId referenceTypeId,
      NodeId parentNodeId) {

    T node =
        constructor.create(
            getNodeContext(),
            nodeId,
            browseName,
            LocalizedText.english(browseName.name()),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasTypeDefinition,
            typeDefinitionId.expanded(),
            Reference.Direction.FORWARD));

    getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            nodeId, referenceTypeId, parentNodeId.expanded(), Reference.Direction.INVERSE));

    return node;
  }

  /**
   * Create a PropertyType variable node under {@code parent} (HasProperty), pre-created explicitly
   * so the typed-setter create-on-set path is never exercised. Read-only by default.
   *
   * <p>Package-private for the diagnostics exposure (WP-Z).
   */
  PropertyTypeNode addPropertyNode(
      UaNode parent, String name, NodeId dataTypeId, int valueRank, Variant value) {

    NodeId nodeId = childNodeId(parent, name);

    var node =
        new PropertyTypeNode(
            getNodeContext(),
            nodeId,
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null,
            new DataValue(value),
            dataTypeId,
            valueRank,
            valueRank == ValueRanks.Scalar ? null : new UInteger[] {uint(0)});

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasTypeDefinition,
            NodeIds.PropertyType.expanded(),
            Reference.Direction.FORWARD));

    getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasProperty,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    return node;
  }

  /**
   * Create a scalar BaseDataVariableType component variable under {@code parent} (HasComponent).
   *
   * <p>Package-private for the diagnostics exposure (WP-Z).
   */
  BaseDataVariableTypeNode addVariableNode(
      UaNode parent, String name, NodeId dataTypeId, Variant value) {

    NodeId nodeId = childNodeId(parent, name);

    var node =
        new BaseDataVariableTypeNode(
            getNodeContext(),
            nodeId,
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null,
            new DataValue(value),
            dataTypeId,
            ValueRanks.Scalar,
            null);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasTypeDefinition,
            NodeIds.BaseDataVariableType.expanded(),
            Reference.Direction.FORWARD));

    getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasComponent,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    return node;
  }

  /**
   * Create a scalar String SelectionListType component variable under {@code parent} with its
   * Mandatory Selections property.
   */
  private SelectionListTypeNode addSelectionListNode(
      UaNode parent, String name, @Nullable String value, Object[] selections) {

    NodeId nodeId = childNodeId(parent, name);

    var node =
        new SelectionListTypeNode(
            getNodeContext(),
            nodeId,
            new QualifiedName(0, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null,
            new DataValue(new Variant(value)),
            NodeIds.String,
            ValueRanks.Scalar,
            null);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasTypeDefinition,
            NodeIds.SelectionListType.expanded(),
            Reference.Direction.FORWARD));

    getNodeManager().addNode(node);

    node.addReference(
        new Reference(
            nodeId,
            NodeIds.HasComponent,
            parent.getNodeId().expanded(),
            Reference.Direction.INVERSE));

    addPropertyNode(
        node, "Selections", NodeIds.BaseDataType, ValueRanks.OneDimension, new Variant(selections));

    return node;
  }

  /**
   * The deterministic id of a member child of {@code parent} (see {@link PubSubNodeIds}).
   *
   * <p>Package-private for the diagnostics exposure (WP-Z).
   */
  NodeId childNodeId(UaNode parent, String name) {
    return PubSubNodeIds.childNodeId(parent.getNodeId(), name);
  }

  // endregion

  // region reconfiguration rebuild (R10)

  /**
   * Apply a successful reconfiguration to the exposed model: delete the subtrees of removed and
   * restarted components, rebuild the subtrees of added and restarted components from {@code
   * newConfig}, and refresh the maintained ns0 values.
   *
   * <p>Caller: the first {@link ManagedPubSubService.ReconfigureHook}, on the reconfiguring thread,
   * inside the mediator's critical section. The engine apply has already completed, so {@code
   * components()} lookups resolve the new handles. Restarted components keep their NodeIds (R11
   * determinism); a client monitoring a removed component's node starts seeing {@code
   * Bad_NodeIdUnknown}. A reconfigure applied while the fragment is not active — before startup, or
   * racing fragment shutdown — records the new configuration without rebuilding: startup builds
   * from the recorded configuration, so a pre-startup reconfigure is still reflected (a reconfigure
   * concurrent with an in-progress startup build may still be built from the older snapshot — an
   * accepted, momentary window; management-plane callers reconfigure before or after startup, not
   * during). One racing service shutdown either throws in the engine before the hooks run or
   * rebuilds into an already-unregistered node manager — harmless and invisible.
   *
   * <p>Coexisting entries are legal (see {@link ReconfigureResult}): under {@code
   * STOP_AND_RESTART}, nested ADDED/REMOVED entries are reported alongside the containing
   * connection's CHANGED entry, and a security-group-induced group-level CHANGED entry may coexist
   * with entries for that group's descendants. Both phases de-duplicate against the ancestor:
   * deletions run ancestors first, so a descendant entry finds its node already deleted and is
   * skipped (its {@code onComponentRemoving} notification fires exactly once, from the ancestor's
   * old-config walk); builds skip any component whose node already exists — every subtree being
   * rebuilt was deleted first, so existence proves a coexisting ancestor entry's rebuild already
   * covered it.
   */
  void applyReconfigure(PubSubConfig newConfig, ReconfigureResult result) {
    if (!active) {
      // no tree to rebuild, but the reflected config must still advance so a startup() that
      // follows a pre-startup reconfigure builds the configuration the engine actually runs
      this.config = newConfig;
      LOGGER.debug("applyReconfigure: fragment not active; recorded the config without a rebuild");
      return;
    }

    PubSubConfiguration2DataType configuration =
        newConfig.toDataType(getServer().getNamespaceTable());

    // 1. deletions: every removed and restarted subtree, ancestors before descendants so a
    //    descendant entry coexisting with its ancestor's entry is skipped (node already gone)
    removeChangedSubtrees(result, ReconfigureResult.Scope.CONNECTION);
    removeChangedSubtrees(result, ReconfigureResult.Scope.WRITER_GROUP);
    removeChangedSubtrees(result, ReconfigureResult.Scope.READER_GROUP);
    removeChangedSubtrees(result, ReconfigureResult.Scope.DATA_SET_WRITER);
    removeChangedSubtrees(result, ReconfigureResult.Scope.DATA_SET_READER);
    removeChangedSubtrees(result, ReconfigureResult.Scope.PUBLISHED_DATA_SET);

    // 2. builds, parents before children: dataset nodes must exist before writers re-add their
    //    DataSetToWriter references, and group/leaf builders resolve their parents by id.
    //    A restarted PublishedDataSet induces CHANGED entries for every referencing writer, so
    //    the writers rebuild in this same pass and re-add their references.
    for (ReconfigureResult.Change change : result.changes()) {
      if (change.kind() != ReconfigureResult.Kind.REMOVED
          && change.scope() == ReconfigureResult.Scope.PUBLISHED_DATA_SET
          && !getNodeManager()
              .containsNode(PubSubNodeIds.publishedDataSetNodeId(namespaceIndex, change.path()))) {
        PublishedDataSetDataType dataSet = findPublishedDataSet(configuration, change.path());
        if (dataSet != null) {
          buildPublishedDataSetNodes(dataSet);
        }
      }
    }

    buildChangedComponents(result, configuration, ReconfigureResult.Scope.CONNECTION);
    buildChangedComponents(result, configuration, ReconfigureResult.Scope.WRITER_GROUP);
    buildChangedComponents(result, configuration, ReconfigureResult.Scope.READER_GROUP);
    buildChangedComponents(result, configuration, ReconfigureResult.Scope.DATA_SET_WRITER);
    buildChangedComponents(result, configuration, ReconfigureResult.Scope.DATA_SET_READER);

    // 3. ns0 refresh, then swap the reflected config
    refreshNs0Values(configuration, newConfig.isEnabled());

    this.config = newConfig;
  }

  /**
   * Register a listener observing per-component subtree builds and removals. Must be called before
   * {@code ServerPubSub.startup()}.
   */
  void addComponentNodeListener(ComponentNodeListener listener) {
    componentNodeListeners.add(listener);
  }

  /**
   * Delete the subtree of every REMOVED and CHANGED (restarted) entry of {@code scope}. Invoked
   * scope by scope, ancestors first, so a descendant entry coexisting with an ancestor's CHANGED
   * entry (see {@link ReconfigureResult}) finds its node already deleted and is skipped by {@link
   * #removeChangedSubtree} — listeners are notified exactly once per component, by the ancestor's
   * old-config walk.
   */
  private void removeChangedSubtrees(ReconfigureResult result, ReconfigureResult.Scope scope) {
    for (ReconfigureResult.Change change : result.changes()) {
      if (change.scope() == scope
          && (change.kind() == ReconfigureResult.Kind.REMOVED
              || change.kind() == ReconfigureResult.Kind.CHANGED)) {
        removeChangedSubtree(change);
      }
    }
  }

  /**
   * Delete the subtree of a removed or restarted component: notify listeners (children before
   * parents, walking the OLD config — never the node graph), delete the subtree root (recursion
   * covers the HasChild-subtype references grafting every descendant), and prune the live-state
   * registries by name-path prefix (safe because deletions always operate on whole subtrees keyed
   * by the same join rule that built the keys). A no-op — no notifications — when the node is
   * already gone (an ancestor's deletion covered it).
   */
  private void removeChangedSubtree(ReconfigureResult.Change change) {
    NodeId nodeId =
        switch (change.scope()) {
          case CONNECTION, WRITER_GROUP, READER_GROUP, DATA_SET_WRITER, DATA_SET_READER ->
              PubSubNodeIds.componentNodeId(namespaceIndex, change.path());
          case PUBLISHED_DATA_SET ->
              PubSubNodeIds.publishedDataSetNodeId(namespaceIndex, change.path());
          // never materialized (matches the initial build); nothing to delete
          case STANDALONE_SUBSCRIBED_DATA_SET, SECURITY_GROUP -> null;
        };

    if (nodeId == null) {
      return;
    }

    UaNode node = getNodeManager().getNode(nodeId).orElse(null);
    if (node == null) {
      LOGGER.debug("no subtree to remove for change: {}", change);
      return;
    }

    notifyRemoving(change);

    node.delete();

    if (change.scope() != ReconfigureResult.Scope.PUBLISHED_DATA_SET) {
      pruneRegistry(stateVariables, change.path());
      pruneRegistry(metaDataVariables, change.path());
    }
  }

  /** Remove registry entries equal to {@code namePath} or under {@code namePath + "/"}. */
  private static void pruneRegistry(Map<String, UaVariableNode> registry, String namePath) {
    registry.keySet().removeIf(key -> key.equals(namePath) || key.startsWith(namePath + "/"));
  }

  /**
   * Build the subtree of every non-removed change of {@code scope}, resolving parents by id. A
   * change whose component node already exists is skipped: the deletion phase already removed
   * everything being rebuilt, so an existing node proves a coexisting ancestor entry's rebuild
   * (which builds all descendants from the new config) already covered this component.
   */
  private void buildChangedComponents(
      ReconfigureResult result,
      PubSubConfiguration2DataType configuration,
      ReconfigureResult.Scope scope) {

    for (ReconfigureResult.Change change : result.changes()) {
      if (change.kind() == ReconfigureResult.Kind.REMOVED || change.scope() != scope) {
        continue;
      }

      if (getNodeManager()
          .containsNode(PubSubNodeIds.componentNodeId(namespaceIndex, change.path()))) {
        LOGGER.debug("skipping change already covered by an ancestor rebuild: {}", change);
        continue;
      }

      String connectionName = change.connectionName();
      String groupName = change.groupName();
      String componentName = change.componentName();
      if (connectionName == null) {
        continue; // tree scopes always carry a connection name
      }

      PubSubConnectionDataType connection = findConnection(configuration, connectionName);
      if (connection == null) {
        LOGGER.error("applied change refers to an unknown connection: {}", change);
        continue;
      }

      switch (scope) {
        case CONNECTION -> buildConnectionNodes(connection);
        case WRITER_GROUP -> {
          WriterGroupDataType group = findWriterGroup(connection, groupName);
          NodeId parentId =
              existingNodeId(PubSubNodeIds.componentNodeId(namespaceIndex, connectionName));
          if (group != null && parentId != null) {
            buildWriterGroupNodes(parentId, connectionName, group);
          } else {
            LOGGER.error("cannot rebuild writer group subtree for change: {}", change);
          }
        }
        case READER_GROUP -> {
          ReaderGroupDataType group = findReaderGroup(connection, groupName);
          NodeId parentId =
              existingNodeId(PubSubNodeIds.componentNodeId(namespaceIndex, connectionName));
          if (group != null && parentId != null) {
            buildReaderGroupNodes(parentId, connectionName, group);
          } else {
            LOGGER.error("cannot rebuild reader group subtree for change: {}", change);
          }
        }
        case DATA_SET_WRITER -> {
          WriterGroupDataType group = findWriterGroup(connection, groupName);
          DataSetWriterDataType writer =
              group != null ? findDataSetWriter(group, componentName) : null;
          NodeId parentId =
              groupName != null
                  ? existingNodeId(
                      PubSubNodeIds.componentNodeId(
                          namespaceIndex, connectionName + "/" + groupName))
                  : null;
          if (writer != null && parentId != null && groupName != null) {
            buildDataSetWriterNodes(parentId, connectionName, groupName, writer);
          } else {
            LOGGER.error("cannot rebuild dataset writer subtree for change: {}", change);
          }
        }
        case DATA_SET_READER -> {
          ReaderGroupDataType group = findReaderGroup(connection, groupName);
          DataSetReaderDataType reader =
              group != null ? findDataSetReader(group, componentName) : null;
          NodeId parentId =
              groupName != null
                  ? existingNodeId(
                      PubSubNodeIds.componentNodeId(
                          namespaceIndex, connectionName + "/" + groupName))
                  : null;
          if (reader != null && parentId != null && groupName != null) {
            buildDataSetReaderNodes(parentId, connectionName, groupName, reader);
          } else {
            LOGGER.error("cannot rebuild dataset reader subtree for change: {}", change);
          }
        }
        default -> {}
      }
    }
  }

  /**
   * Notify listeners of every component in the subtree being deleted, children before parents,
   * walking the OLD config's component structure by the change's name components.
   */
  private void notifyRemoving(ReconfigureResult.Change change) {
    PubSubConfig oldConfig = this.config;
    String connectionName = change.connectionName();
    String groupName = change.groupName();
    String componentName = change.componentName();

    switch (change.scope()) {
      case CONNECTION -> {
        if (connectionName == null) {
          return;
        }
        oldConfig
            .connection(connectionName)
            .ifPresent(
                connection -> {
                  for (WriterGroupConfig group : connection.writerGroups()) {
                    notifyRemovingWriterGroup(connectionName, group);
                  }
                  for (ReaderGroupConfig group : connection.readerGroups()) {
                    notifyRemovingReaderGroup(connectionName, group);
                  }
                  notifyRemoving(ComponentType.CONNECTION, connectionName);
                });
      }
      case WRITER_GROUP -> {
        if (connectionName == null || groupName == null) {
          return;
        }
        oldConfig
            .connection(connectionName)
            .flatMap(
                connection ->
                    connection.writerGroups().stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst())
            .ifPresent(group -> notifyRemovingWriterGroup(connectionName, group));
      }
      case READER_GROUP -> {
        if (connectionName == null || groupName == null) {
          return;
        }
        oldConfig
            .connection(connectionName)
            .flatMap(
                connection ->
                    connection.readerGroups().stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst())
            .ifPresent(group -> notifyRemovingReaderGroup(connectionName, group));
      }
      case DATA_SET_WRITER -> {
        if (componentName != null) {
          notifyRemoving(ComponentType.DATA_SET_WRITER, change.path());
        }
      }
      case DATA_SET_READER -> {
        if (componentName != null) {
          notifyRemoving(ComponentType.DATA_SET_READER, change.path());
        }
      }
      default -> {}
    }
  }

  private void notifyRemovingWriterGroup(String connectionName, WriterGroupConfig group) {
    String groupPath = connectionName + "/" + group.getName();
    for (DataSetWriterConfig writer : group.getDataSetWriters()) {
      notifyRemoving(ComponentType.DATA_SET_WRITER, groupPath + "/" + writer.getName());
    }
    notifyRemoving(ComponentType.WRITER_GROUP, groupPath);
  }

  private void notifyRemovingReaderGroup(String connectionName, ReaderGroupConfig group) {
    String groupPath = connectionName + "/" + group.getName();
    for (DataSetReaderConfig reader : group.getDataSetReaders()) {
      notifyRemoving(ComponentType.DATA_SET_READER, groupPath + "/" + reader.getName());
    }
    notifyRemoving(ComponentType.READER_GROUP, groupPath);
  }

  private void notifyRemoving(ComponentType type, String namePath) {
    for (ComponentNodeListener listener : componentNodeListeners) {
      try {
        listener.onComponentRemoving(type, namePath);
      } catch (Exception e) {
        LOGGER.error("ComponentNodeListener.onComponentRemoving failed: {}", namePath, e);
      }
    }
  }

  private void notifyBuilt(ComponentType type, String namePath, UaObjectNode componentNode) {
    for (ComponentNodeListener listener : componentNodeListeners) {
      try {
        listener.onComponentBuilt(type, namePath, componentNode);
      } catch (Exception e) {
        LOGGER.error("ComponentNodeListener.onComponentBuilt failed: {}", namePath, e);
      }
    }
  }

  // region wire-form lookups (names are unique per scope, enforced by the config builder)

  private static @Nullable PubSubConnectionDataType findConnection(
      PubSubConfiguration2DataType configuration, String name) {

    for (PubSubConnectionDataType connection :
        orEmpty(configuration.getConnections(), PubSubConnectionDataType[]::new)) {
      if (Objects.equals(connection.getName(), name)) {
        return connection;
      }
    }
    return null;
  }

  private static @Nullable PublishedDataSetDataType findPublishedDataSet(
      PubSubConfiguration2DataType configuration, String name) {

    for (PublishedDataSetDataType dataSet :
        orEmpty(configuration.getPublishedDataSets(), PublishedDataSetDataType[]::new)) {
      if (Objects.equals(dataSet.getName(), name)) {
        return dataSet;
      }
    }
    return null;
  }

  private static @Nullable WriterGroupDataType findWriterGroup(
      PubSubConnectionDataType connection, @Nullable String name) {

    for (WriterGroupDataType group :
        orEmpty(connection.getWriterGroups(), WriterGroupDataType[]::new)) {
      if (Objects.equals(group.getName(), name)) {
        return group;
      }
    }
    return null;
  }

  private static @Nullable ReaderGroupDataType findReaderGroup(
      PubSubConnectionDataType connection, @Nullable String name) {

    for (ReaderGroupDataType group :
        orEmpty(connection.getReaderGroups(), ReaderGroupDataType[]::new)) {
      if (Objects.equals(group.getName(), name)) {
        return group;
      }
    }
    return null;
  }

  private static @Nullable DataSetWriterDataType findDataSetWriter(
      WriterGroupDataType group, @Nullable String name) {

    for (DataSetWriterDataType writer :
        orEmpty(group.getDataSetWriters(), DataSetWriterDataType[]::new)) {
      if (Objects.equals(writer.getName(), name)) {
        return writer;
      }
    }
    return null;
  }

  private static @Nullable DataSetReaderDataType findDataSetReader(
      ReaderGroupDataType group, @Nullable String name) {

    for (DataSetReaderDataType reader :
        orEmpty(group.getDataSetReaders(), DataSetReaderDataType[]::new)) {
      if (Objects.equals(reader.getName(), name)) {
        return reader;
      }
    }
    return null;
  }

  // endregion

  // endregion

  // region ConfigurationObjectIds (R4/R11)

  @Override
  public @Nullable NodeId connectionObjectId(String connectionName) {
    return existingNodeId(PubSubNodeIds.componentNodeId(namespaceIndex, connectionName));
  }

  @Override
  public @Nullable NodeId writerGroupObjectId(String connectionName, String groupName) {
    return existingNodeId(
        PubSubNodeIds.componentNodeId(namespaceIndex, connectionName + "/" + groupName));
  }

  @Override
  public @Nullable NodeId dataSetWriterObjectId(
      String connectionName, String groupName, String writerName) {
    return existingNodeId(
        PubSubNodeIds.componentNodeId(
            namespaceIndex, connectionName + "/" + groupName + "/" + writerName));
  }

  @Override
  public @Nullable NodeId readerGroupObjectId(String connectionName, String groupName) {
    return existingNodeId(
        PubSubNodeIds.componentNodeId(namespaceIndex, connectionName + "/" + groupName));
  }

  @Override
  public @Nullable NodeId dataSetReaderObjectId(
      String connectionName, String groupName, String readerName) {
    return existingNodeId(
        PubSubNodeIds.componentNodeId(
            namespaceIndex, connectionName + "/" + groupName + "/" + readerName));
  }

  @Override
  public @Nullable NodeId publishedDataSetObjectId(String name) {
    return existingNodeId(PubSubNodeIds.publishedDataSetNodeId(namespaceIndex, name));
  }

  /** {@code nodeId} iff the node currently exists in this fragment's node manager, else null. */
  private @Nullable NodeId existingNodeId(NodeId nodeId) {
    return getNodeManager().containsNode(nodeId) ? nodeId : null;
  }

  /** The deterministic id of a published dataset node, present-checked against the node manager. */
  private @Nullable NodeId publishedDataSetNodeId(String name) {
    return publishedDataSetObjectId(name);
  }

  // endregion

  // region ns0 population and live state

  /**
   * Populate values on the existing ns0 children of the PublishSubscribe object. Existing nodes are
   * looked up and their values set; nothing is created (and absent nodes are only logged), so the
   * ns0 node manager is never modified.
   */
  private void populateExistingNs0Nodes(PubSubConfiguration2DataType configuration) {
    Optional<UaNode> publishSubscribeNode =
        getServer().getAddressSpaceManager().getManagedNode(NodeIds.PublishSubscribe);

    if (publishSubscribeNode.isEmpty()) {
      LOGGER.warn("ns0 PublishSubscribe node not found: {}", NodeIds.PublishSubscribe);
      return;
    }

    UaNode node = publishSubscribeNode.get();

    setExistingPropertyValue(
        node,
        PublishSubscribeType.SUPPORTED_TRANSPORT_PROFILES.getBrowseName(),
        new Variant(new String[] {UdpTransportProvider.TRANSPORT_PROFILE_URI}));

    refreshNs0Values(configuration, config.isEnabled());
  }

  /**
   * Refresh the ns0 values maintained per apply (and set at startup): the ConfigurationVersion —
   * read from the mediator-owned single source (D26) — the ConfigurationProperties, and the root
   * Status/State.
   */
  private void refreshNs0Values(PubSubConfiguration2DataType configuration, boolean enabled) {
    Optional<UaNode> publishSubscribeNode =
        getServer().getAddressSpaceManager().getManagedNode(NodeIds.PublishSubscribe);

    publishSubscribeNode.ifPresent(
        node -> {
          setExistingPropertyValue(
              node,
              PublishSubscribeType.CONFIGURATION_VERSION.getBrowseName(),
              new Variant(configurationVersion.get()));

          setExistingPropertyValue(
              node,
              PublishSubscribeType.CONFIGURATION_PROPERTIES.getBrowseName(),
              new Variant(
                  orEmpty(configuration.getConfigurationProperties(), KeyValuePair[]::new)));
        });

    setRootState(enabled ? PubSubState.Operational : PubSubState.Disabled);
  }

  /** Set the value of an existing ns0 property node, by browse name; never creates. */
  private void setExistingPropertyValue(UaNode node, String browseName, Variant value) {
    Optional<VariableNode> propertyNode = node.getPropertyNode(new QualifiedName(0, browseName));

    propertyNode.ifPresentOrElse(
        property -> property.setValue(new DataValue(value)),
        () -> LOGGER.warn("ns0 property node not found: {}", browseName));
  }

  /**
   * Set the ns0 PublishSubscribe Status/State value ({@code i=17406}). The root state reflects the
   * service-level enabled flag: Operational while this exposure is active and the configuration is
   * enabled, Disabled otherwise (the engine has no service-root component handle).
   */
  private void setRootState(PubSubState state) {
    getServer()
        .getAddressSpaceManager()
        .getManagedNode(NodeIds.PublishSubscribe_Status_State)
        .ifPresent(
            node -> {
              if (node instanceof UaVariableNode variableNode) {
                variableNode.setValue(new DataValue(new Variant(state)));
              }
            });
  }

  /**
   * Register the live-update listeners. {@link PubSubService} has no listener removal, so the
   * callbacks are guarded by {@link #active} and become no-ops after shutdown.
   */
  private void registerListeners() {
    service.addStateListener(
        event -> {
          if (!active || !isTrackedComponentType(event.component().componentType())) {
            return;
          }

          UaVariableNode stateNode = stateVariables.get(event.component().path());
          if (stateNode != null) {
            stateNode.setValue(new DataValue(new Variant(event.newState())));
          }
        });

    service.addMetaDataListener(
        event -> {
          if (!active) {
            return;
          }

          UaVariableNode metaDataNode = metaDataVariables.get(event.reader().path());
          if (metaDataNode != null) {
            metaDataNode.setValue(new DataValue(new Variant(event.metaData())));
          }
        });
  }

  /**
   * Only components with Status nodes in this exposure are tracked; other component types (e.g.
   * PublishedDataSets, whose bare-name paths could collide with connection names) are ignored.
   */
  private static boolean isTrackedComponentType(ComponentType componentType) {
    return switch (componentType) {
      case CONNECTION, WRITER_GROUP, DATA_SET_WRITER, READER_GROUP, DATA_SET_READER -> true;
      default -> false;
    };
  }

  /** Seed a Status/State value from the runtime, falling back to the Part 14 default Disabled. */
  private PubSubState initialState(Supplier<Optional<PubSubHandle>> handleLookup) {
    try {
      return handleLookup.get().map(service::state).orElse(PubSubState.Disabled);
    } catch (IllegalArgumentException e) {
      return PubSubState.Disabled;
    }
  }

  // endregion

  private static <T> T[] orEmpty(T @Nullable [] array, IntFunction<T[]> arrayFactory) {
    return array != null ? array : arrayFactory.apply(0);
  }

  private static String nullToEmpty(@Nullable String value) {
    return value != null ? value : "";
  }
}

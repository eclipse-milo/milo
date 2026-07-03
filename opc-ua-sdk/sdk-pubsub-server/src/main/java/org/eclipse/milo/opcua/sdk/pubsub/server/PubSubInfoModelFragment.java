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

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntFunction;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
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
 * Exposes the read-only PublishSubscribe information model for an attached PubSub runtime:
 * populates and animates the ns0 PublishSubscribe subtree and grafts connection, group, writer,
 * reader, and published-dataset objects reflecting the attach-time configuration.
 *
 * <p>The fragment is a {@link ManagedAddressSpaceFragmentWithLifecycle} with its own {@code
 * UaNodeManager} and {@link SubscriptionModel}, registered with the server's {@code
 * AddressSpaceManager} on startup and unregistered on shutdown. References grafting the new
 * subtrees onto ns0 nodes (PublishSubscribe {@code i=14443} via HasPubSubConnection, the
 * PublishedDataSets folder {@code i=17371} via HasComponent) are stored, with their inverses, in
 * this fragment's node manager only; ns0's node manager is never structurally modified.
 *
 * <p>Node identity: every node created by this fragment has a deterministic string {@link NodeId}
 * in the server application namespace, {@code "PubSub/<connection>[/<group>[/<writer|reader>]]"}
 * for runtime components and {@code "PubSub/PublishedDataSets/<name>"} for published datasets, with
 * member children appended as further {@code "/"}-separated segments (e.g. {@code
 * "PubSub/conn/Status/State"}). The ids are stable across server restarts for an unchanged
 * configuration.
 *
 * <p>All variables are read-only ({@code AccessLevel.CurrentRead}, the {@code UaVariableNode}
 * default) and no method nodes are created. Pre-existing ns0 method nodes (AddConnection,
 * RemoveConnection, the SKS methods, ...) and unbacked optional ns0 components (Diagnostics,
 * PubSubConfiguration, PubSubCapablities, ...) are left exactly as the ns0 loader created them.
 * PublishedDataSet nodes carry no Status child (Part 14 defines none); published-dataset runtime
 * state is not surfaced.
 *
 * <p>Values are populated from the attach-time {@link PubSubConfig}, normalized through {@code
 * PubSubConfig.toDataType} so defaults, transport profile URIs, and derived DataSetMetaData match
 * what the runtime publishes. Existing ns0 children of PublishSubscribe (Status/State {@code
 * i=17406}, SupportedTransportProfiles {@code i=17481}, ConfigurationVersion, and
 * ConfigurationProperties) are populated by looking up the existing nodes and setting values only;
 * property create-on-set against ns0 parents is never triggered.
 *
 * <p>Live state: component Status/State variables are updated from {@link
 * PubSubService#addStateListener}, keyed by component name path so that reconfiguration (which
 * invalidates handles) does not break tracking; reader DataSetMetaData values are updated from
 * {@link PubSubService#addMetaDataListener}. Everything else reflects the attach-time configuration
 * only: reconfiguring via {@code ServerPubSub.runtime()} does <b>not</b> rebuild config-derived
 * nodes (a documented v1 limitation).
 *
 * <p>Created by {@link ServerPubSub} when {@link ServerPubSubOptions#isExposeInformationModel()} is
 * {@code true}; {@link #startup()} and {@link #shutdown()} are driven by the owning {@link
 * ServerPubSub}'s lifecycle.
 */
final class PubSubInfoModelFragment extends ManagedAddressSpaceFragmentWithLifecycle {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubInfoModelFragment.class);

  /** Prefix shared by every NodeId identifier minted by this fragment. */
  private static final String NODE_ID_PREFIX = "PubSub";

  /** The epoch of the OPC UA VersionTime data type: 2000-01-01T00:00:00 UTC. */
  private static final Instant VERSION_TIME_EPOCH = Instant.parse("2000-01-01T00:00:00Z");

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

  private final PubSubConfig config;
  private final PubSubService service;
  private final UShort namespaceIndex;

  PubSubInfoModelFragment(
      OpcUaServer server, PubSubConfig config, PubSubService service, ServerPubSubOptions options) {

    super(server);

    this.config = config;
    this.service = service;

    namespaceIndex = server.getServerNamespace().getNamespaceIndex();

    subscriptionModel = new SubscriptionModel(server, this);
    getLifecycleManager().addLifecycle(subscriptionModel);

    getLifecycleManager()
        .addLifecycle(
            new Lifecycle() {
              @Override
              public void startup() {
                PubSubConfiguration2DataType configuration =
                    config.toDataType(getServer().getNamespaceTable());

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
    var dataSetNodeIds = new HashMap<String, NodeId>();

    PublishedDataSetDataType[] publishedDataSets =
        orEmpty(configuration.getPublishedDataSets(), PublishedDataSetDataType[]::new);

    for (PublishedDataSetDataType dataSet : publishedDataSets) {
      NodeId nodeId = buildPublishedDataSetNodes(dataSet);
      if (dataSet.getName() != null) {
        dataSetNodeIds.put(dataSet.getName(), nodeId);
      }
    }

    PubSubConnectionDataType[] connections =
        orEmpty(configuration.getConnections(), PubSubConnectionDataType[]::new);

    for (PubSubConnectionDataType connection : connections) {
      buildConnectionNodes(connection, dataSetNodeIds);
    }
  }

  private NodeId buildPublishedDataSetNodes(PublishedDataSetDataType dataSet) {
    String name = nullToEmpty(dataSet.getName());

    var nodeId = new NodeId(namespaceIndex, NODE_ID_PREFIX + "/PublishedDataSets/" + name);

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

    return nodeId;
  }

  private void buildConnectionNodes(
      PubSubConnectionDataType connection, Map<String, NodeId> dataSetNodeIds) {

    String name = nullToEmpty(connection.getName());

    var nodeId = new NodeId(namespaceIndex, NODE_ID_PREFIX + "/" + name);

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

    addStatusNodes(node, name, initialState(service.components().connection(name)));

    WriterGroupDataType[] writerGroups =
        orEmpty(connection.getWriterGroups(), WriterGroupDataType[]::new);

    for (WriterGroupDataType group : writerGroups) {
      buildWriterGroupNodes(node, name, group, dataSetNodeIds);
    }

    ReaderGroupDataType[] readerGroups =
        orEmpty(connection.getReaderGroups(), ReaderGroupDataType[]::new);

    for (ReaderGroupDataType group : readerGroups) {
      buildReaderGroupNodes(node, name, group);
    }
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
      PubSubConnectionTypeNode connectionNode,
      String connectionName,
      WriterGroupDataType group,
      Map<String, NodeId> dataSetNodeIds) {

    String name = nullToEmpty(group.getName());
    String path = connectionName + "/" + name;

    var nodeId = new NodeId(namespaceIndex, NODE_ID_PREFIX + "/" + path);

    WriterGroupTypeNode node =
        addObjectNode(
            WriterGroupTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.WriterGroupType,
            NodeIds.HasWriterGroup,
            connectionNode.getNodeId());

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

    addStatusNodes(
        node, path, initialState(service.components().writerGroup(connectionName, name)));

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
      buildDataSetWriterNodes(node, connectionName, name, writer, dataSetNodeIds);
    }
  }

  private void buildDataSetWriterNodes(
      WriterGroupTypeNode groupNode,
      String connectionName,
      String groupName,
      DataSetWriterDataType writer,
      Map<String, NodeId> dataSetNodeIds) {

    String name = nullToEmpty(writer.getName());
    String path = connectionName + "/" + groupName + "/" + name;

    var nodeId = new NodeId(namespaceIndex, NODE_ID_PREFIX + "/" + path);

    DataSetWriterTypeNode node =
        addObjectNode(
            DataSetWriterTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.DataSetWriterType,
            NodeIds.HasDataSetWriter,
            groupNode.getNodeId());

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
        node,
        path,
        initialState(service.components().dataSetWriter(connectionName, groupName, name)));

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
        writer.getDataSetName() != null ? dataSetNodeIds.get(writer.getDataSetName()) : null;

    if (dataSetNodeId != null) {
      // stored with its invert, so the dataset node browses a forward DataSetToWriter reference
      node.addReference(
          new Reference(
              nodeId,
              NodeIds.DataSetToWriter,
              dataSetNodeId.expanded(),
              Reference.Direction.INVERSE));
    }
  }

  private void buildReaderGroupNodes(
      PubSubConnectionTypeNode connectionNode, String connectionName, ReaderGroupDataType group) {

    String name = nullToEmpty(group.getName());
    String path = connectionName + "/" + name;

    var nodeId = new NodeId(namespaceIndex, NODE_ID_PREFIX + "/" + path);

    ReaderGroupTypeNode node =
        addObjectNode(
            ReaderGroupTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.ReaderGroupType,
            NodeIds.HasReaderGroup,
            connectionNode.getNodeId());

    addGroupPropertyNodes(node, group);

    addStatusNodes(
        node, path, initialState(service.components().readerGroup(connectionName, name)));

    DataSetReaderDataType[] readers =
        orEmpty(group.getDataSetReaders(), DataSetReaderDataType[]::new);

    for (DataSetReaderDataType reader : readers) {
      buildDataSetReaderNodes(node, connectionName, name, reader);
    }
  }

  private void buildDataSetReaderNodes(
      ReaderGroupTypeNode groupNode,
      String connectionName,
      String groupName,
      DataSetReaderDataType reader) {

    String name = nullToEmpty(reader.getName());
    String path = connectionName + "/" + groupName + "/" + name;

    var nodeId = new NodeId(namespaceIndex, NODE_ID_PREFIX + "/" + path);

    DataSetReaderTypeNode node =
        addObjectNode(
            DataSetReaderTypeNode::new,
            nodeId,
            new QualifiedName(namespaceIndex, name),
            NodeIds.DataSetReaderType,
            NodeIds.HasDataSetReader,
            groupNode.getNodeId());

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
        node,
        path,
        initialState(service.components().dataSetReader(connectionName, groupName, name)));

    if (reader.getMessageSettings() instanceof UadpDataSetReaderMessageDataType uadp) {
      buildReaderMessageSettingsNodes(node, nodeId, uadp);
    }

    buildSubscribedDataSetNodes(node, nodeId, reader);
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
   * under {@code componentPath} for live updates.
   */
  private void addStatusNodes(UaNode parent, String componentPath, PubSubState initialState) {
    PubSubStatusTypeNode statusNode =
        addObjectNode(
            PubSubStatusTypeNode::new,
            childNodeId(parent, "Status"),
            new QualifiedName(0, "Status"),
            NodeIds.PubSubStatusType,
            NodeIds.HasComponent,
            parent.getNodeId());

    BaseDataVariableTypeNode stateNode =
        addVariableNode(statusNode, "State", NodeIds.PubSubState, new Variant(initialState));

    stateVariables.put(componentPath, stateNode);
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
   */
  private <T extends UaObjectNode> T addObjectNode(
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
   */
  private PropertyTypeNode addPropertyNode(
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
   */
  private BaseDataVariableTypeNode addVariableNode(
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

  private NodeId childNodeId(UaNode parent, String name) {
    return new NodeId(namespaceIndex, parent.getNodeId().getIdentifier().toString() + "/" + name);
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

    setExistingPropertyValue(
        node,
        PublishSubscribeType.CONFIGURATION_VERSION.getBrowseName(),
        new Variant(versionTimeNow()));

    setExistingPropertyValue(
        node,
        PublishSubscribeType.CONFIGURATION_PROPERTIES.getBrowseName(),
        new Variant(orEmpty(configuration.getConfigurationProperties(), KeyValuePair[]::new)));

    setRootState(config.isEnabled() ? PubSubState.Operational : PubSubState.Disabled);
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
  private PubSubState initialState(Optional<PubSubHandle> handle) {
    try {
      return handle.map(service::state).orElse(PubSubState.Disabled);
    } catch (IllegalArgumentException e) {
      return PubSubState.Disabled;
    }
  }

  /** The current time as an OPC UA VersionTime: seconds since 2000-01-01T00:00:00 UTC. */
  private static UInteger versionTimeNow() {
    long seconds = Instant.now().getEpochSecond() - VERSION_TIME_EPOCH.getEpochSecond();
    return uint(seconds & 0xFFFFFFFFL);
  }

  // endregion

  private static <T> T[] orEmpty(T @Nullable [] array, IntFunction<T[]> arrayFactory) {
    return array != null ? array : arrayFactory.apply(0);
  }

  private static String nullToEmpty(@Nullable String value) {
    return value != null ? value : "";
  }
}

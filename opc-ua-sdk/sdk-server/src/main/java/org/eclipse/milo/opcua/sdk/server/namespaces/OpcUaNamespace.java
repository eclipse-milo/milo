/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.namespaces;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConditionTypeConditionRefresh;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConditionTypeConditionRefresh2;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.OperationLimitsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.namespaces.loader.NodeLoader;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcUaNamespace extends ManagedNamespaceWithLifecycle {

  /**
   * <a href="https://profiles.opcfoundation.org/profile/1333">Standard 2022 UA Server Profile</a>
   */
  @SuppressWarnings("HttpUrlsUsage")
  private static final String SERVER_PROFILE_STANDARD_2022 =
      "http://opcfoundation.org/UA-Profile/Server/StandardUA2022";

  private static final double MIN_SAMPLING_INTERVAL = 100.0;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final SubscriptionModel subscriptionModel;

  private final OpcUaServer server;

  public OpcUaNamespace(OpcUaServer server) {
    super(server, Namespaces.OPC_UA);

    this.server = server;

    subscriptionModel = new SubscriptionModel(server, this);

    getLifecycleManager()
        .addStartupTask(
            () -> {
              loadNodes();
              configureServerObject();
              configureConditionType();

              // Set a reasonable value for the MinimumSamplingInterval
              // attribute on all VariableNodes, otherwise it defaults to 0.
              getNodeManager().getNodes().stream()
                  .filter(node -> node instanceof UaVariableNode)
                  .map(UaVariableNode.class::cast)
                  .forEach(n -> n.setMinimumSamplingInterval(MIN_SAMPLING_INTERVAL));
            });

    getLifecycleManager().addLifecycle(subscriptionModel);
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

  private void loadNodes() {
    try {
      long startTime = System.nanoTime();
      long startCount = getNodeManager().getNodes().size();

      new NodeLoader(getNodeContext(), getNodeManager()).loadNodes();

      long deltaMs =
          TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
      long deltaCount = getNodeManager().getNodes().size() - startCount;

      logger.debug("Loaded {} nodes in {}ms.", deltaCount, deltaMs);
    } catch (Exception e) {
      logger.error("Error loading nodes.", e);
    }
  }

  private void configureServerObject() {
    ServerTypeNode serverTypeNode = (ServerTypeNode) getNodeManager().get(NodeIds.Server);

    assert serverTypeNode != null;

    serverTypeNode
        .getNamespaceArrayNode()
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> new DataValue(new Variant(server.getNamespaceTable().toArray()))));

    serverTypeNode
        .getServerArrayNode()
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> new DataValue(new Variant(server.getServerTable().toArray()))));

    serverTypeNode.setAuditing(false);
    serverTypeNode.getServerDiagnosticsNode().setEnabledFlag(false);
    serverTypeNode.setServiceLevel(ubyte(255));
    serverTypeNode.setEstimatedReturnTime(DateTime.now());

    ServerStatusTypeNode serverStatus = serverTypeNode.getServerStatusNode();

    BuildInfo buildInfo = server.getConfig().getBuildInfo();
    serverStatus.setBuildInfo(buildInfo);
    serverStatus.getBuildInfoNode().setBuildDate(buildInfo.getBuildDate());
    serverStatus.getBuildInfoNode().setBuildNumber(buildInfo.getBuildNumber());
    serverStatus.getBuildInfoNode().setManufacturerName(buildInfo.getManufacturerName());
    serverStatus.getBuildInfoNode().setProductName(buildInfo.getProductName());
    serverStatus.getBuildInfoNode().setProductUri(buildInfo.getProductUri());
    serverStatus.getBuildInfoNode().setSoftwareVersion(buildInfo.getSoftwareVersion());

    serverStatus.setCurrentTime(DateTime.now());
    serverStatus.setSecondsTillShutdown(uint(0));
    serverStatus.setShutdownReason(LocalizedText.NULL_VALUE);
    serverStatus.setState(ServerState.Running);
    serverStatus.setStartTime(DateTime.now());

    serverStatus
        .getCurrentTimeNode()
        .getFilterChain()
        .addLast(AttributeFilters.getValue(ctx -> new DataValue(new Variant(DateTime.now()))));

    serverStatus
        .getFilterChain()
        .addLast(
            AttributeFilters.getValue(
                ctx -> {
                  ServerStatusTypeNode serverStatusNode = (ServerStatusTypeNode) ctx.getNode();

                  ExtensionObject xo =
                      ExtensionObject.encode(
                          server.getStaticEncodingContext(),
                          new ServerStatusDataType(
                              serverStatusNode.getStartTime(),
                              DateTime.now(),
                              serverStatusNode.getState(),
                              serverStatusNode.getBuildInfo(),
                              serverStatusNode.getSecondsTillShutdown(),
                              serverStatusNode.getShutdownReason()));

                  return new DataValue(new Variant(xo));
                }));

    final OpcUaServerConfigLimits limits = server.getConfig().getLimits();
    ServerCapabilitiesTypeNode serverCapabilities = serverTypeNode.getServerCapabilitiesNode();
    serverCapabilities.setServerProfileArray(new String[] {SERVER_PROFILE_STANDARD_2022});
    serverCapabilities.setLocaleIdArray(new String[] {Locale.ENGLISH.getLanguage()});
    serverCapabilities.setMaxArrayLength(limits.getMaxArrayLength());
    serverCapabilities.setMaxStringLength(limits.getMaxStringLength());
    serverCapabilities.setMaxByteStringLength(limits.getMaxByteStringLength());
    serverCapabilities.setMaxBrowseContinuationPoints(limits.getMaxBrowseContinuationPoints());
    serverCapabilities.setMaxHistoryContinuationPoints(limits.getMaxHistoryContinuationPoints());
    serverCapabilities.setMaxQueryContinuationPoints(limits.getMaxQueryContinuationPoints());
    serverCapabilities.setMinSupportedSampleRate(limits.getMinSupportedSampleRate());
    serverCapabilities.setMaxSessions(limits.getMaxSessions());
    serverCapabilities.setMaxSubscriptions(limits.getMaxSubscriptions());
    serverCapabilities.setMaxSubscriptionsPerSession(limits.getMaxSubscriptionsPerSession());
    serverCapabilities.setMaxMonitoredItems(limits.getMaxMonitoredItems());

    // note: we don't have a per-subscription limit, we have a per-session limit.
    serverCapabilities.getMaxMonitoredItemsPerSubscriptionNode().delete();

    /* optional limits that are not implemented */

    // TODO optional, but will be needed for role support
    serverCapabilities.getRoleSetNode().delete();

    serverCapabilities.getMaxSelectClauseParametersNode().delete();
    serverCapabilities.getMaxWhereClauseParametersNode().delete();
    serverCapabilities.getConformanceUnitsNode().delete();

    OperationLimitsTypeNode limitsNode = serverCapabilities.getOperationLimitsNode();
    limitsNode.setMaxMonitoredItemsPerCall(limits.getMaxMonitoredItemsPerCall());
    limitsNode.setMaxNodesPerBrowse(limits.getMaxNodesPerBrowse());
    limitsNode.setMaxNodesPerHistoryReadData(limits.getMaxNodesPerHistoryReadData());
    limitsNode.setMaxNodesPerHistoryReadEvents(limits.getMaxNodesPerHistoryReadEvents());
    limitsNode.setMaxNodesPerHistoryUpdateData(limits.getMaxNodesPerHistoryUpdateData());
    limitsNode.setMaxNodesPerHistoryUpdateEvents(limits.getMaxNodesPerHistoryUpdateEvents());
    limitsNode.setMaxNodesPerMethodCall(limits.getMaxNodesPerMethodCall());
    limitsNode.setMaxNodesPerNodeManagement(limits.getMaxNodesPerNodeManagement());
    limitsNode.setMaxNodesPerRead(limits.getMaxNodesPerRead());
    limitsNode.setMaxNodesPerRegisterNodes(limits.getMaxNodesPerRegisterNodes());
    limitsNode.setMaxNodesPerTranslateBrowsePathsToNodeIds(
        limits.getMaxNodesPerTranslateBrowsePathsToNodeIds());
    limitsNode.setMaxNodesPerWrite(limits.getMaxNodesPerWrite());

    serverTypeNode.getServerRedundancyNode().setRedundancySupport(RedundancySupport.None);

    serverTypeNode.setGetMonitoredItemsHandler(new GetMonitoredItemsMethodImpl(server));
    serverTypeNode.setResendDataHandler(new ResendDataMethodImpl());

    configureAliasMethods();
  }

  private void configureAliasMethods() {
    // The standard FindAlias Methods have no behavior unless an application installs an
    // AliasManager. Marking them non-executable surfaces alias support as an absent feature
    // instead of a callable Method that always fails with Bad_NotImplemented; an installed
    // AliasManager restores both flags when it binds its handlers. UserExecutable is the flag
    // access control enforces on Call.
    NodeId[] findAliasNodeIds = {
      NodeIds.Aliases_FindAlias, NodeIds.TagVariables_FindAlias, NodeIds.Topics_FindAlias
    };

    for (NodeId findAliasNodeId : findAliasNodeIds) {
      UaNode node = getNodeManager().get(findAliasNodeId);

      if (node instanceof UaMethodNode methodNode) {
        methodNode.setExecutable(false);
        methodNode.setUserExecutable(false);
      } else {
        logger.warn("FindAlias UaMethodNode not found: {}", findAliasNodeId);
      }
    }
  }

  /**
   * Configure the ConditionType declaration: its ConditionRefresh Methods, which have no instance
   * modelling rule and are called against the type node, and its SupportsFilteredRetain Property.
   */
  private void configureConditionType() {
    UaNode node = getNodeManager().get(NodeIds.ConditionType_ConditionRefresh);

    if (node instanceof UaMethodNode conditionRefreshNode) {
      conditionRefreshNode.bindInvocationHandler(
          new ConditionRefreshMethodImpl(conditionRefreshNode));
    } else {
      logger.warn("ConditionRefresh UaMethodNode not found.");
    }

    UaNode node2 = getNodeManager().get(NodeIds.ConditionType_ConditionRefresh2);

    if (node2 instanceof UaMethodNode conditionRefresh2Node) {
      conditionRefresh2Node.bindInvocationHandler(
          new ConditionRefresh2MethodImpl(conditionRefresh2Node));
    } else {
      logger.warn("ConditionRefresh2 UaMethodNode not found.");
    }

    UaNode node3 = getNodeManager().get(NodeIds.ConditionType_SupportsFilteredRetain);

    if (node3 instanceof UaVariableNode supportsFilteredRetainNode) {
      supportsFilteredRetainNode.setValue(new DataValue(new Variant(false)));
    } else {
      logger.warn("SupportsFilteredRetain UaVariableNode not found.");
    }
  }

  private static class ConditionRefreshMethodImpl extends AbstractMethodInvocationHandler {

    private final OpcUaServer server;
    private final Argument[] inputArguments;
    private final Argument[] outputArguments;

    ConditionRefreshMethodImpl(UaMethodNode node) {
      super(node);

      server = node.getNodeContext().getServer();
      inputArguments = ConditionTypeConditionRefresh.inputArguments(server.getNamespaceTable());
      outputArguments = ConditionTypeConditionRefresh.outputArguments(server.getNamespaceTable());
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments;
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments;
    }

    @Override
    protected Variant[] invoke(InvocationContext context, Variant[] values) throws UaException {
      ConditionTypeConditionRefresh.Inputs input =
          ConditionTypeConditionRefresh.Inputs.fromVariants(
              server.getStaticEncodingContext(), values);

      Session session =
          context.getSession().orElseThrow(() -> new UaException(StatusCodes.Bad_UserAccessDenied));

      server.getConditionManager().conditionRefresh(session, input.subscriptionId());
      return new Variant[0];
    }
  }

  private static class ConditionRefresh2MethodImpl extends AbstractMethodInvocationHandler {

    private final OpcUaServer server;
    private final Argument[] inputArguments;
    private final Argument[] outputArguments;

    ConditionRefresh2MethodImpl(UaMethodNode node) {
      super(node);

      server = node.getNodeContext().getServer();
      inputArguments = ConditionTypeConditionRefresh2.inputArguments(server.getNamespaceTable());
      outputArguments = ConditionTypeConditionRefresh2.outputArguments(server.getNamespaceTable());
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments;
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments;
    }

    @Override
    protected Variant[] invoke(InvocationContext context, Variant[] values) throws UaException {
      ConditionTypeConditionRefresh2.Inputs input =
          ConditionTypeConditionRefresh2.Inputs.fromVariants(
              server.getStaticEncodingContext(), values);

      Session session =
          context.getSession().orElseThrow(() -> new UaException(StatusCodes.Bad_UserAccessDenied));

      server
          .getConditionManager()
          .conditionRefresh2(session, input.subscriptionId(), input.monitoredItemId());
      return new Variant[0];
    }
  }

  private static class GetMonitoredItemsMethodImpl implements ServerType.GetMonitoredItemsHandler {

    private final OpcUaServer server;

    GetMonitoredItemsMethodImpl(OpcUaServer server) {
      this.server = server;
    }

    @Override
    public ServerTypeGetMonitoredItems.Outputs getMonitoredItems(
        InvocationContext context, UInteger subscriptionId) throws UaException {

      Session session =
          context.getSession().orElseThrow(() -> new UaException(StatusCodes.Bad_SessionIdInvalid));

      Subscription subscription = server.getSubscriptions().get(subscriptionId);

      if (subscription == null) {
        throw new UaException(StatusCodes.Bad_SubscriptionIdInvalid);
      }

      if (!session.getSessionId().equals(subscription.getSession().getSessionId())) {
        throw new UaException(StatusCodes.Bad_UserAccessDenied);
      }

      var serverHandleList = new ArrayList<UInteger>();
      var clientHandleList = new ArrayList<UInteger>();

      for (BaseMonitoredItem<?> item : subscription.getMonitoredItems().values()) {
        serverHandleList.add(item.getId());
        clientHandleList.add(uint(item.getClientHandle()));
      }

      return new ServerTypeGetMonitoredItems.Outputs(
          serverHandleList.toArray(new UInteger[0]), clientHandleList.toArray(new UInteger[0]));
    }
  }

  private static class ResendDataMethodImpl implements ServerType.ResendDataHandler {

    @Override
    public void resendData(InvocationContext context, UInteger subscriptionId) throws UaException {
      Session session = context.getSession().orElse(null);

      if (session != null) {
        Subscription subscription =
            session.getSubscriptionManager().getSubscription(subscriptionId);

        if (subscription == null) {
          if (session.getServer().getSubscriptions().containsKey(subscriptionId)) {
            // Exists but belongs to another session
            throw new UaException(StatusCodes.Bad_UserAccessDenied);
          } else {
            // Doesn't exist in any session
            throw new UaException(StatusCodes.Bad_SubscriptionIdInvalid);
          }
        } else {
          subscription.getMonitoredItems().values().stream()
              .filter(item -> item instanceof MonitoredDataItem)
              .map(item -> (MonitoredDataItem) item)
              .forEach(MonitoredDataItem::maybeSendLastValue);
        }
      } else {
        throw new UaException(StatusCodes.Bad_UserAccessDenied);
      }
    }
  }
}

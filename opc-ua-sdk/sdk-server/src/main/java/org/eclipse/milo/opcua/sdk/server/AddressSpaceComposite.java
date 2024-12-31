/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.stream.Collectors.groupingBy;
import static org.eclipse.milo.opcua.sdk.core.util.GroupMapCollate.groupMapCollate;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReferenceResult.ReferenceList;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.EventItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link AddressSpace} that is composed of {@link AddressSpaceFragment}s.
 *
 * <p>Service call operations are executed by the first fragment that matches on the NodeId in the
 * operation.
 */
public class AddressSpaceComposite implements AddressSpaceFragment {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final CopyOnWriteArrayList<AddressSpaceFragment> addressSpaces =
      new CopyOnWriteArrayList<>();

  private final CompositeAddressSpaceFilter filter = new CompositeAddressSpaceFilter(addressSpaces);

  private final OpcUaServer server;

  public AddressSpaceComposite(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public AddressSpaceFilter getFilter() {
    return filter;
  }

  /**
   * Register an {@link AddressSpace} with this composite.
   *
   * <p>The AddressSpace is inserted at the end of the list.
   *
   * @param addressSpace the {@link AddressSpace} to register.
   */
  public synchronized void register(AddressSpaceFragment addressSpace) {
    if (!addressSpaces.contains(addressSpace)) {
      addressSpaces.add(addressSpace);

      logger.debug("registered {}", addressSpace);
    } else {
      logger.warn("AddressSpace already registered: {}", addressSpace);
    }
  }

  /**
   * Register an {@link AddressSpace} with this composite.
   *
   * <p>The AddressSpace is inserted at the front of the list.
   *
   * @param addressSpace the {@link AddressSpace} to register.
   */
  public synchronized void registerFirst(AddressSpaceFragment addressSpace) {
    if (!addressSpaces.contains(addressSpace)) {
      addressSpaces.add(0, addressSpace);

      logger.debug("registered {} at index 0", addressSpace);
    } else {
      logger.warn("AddressSpace already registered: {}", addressSpace);
    }
  }

  /**
   * Unregister an {@link AddressSpace} with this composite.
   *
   * @param addressSpace the {@link AddressSpace} to unregister.
   */
  public synchronized void unregister(AddressSpaceFragment addressSpace) {
    if (addressSpaces.contains(addressSpace)) {
      addressSpaces.remove(addressSpace);

      logger.debug("unregistered {}", addressSpace);
    } else {
      logger.warn("AddressSpace not registered: {}", addressSpace);
    }
  }

  /**
   * Get the {@link OpcUaServer} instance.
   *
   * @return the {@link OpcUaServer} instance.
   */
  protected OpcUaServer getServer() {
    return server;
  }

  /**
   * Get a copy of the current {@link AddressSpace} list.
   *
   * @return a copy of the current {@link AddressSpace} list.
   */
  protected List<AddressSpaceFragment> getAddressSpaces() {
    return new ArrayList<>(addressSpaces);
  }

  private AddressSpaceFragment getAddressSpace(Predicate<AddressSpaceFragment> filter) {
    return addressSpaces.stream()
        .filter(filter)
        .findFirst()
        .orElse(new EmptyAddressSpaceFragment(server));
  }

  // region ViewServices

  @Override
  public List<ReferenceResult> browse(
      BrowseContext context, ViewDescription view, List<NodeId> nodeIds) {
    List<ReferenceResult> initialResults =
        groupMapCollate(
            nodeIds,
            nodeId -> getAddressSpace(asx -> asx.getFilter().filterBrowse(server, nodeId)),
            (AddressSpace asx) ->
                group -> {
                  var ctx = new BrowseContext(server, context.getSession().orElse(null));

                  return asx.browse(ctx, view, group);
                });

    final var finalResults = new ArrayList<ReferenceResult>();

    for (int i = 0; i < initialResults.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      ReferenceResult initialResult = initialResults.get(i);

      if (initialResult instanceof ReferenceList rl) {
        final var references = new LinkedHashSet<>(rl.references());

        // Gather additional references from all AddressSpaces except
        // the first, which is the one we called browse on above.

        var browseContext = new BrowseContext(getServer(), context.getSession().orElse(null));

        AddressSpaceFragment first =
            getAddressSpace(asx -> asx.getFilter().filterBrowse(server, nodeId));

        for (AddressSpace asx : addressSpaces) {
          if (asx != first) {
            ReferenceList gatherResult = asx.gather(browseContext, view, nodeId);

            references.addAll(gatherResult.references());
          }
        }

        finalResults.add(ReferenceResult.of(new ArrayList<>(references)));
      } else {
        finalResults.add(initialResult);
      }
    }

    return finalResults;
  }

  @Override
  public ReferenceList gather(BrowseContext context, ViewDescription view, NodeId nodeId) {
    var referenceStreams = new ArrayList<Stream<Reference>>();

    for (AddressSpace asx : addressSpaces) {
      var browseContext = new BrowseContext(server, context.getSession().orElse(null));

      ReferenceList result = asx.gather(browseContext, view, nodeId);
      referenceStreams.add(result.references().stream());
    }

    List<Reference> references =
        referenceStreams.stream().flatMap(Function.identity()).distinct().toList();

    return ReferenceResult.of(references);
  }

  @Override
  public List<NodeId> registerNodes(RegisterNodesContext context, List<NodeId> nodeIds) {
    return groupMapCollate(
        nodeIds,
        nodeId -> getAddressSpace(asx -> asx.getFilter().filterRegisterNode(server, nodeId)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new RegisterNodesContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.registerNodes(ctx, group);
            });
  }

  @Override
  public void unregisterNodes(UnregisterNodesContext context, List<NodeId> nodeIds) {
    groupMapCollate(
        nodeIds,
        nodeId -> getAddressSpace(asx -> asx.getFilter().filterUnregisterNode(server, nodeId)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new UnregisterNodesContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              asx.unregisterNodes(ctx, group);

              return Collections.nCopies(group.size(), Unit.VALUE);
            });
  }

  @Override
  public UInteger getViewCount() {
    return addressSpaces.stream().map(AddressSpace::getViewCount).reduce(uint(0), UInteger::add);
  }

  // endregion

  // region AttributeServices

  @Override
  public List<DataValue> read(
      ReadContext context,
      Double maxAge,
      TimestampsToReturn timestamps,
      List<ReadValueId> readValueIds) {

    return groupMapCollate(
        readValueIds,
        readValueId -> getAddressSpace(asx -> asx.getFilter().filterRead(server, readValueId)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new ReadContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.read(ctx, maxAge, timestamps, group);
            });
  }

  @Override
  public List<StatusCode> write(WriteContext context, List<WriteValue> writeValues) {

    return groupMapCollate(
        writeValues,
        writeValue -> getAddressSpace(asx -> asx.getFilter().filterWrite(server, writeValue)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new WriteContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.write(ctx, group);
            });
  }

  // endregion

  // region AttributeHistoryServices

  @Override
  public List<HistoryReadResult> historyRead(
      HistoryReadContext context,
      HistoryReadDetails details,
      TimestampsToReturn timestamps,
      List<HistoryReadValueId> readValueIds) {

    return groupMapCollate(
        readValueIds,
        readValueId ->
            getAddressSpace(asx -> asx.getFilter().filterHistoryRead(server, readValueId)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new HistoryReadContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.historyRead(ctx, details, timestamps, group);
            });
  }

  @Override
  public List<HistoryUpdateResult> historyUpdate(
      HistoryUpdateContext context, List<HistoryUpdateDetails> updateDetailsList) {

    return groupMapCollate(
        updateDetailsList,
        updateDetails ->
            getAddressSpace(asx -> asx.getFilter().filterHistoryUpdate(server, updateDetails)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new HistoryUpdateContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.historyUpdate(ctx, group);
            });
  }

  // endregion

  // region MethodServices

  @Override
  public List<CallMethodResult> call(CallContext context, List<CallMethodRequest> requests) {

    return groupMapCollate(
        requests,
        request -> getAddressSpace(asx -> asx.getFilter().filterCall(server, request)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new CallContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.call(ctx, group);
            });
  }

  // endregion

  // region MonitoredItemServices

  @Override
  public RevisedDataItemParameters onCreateDataItem(
      ReadValueId itemToMonitor, Double requestedSamplingInterval, UInteger requestedQueueSize) {

    AddressSpace addressSpace =
        getAddressSpace(asx -> asx.getFilter().filterOnCreateDataItem(server, itemToMonitor));

    return addressSpace.onCreateDataItem(
        itemToMonitor, requestedSamplingInterval, requestedQueueSize);
  }

  @Override
  public RevisedDataItemParameters onModifyDataItem(
      ReadValueId itemToModify, Double requestedSamplingInterval, UInteger requestedQueueSize) {

    AddressSpace addressSpace =
        getAddressSpace(asx -> asx.getFilter().filterOnModifyDataItem(server, itemToModify));

    return addressSpace.onModifyDataItem(
        itemToModify, requestedSamplingInterval, requestedQueueSize);
  }

  @Override
  public RevisedEventItemParameters onCreateEventItem(
      ReadValueId itemToMonitor, UInteger requestedQueueSize) {

    AddressSpace addressSpace =
        getAddressSpace(asx -> asx.getFilter().filterOnCreateEventItem(server, itemToMonitor));

    return addressSpace.onCreateEventItem(itemToMonitor, requestedQueueSize);
  }

  @Override
  public RevisedEventItemParameters onModifyEventItem(
      ReadValueId itemToModify, UInteger requestedQueueSize) {

    AddressSpace addressSpace =
        getAddressSpace(asx -> asx.getFilter().filterOnModifyEventItem(server, itemToModify));

    return addressSpace.onModifyEventItem(itemToModify, requestedQueueSize);
  }

  @Override
  public void onDataItemsCreated(List<DataItem> dataItems) {
    Map<AddressSpace, List<DataItem>> byAddressSpace =
        dataItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnDataItemsCreated(server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onDataItemsCreated);
  }

  @Override
  public void onDataItemsModified(List<DataItem> dataItems) {
    Map<AddressSpace, List<DataItem>> byAddressSpace =
        dataItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnDataItemsModified(server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onDataItemsModified);
  }

  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems) {
    Map<AddressSpace, List<DataItem>> byAddressSpace =
        dataItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnDataItemsDeleted(server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onDataItemsDeleted);
  }

  @Override
  public void onEventItemsCreated(List<EventItem> eventItems) {
    Map<AddressSpace, List<EventItem>> byAddressSpace =
        eventItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnEventItemsCreated(server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onEventItemsCreated);
  }

  @Override
  public void onEventItemsModified(List<EventItem> eventItems) {
    Map<AddressSpace, List<EventItem>> byAddressSpace =
        eventItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnEventItemsModified(server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onEventItemsModified);
  }

  @Override
  public void onEventItemsDeleted(List<EventItem> eventItems) {
    Map<AddressSpace, List<EventItem>> byAddressSpace =
        eventItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnEventItemsDeleted(server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onEventItemsDeleted);
  }

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
    Map<AddressSpace, List<MonitoredItem>> byAddressSpace =
        monitoredItems.stream()
            .collect(
                groupingBy(
                    item ->
                        getAddressSpace(
                            asx ->
                                asx.getFilter()
                                    .filterOnMonitoringModeChanged(
                                        server, item.getReadValueId()))));

    byAddressSpace.forEach(AddressSpace::onMonitoringModeChanged);
  }

  // endregion

  // region NodeManagementServices

  @Override
  public List<AddNodesResult> addNodes(AddNodesContext context, List<AddNodesItem> nodesToAdd) {
    return groupMapCollate(
        nodesToAdd,
        addNodesItem ->
            getAddressSpace(asx -> asx.getFilter().filterAddNodes(server, addNodesItem)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new AddNodesContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.addNodes(ctx, group);
            });
  }

  @Override
  public List<StatusCode> deleteNodes(
      DeleteNodesContext context, List<DeleteNodesItem> nodesToDelete) {
    return groupMapCollate(
        nodesToDelete,
        deleteNodesItem ->
            getAddressSpace(asx -> asx.getFilter().filterDeleteNodes(server, deleteNodesItem)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new DeleteNodesContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.deleteNodes(ctx, group);
            });
  }

  @Override
  public List<StatusCode> addReferences(
      AddReferencesContext context, List<AddReferencesItem> referencesToAdd) {
    return groupMapCollate(
        referencesToAdd,
        addReferencesItem ->
            getAddressSpace(asx -> asx.getFilter().filterAddReferences(server, addReferencesItem)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new AddReferencesContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.addReferences(ctx, group);
            });
  }

  @Override
  public List<StatusCode> deleteReferences(
      DeleteReferencesContext context, List<DeleteReferencesItem> referencesToDelete) {
    return groupMapCollate(
        referencesToDelete,
        deleteReferencesItem ->
            getAddressSpace(
                asx -> asx.getFilter().filterDeleteReferences(server, deleteReferencesItem)),
        (AddressSpace asx) ->
            group -> {
              var ctx =
                  new DeleteReferencesContext(
                      server,
                      context.getSession().orElse(null),
                      context.getDiagnosticsContext(),
                      context.getAuditEntryId(),
                      context.getTimeoutHint(),
                      context.getAdditionalHeader());

              return asx.deleteReferences(ctx, group);
            });
  }

  // endregion

  private static class CompositeAddressSpaceFilter implements AddressSpaceFilter {

    private final List<AddressSpaceFragment> addressSpaces;

    CompositeAddressSpaceFilter(List<AddressSpaceFragment> addressSpaces) {
      this.addressSpaces = addressSpaces;
    }

    @Override
    public boolean filterBrowse(OpcUaServer server, NodeId nodeId) {
      return addressSpaces.stream().anyMatch(asx -> asx.getFilter().filterBrowse(server, nodeId));
    }

    @Override
    public boolean filterRegisterNode(OpcUaServer server, NodeId nodeId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterRegisterNode(server, nodeId));
    }

    @Override
    public boolean filterUnregisterNode(OpcUaServer server, NodeId nodeId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterUnregisterNode(server, nodeId));
    }

    @Override
    public boolean filterRead(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterRead(server, readValueId));
    }

    @Override
    public boolean filterWrite(OpcUaServer server, WriteValue writeValue) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterWrite(server, writeValue));
    }

    @Override
    public boolean filterHistoryRead(OpcUaServer server, HistoryReadValueId historyReadValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterHistoryRead(server, historyReadValueId));
    }

    @Override
    public boolean filterHistoryUpdate(
        OpcUaServer server, HistoryUpdateDetails historyUpdateDetails) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterHistoryUpdate(server, historyUpdateDetails));
    }

    @Override
    public boolean filterCall(OpcUaServer server, CallMethodRequest callMethodRequest) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterCall(server, callMethodRequest));
    }

    @Override
    public boolean filterOnCreateDataItem(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnCreateDataItem(server, readValueId));
    }

    @Override
    public boolean filterOnModifyDataItem(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnModifyDataItem(server, readValueId));
    }

    @Override
    public boolean filterOnCreateEventItem(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnCreateEventItem(server, readValueId));
    }

    @Override
    public boolean filterOnModifyEventItem(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnModifyEventItem(server, readValueId));
    }

    @Override
    public boolean filterOnDataItemsCreated(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnDataItemsCreated(server, readValueId));
    }

    @Override
    public boolean filterOnDataItemsModified(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnDataItemsModified(server, readValueId));
    }

    @Override
    public boolean filterOnDataItemsDeleted(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnDataItemsDeleted(server, readValueId));
    }

    @Override
    public boolean filterOnEventItemsCreated(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnEventItemsCreated(server, readValueId));
    }

    @Override
    public boolean filterOnEventItemsModified(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnEventItemsModified(server, readValueId));
    }

    @Override
    public boolean filterOnEventItemsDeleted(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnEventItemsDeleted(server, readValueId));
    }

    @Override
    public boolean filterOnMonitoringModeChanged(OpcUaServer server, ReadValueId readValueId) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterOnMonitoringModeChanged(server, readValueId));
    }

    @Override
    public boolean filterAddNodes(OpcUaServer server, AddNodesItem addNodesItem) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterAddNodes(server, addNodesItem));
    }

    @Override
    public boolean filterDeleteNodes(OpcUaServer server, DeleteNodesItem deleteNodesItem) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterDeleteNodes(server, deleteNodesItem));
    }

    @Override
    public boolean filterAddReferences(OpcUaServer server, AddReferencesItem addReferencesItem) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterAddReferences(server, addReferencesItem));
    }

    @Override
    public boolean filterDeleteReferences(
        OpcUaServer server, DeleteReferencesItem deleteReferencesItem) {
      return addressSpaces.stream()
          .anyMatch(asx -> asx.getFilter().filterDeleteReferences(server, deleteReferencesItem));
    }
  }

  /** EmptyAddressSpace is used ephemerally and should never be registered. */
  private static class EmptyAddressSpaceFragment extends ManagedAddressSpace
      implements AddressSpaceFragment {

    EmptyAddressSpaceFragment(OpcUaServer server) {
      super(server);
    }

    @Override
    public AddressSpaceFilter getFilter() {
      return new SimpleAddressSpaceFilter() {
        @Override
        protected boolean filterNode(NodeId nodeId) {
          return true;
        }

        @Override
        protected boolean filterMonitoredItem(NodeId nodeId) {
          return true;
        }
      };
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {}

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}
  }
}

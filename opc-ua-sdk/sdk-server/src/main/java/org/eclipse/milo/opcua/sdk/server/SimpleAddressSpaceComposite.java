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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReferenceResult.ReferenceList;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.EventItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteAtTimeDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateStructureDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.Unit;

/**
 * An abstract {@link AddressSpaceFragment} composite that provides O(1) address space lookup when
 * the routing decision can be made directly from a {@link NodeId}.
 *
 * <p>This class differs from {@link AddressSpaceComposite} in how it routes operations to address
 * spaces:
 *
 * <ul>
 *   <li>{@link AddressSpaceComposite} iterates through all registered fragments and uses their
 *       {@link AddressSpaceFilter}s to find the first matching fragment for each operation (O(n)
 *       per lookup).
 *   <li>{@code SimpleAddressSpaceComposite} delegates the routing decision to subclasses via {@link
 *       #getAddressSpace(NodeId)}, enabling O(1) lookups when implemented with a HashMap or similar
 *       data structure.
 * </ul>
 *
 * <p><b>When to use SimpleAddressSpaceComposite:</b>
 *
 * <ul>
 *   <li>Address space routing is deterministic and can be computed directly from the {@link NodeId}
 *       (e.g., by namespace index or by parsing a device name from a String identifier).
 *   <li>You can implement O(1) or O(log n) lookups using a HashMap or TreeMap.
 *   <li>You want to minimize per-operation iteration overhead when the number of address spaces is
 *       large.
 * </ul>
 *
 * <p><b>When to use AddressSpaceComposite instead:</b>
 *
 * <ul>
 *   <li>You need dynamic registration/unregistration of address spaces at runtime.
 *   <li>You have complex filtering requirements that go beyond simple {@link NodeId} checks.
 *   <li>You need ordered/priority-based fragment selection (via {@code registerFirst}).
 * </ul>
 *
 * <p><b>Example implementation:</b>
 *
 * <p>Consider a scenario where the composite manages devices, each represented by an {@link
 * AddressSpaceFragment}. Nodes use String-based identifiers with a device prefix syntax like {@code
 * "[device]Foo.Bar.Baz"}:
 *
 * <pre>{@code
 * public class DeviceBasedComposite extends SimpleAddressSpaceComposite {
 *   private static final Pattern DEVICE_PATTERN = Pattern.compile("^\\[(.+?)].*");
 *
 *   private final Map<String, AddressSpaceFragment> fragmentsByDevice = new HashMap<>();
 *
 *   public DeviceBasedComposite(OpcUaServer server) {
 *     super(server);
 *   }
 *
 *   public void addDevice(String deviceName, AddressSpaceFragment fragment) {
 *     fragmentsByDevice.put(deviceName, fragment);
 *   }
 *
 *   @Override
 *   protected List<AddressSpaceFragment> getAddressSpaces() {
 *     return new ArrayList<>(fragmentsByDevice.values());
 *   }
 *
 *   @Override
 *   protected Optional<AddressSpaceFragment> getAddressSpace(NodeId nodeId) {
 *     return parseDeviceName(nodeId)
 *         .map(fragmentsByDevice::get);
 *   }
 *
 *   private Optional<String> parseDeviceName(NodeId nodeId) {
 *     Object identifier = nodeId.getIdentifier();
 *     if (identifier instanceof String s) {
 *       Matcher matcher = DEVICE_PATTERN.matcher(s);
 *       if (matcher.matches()) {
 *         return Optional.of(matcher.group(1));
 *       }
 *     }
 *     return Optional.empty();
 *   }
 * }
 * }</pre>
 *
 * @see AddressSpaceComposite
 * @see AddressSpaceFragment
 */
public abstract class SimpleAddressSpaceComposite implements AddressSpaceFragment {

  private final AddressSpaceFilter filter =
      SimpleAddressSpaceFilter.create(nodeId -> getAddressSpace(nodeId).isPresent());

  private final OpcUaServer server;

  public SimpleAddressSpaceComposite(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public AddressSpaceFilter getFilter() {
    return filter;
  }

  /**
   * Returns the {@link OpcUaServer} instance this composite belongs to.
   *
   * @return the {@link OpcUaServer} instance.
   */
  protected OpcUaServer getServer() {
    return server;
  }

  /**
   * Returns the list of all {@link AddressSpaceFragment}s managed by this composite.
   *
   * @return a list of all {@link AddressSpaceFragment}s in this composite.
   */
  protected abstract List<AddressSpaceFragment> getAddressSpaces();

  /**
   * Returns the {@link AddressSpaceFragment} responsible for the given {@link NodeId}, if one
   * exists.
   *
   * <p>If no address space is responsible for the given {@link NodeId}, return {@link
   * Optional#empty()}.
   *
   * @param nodeId the {@link NodeId} to find the responsible address space for.
   * @return an {@link Optional} containing the responsible {@link AddressSpaceFragment}, or empty
   *     if none exists.
   */
  protected abstract Optional<AddressSpaceFragment> getAddressSpace(NodeId nodeId);

  private AddressSpaceFragment getAddressSpaceInternal(NodeId nodeId) {
    return getAddressSpace(nodeId).orElse(new EmptyAddressSpaceFragment(server));
  }

  // region ViewServices

  @Override
  public List<ReferenceResult> browse(
      BrowseContext context, ViewDescription view, List<NodeId> nodeIds) {

    List<ReferenceResult> initialResults =
        groupMapCollate(
            nodeIds,
            this::getAddressSpaceInternal,
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

        var browseContext = new BrowseContext(server, context.getSession().orElse(null));

        AddressSpaceFragment first = getAddressSpaceInternal(nodeId);

        for (AddressSpace asx : getAddressSpaces()) {
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

    for (AddressSpace asx : getAddressSpaces()) {
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
        this::getAddressSpaceInternal,
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
        this::getAddressSpaceInternal,
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
    return getAddressSpaces().stream()
        .map(AddressSpace::getViewCount)
        .reduce(uint(0), UInteger::add);
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
        readValueId ->
            getAddressSpace(readValueId.getNodeId()).orElse(new EmptyAddressSpaceFragment(server)),
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
        writeValue ->
            getAddressSpace(writeValue.getNodeId()).orElse(new EmptyAddressSpaceFragment(server)),
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
        historyReadValueId -> getAddressSpaceInternal(historyReadValueId.getNodeId()),
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
        historyUpdateDetails -> {
          if (historyUpdateDetails instanceof DeleteAtTimeDetails details) {
            return getAddressSpaceInternal(details.getNodeId());
          } else if (historyUpdateDetails instanceof DeleteEventDetails details) {
            return getAddressSpaceInternal(details.getNodeId());
          } else if (historyUpdateDetails instanceof DeleteRawModifiedDetails details) {
            return getAddressSpaceInternal(details.getNodeId());
          } else if (historyUpdateDetails instanceof UpdateDataDetails details) {
            return getAddressSpaceInternal(details.getNodeId());
          } else if (historyUpdateDetails instanceof UpdateEventDetails details) {
            return getAddressSpaceInternal(details.getNodeId());
          } else if (historyUpdateDetails instanceof UpdateStructureDataDetails details) {
            return getAddressSpaceInternal(details.getNodeId());
          } else {
            throw new IllegalArgumentException(
                "unexpected HistoryUpdateDetails: " + historyUpdateDetails);
          }
        },
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
        callMethodRequest -> getAddressSpaceInternal(callMethodRequest.getObjectId()),
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

    AddressSpace addressSpace = getAddressSpaceInternal(itemToMonitor.getNodeId());

    return addressSpace.onCreateDataItem(
        itemToMonitor, requestedSamplingInterval, requestedQueueSize);
  }

  @Override
  public RevisedDataItemParameters onModifyDataItem(
      ReadValueId itemToModify, Double requestedSamplingInterval, UInteger requestedQueueSize) {

    AddressSpace addressSpace = getAddressSpaceInternal(itemToModify.getNodeId());

    return addressSpace.onModifyDataItem(
        itemToModify, requestedSamplingInterval, requestedQueueSize);
  }

  @Override
  public RevisedEventItemParameters onCreateEventItem(
      ReadValueId itemToMonitor, UInteger requestedQueueSize) {

    AddressSpace addressSpace = getAddressSpaceInternal(itemToMonitor.getNodeId());

    return addressSpace.onCreateEventItem(itemToMonitor, requestedQueueSize);
  }

  @Override
  public RevisedEventItemParameters onModifyEventItem(
      ReadValueId itemToModify, UInteger requestedQueueSize) {

    AddressSpace addressSpace = getAddressSpaceInternal(itemToModify.getNodeId());

    return addressSpace.onModifyEventItem(itemToModify, requestedQueueSize);
  }

  @Override
  public void onDataItemsCreated(List<DataItem> dataItems) {
    Map<AddressSpace, List<DataItem>> byAddressSpace =
        dataItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onDataItemsCreated);
  }

  @Override
  public void onDataItemsModified(List<DataItem> dataItems) {
    Map<AddressSpace, List<DataItem>> byAddressSpace =
        dataItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onDataItemsModified);
  }

  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems) {
    Map<AddressSpace, List<DataItem>> byAddressSpace =
        dataItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onDataItemsDeleted);
  }

  @Override
  public void onEventItemsCreated(List<EventItem> eventItems) {
    Map<AddressSpace, List<EventItem>> byAddressSpace =
        eventItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onEventItemsCreated);
  }

  @Override
  public void onEventItemsModified(List<EventItem> eventItems) {
    Map<AddressSpace, List<EventItem>> byAddressSpace =
        eventItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onEventItemsModified);
  }

  @Override
  public void onEventItemsDeleted(List<EventItem> eventItems) {
    Map<AddressSpace, List<EventItem>> byAddressSpace =
        eventItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onEventItemsDeleted);
  }

  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
    Map<AddressSpace, List<MonitoredItem>> byAddressSpace =
        monitoredItems.stream()
            .collect(
                groupingBy(item -> getAddressSpaceInternal(item.getReadValueId().getNodeId())));

    byAddressSpace.forEach(AddressSpace::onMonitoringModeChanged);
  }

  // endregion

  // region NodeManagementServices

  @Override
  public List<AddNodesResult> addNodes(AddNodesContext context, List<AddNodesItem> nodesToAdd) {
    return groupMapCollate(
        nodesToAdd,
        addNodesItem -> {
          NamespaceTable namespaceTable = server.getNamespaceTable();

          ExpandedNodeId requestedNewNodeId = addNodesItem.getRequestedNewNodeId();

          if (requestedNewNodeId.isNotNull()) {
            return requestedNewNodeId
                .toNodeId(namespaceTable)
                .map(this::getAddressSpaceInternal)
                .orElse(new EmptyAddressSpaceFragment(server));
          } else {
            return addNodesItem
                .getParentNodeId()
                .toNodeId(namespaceTable)
                .map(this::getAddressSpaceInternal)
                .orElse(new EmptyAddressSpaceFragment(server));
          }
        },
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
        deleteNodesItem -> getAddressSpaceInternal(deleteNodesItem.getNodeId()),
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
        addReferencesItem -> getAddressSpaceInternal(addReferencesItem.getSourceNodeId()),
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
        deleteReferencesItem -> getAddressSpaceInternal(deleteReferencesItem.getSourceNodeId()),
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

  /** EmptyAddressSpaceFragment is used ephemerally and should never be registered. */
  private static class EmptyAddressSpaceFragment extends ManagedAddressSpace
      implements AddressSpaceFragment {

    private EmptyAddressSpaceFragment(OpcUaServer server) {
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

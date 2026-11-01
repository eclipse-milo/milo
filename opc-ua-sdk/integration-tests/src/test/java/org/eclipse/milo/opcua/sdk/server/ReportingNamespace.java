/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateDataDetails;
import org.jspecify.annotations.Nullable;

/**
 * A namespace whose "blocked" operations fail with a {@link Diagnostic} in every service that
 * returns operation diagnostics, and whose "ok" operations succeed without one.
 *
 * <p>Read and Write fail by throwing from an attribute filter, the way a {@link
 * ManagedAddressSpace} application reports. The other services report through their context's
 * {@link OperationDiagnostics}, the way an address space that builds its own results reports.
 */
final class ReportingNamespace extends ManagedNamespaceWithLifecycle {

  static final String BLOCKED = "blocked";
  static final String OK = "ok";

  private final String label;

  private final UaObjectNode owner;

  ReportingNamespace(OpcUaServer server, String namespaceUri, String label) {
    super(server, namespaceUri);

    this.label = label;

    for (String name : List.of(BLOCKED, OK)) {
      UaVariableNode variable =
          new UaVariableNode.UaVariableNodeBuilder(getNodeContext())
              .setNodeId(newNodeId(name))
              .setAccessLevel(AccessLevel.READ_WRITE)
              .setUserAccessLevel(AccessLevel.READ_WRITE)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(NodeIds.Int32)
              .setTypeDefinition(NodeIds.BaseDataVariableType)
              .buildAndAdd();

      variable.setValue(new DataValue(new Variant(0)));

      if (name.equals(BLOCKED)) {
        variable.getFilterChain().addLast(new BlockingFilter());
      }
    }

    owner =
        UaObjectNode.builder(getNodeContext())
            .setNodeId(newNodeId("owner"))
            .setBrowseName(newQualifiedName("owner"))
            .setDisplayName(LocalizedText.english("owner"))
            .buildAndAdd();

    for (String name : List.of(BLOCKED, OK)) {
      UaMethodNode method =
          UaMethodNode.builder(getNodeContext())
              .setNodeId(newNodeId("method/" + name))
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .buildAndAdd();

      method.setInputArguments(
          new Argument[] {
            new Argument("Speed", NodeIds.Double, -1, null, LocalizedText.NULL_VALUE)
          });
      method.setOutputArguments(new Argument[0]);
      method.setInvocationHandler(
          name.equals(BLOCKED) ? this::invokeBlocked : (context, request) -> good());

      owner.addComponent(method);
    }
  }

  /** The diagnostic every blocked operation in this namespace reports. */
  Diagnostic diagnostic() {
    return Diagnostic.symbolic(getNamespaceUri(), "E_BLOCKED")
        .withText("Blocked in " + label)
        .withAdditionalInfo("detail for " + label)
        .withInner(new StatusCode(StatusCodes.Bad_DeviceFailure), Diagnostic.text("device"));
  }

  /** The diagnostic a blocked Method call reports for its one input argument. */
  Diagnostic argumentDiagnostic() {
    return Diagnostic.text("Speed is out of range in " + label);
  }

  /** The diagnostic a blocked HistoryUpdate reports for its second value. */
  Diagnostic valueDiagnostic() {
    return Diagnostic.text("Value exists in " + label);
  }

  NodeId nodeId(String name) {
    return newNodeId(name);
  }

  CallMethodRequest callRequest(String name) {
    return new CallMethodRequest(
        owner.getNodeId(), newNodeId("method/" + name), new Variant[] {new Variant(1.0)});
  }

  private CallMethodResult invokeBlocked(AccessContext context, CallMethodRequest request) {
    if (context instanceof AddressSpace.CallContext callContext) {
      CallOperationDiagnostics diagnostics = callContext.getOperationDiagnostics();

      diagnostics.report(request, diagnostic());
      diagnostics.reportArguments(request, List.of(argumentDiagnostic()));
    }

    return new CallMethodResult(
        new StatusCode(StatusCodes.Bad_InvalidArgument),
        new StatusCode[] {new StatusCode(StatusCodes.Bad_OutOfRange)},
        new DiagnosticInfo[0],
        new Variant[0]);
  }

  private static CallMethodResult good() {
    return new CallMethodResult(
        StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], new Variant[0]);
  }

  @Override
  public List<HistoryReadResult> historyRead(
      HistoryReadContext context,
      HistoryReadDetails readDetails,
      TimestampsToReturn timestamps,
      List<HistoryReadValueId> readValueIds) {

    var results = new ArrayList<HistoryReadResult>();

    for (HistoryReadValueId readValueId : readValueIds) {
      if (isBlocked(readValueId.getNodeId())) {
        context.getOperationDiagnostics().report(readValueId, diagnostic());
        results.add(new HistoryReadResult(bad(), null, null));
      } else {
        results.add(new HistoryReadResult(StatusCode.GOOD, null, null));
      }
    }

    return results;
  }

  @Override
  public List<HistoryUpdateResult> historyUpdate(
      HistoryUpdateContext context, List<HistoryUpdateDetails> updateDetails) {

    var results = new ArrayList<HistoryUpdateResult>();

    for (HistoryUpdateDetails details : updateDetails) {
      var update = (UpdateDataDetails) details;

      if (isBlocked(update.getNodeId())) {
        HistoryUpdateOperationDiagnostics diagnostics = context.getOperationDiagnostics();
        diagnostics.report(details, diagnostic());
        diagnostics.reportResults(details, Arrays.asList(null, valueDiagnostic()));

        results.add(
            new HistoryUpdateResult(
                bad(),
                new StatusCode[] {StatusCode.GOOD, new StatusCode(StatusCodes.Bad_EntryExists)},
                null));
      } else {
        results.add(new HistoryUpdateResult(StatusCode.GOOD, new StatusCode[0], null));
      }
    }

    return results;
  }

  @Override
  public List<AddNodesResult> addNodes(AddNodesContext context, List<AddNodesItem> nodesToAdd) {
    var results = new ArrayList<AddNodesResult>();

    for (AddNodesItem item : nodesToAdd) {
      NodeId nodeId = item.getRequestedNewNodeId().toNodeId(getServer().getNamespaceTable()).get();

      if (isBlocked(nodeId)) {
        context.getOperationDiagnostics().report(item, diagnostic());
        results.add(new AddNodesResult(bad(), NodeId.NULL_VALUE));
      } else {
        results.add(new AddNodesResult(StatusCode.GOOD, nodeId));
      }
    }

    return results;
  }

  @Override
  public List<StatusCode> deleteNodes(
      DeleteNodesContext context, List<DeleteNodesItem> nodesToDelete) {

    return reportBlocked(context, nodesToDelete, DeleteNodesItem::getNodeId);
  }

  @Override
  public List<StatusCode> addReferences(
      AddReferencesContext context, List<AddReferencesItem> referencesToAdd) {

    return reportBlocked(context, referencesToAdd, AddReferencesItem::getSourceNodeId);
  }

  @Override
  public List<StatusCode> deleteReferences(
      DeleteReferencesContext context, List<DeleteReferencesItem> referencesToDelete) {

    return reportBlocked(context, referencesToDelete, DeleteReferencesItem::getSourceNodeId);
  }

  private <T> List<StatusCode> reportBlocked(
      ServiceOperationContext<T> context, List<T> items, Function<T, NodeId> nodeIdOf) {

    var results = new ArrayList<StatusCode>();

    for (T item : items) {
      if (isBlocked(nodeIdOf.apply(item))) {
        context.getOperationDiagnostics().report(item, diagnostic());
        results.add(bad());
      } else {
        results.add(StatusCode.GOOD);
      }
    }

    return Collections.unmodifiableList(results);
  }

  private boolean isBlocked(NodeId nodeId) {
    return newNodeId(BLOCKED).equals(nodeId);
  }

  private static StatusCode bad() {
    return new StatusCode(StatusCodes.Bad_NotWritable);
  }

  /** Fails every read and write of the Value attribute with this namespace's diagnostic. */
  private final class BlockingFilter implements AttributeFilter {

    @Override
    public @Nullable Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
        throws UaException {

      if (attributeId == AttributeId.Value) {
        throw new UaException(bad(), "read blocked for the test", diagnostic());
      }

      return ctx.readAttribute(attributeId);
    }

    @Override
    public void writeAttribute(
        AttributeFilterContext ctx, AttributeId attributeId, @Nullable Object value)
        throws UaException {

      if (attributeId == AttributeId.Value) {
        throw new UaException(bad(), "write blocked for the test", diagnostic());
      }

      ctx.writeAttribute(attributeId, value);
    }
  }
}

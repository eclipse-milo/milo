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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.NodeFieldAddress;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

/**
 * A {@link PublishedDataSetSource} that pulls a snapshot from the server's address space at publish
 * time, reading each {@link NodeFieldAddress}-backed field with one batched internal read per
 * publish cycle.
 *
 * <p>Field addresses are resolved against the server's {@link NamespaceTable} at read time; node
 * existence is not required: a missing node yields a {@code Bad_NodeIdUnknown} {@link DataValue}
 * for that field, and a field whose namespace URI can no longer be resolved (possible only after
 * reconfiguration) does too. Fields not backed by a {@link NodeFieldAddress} are left unsupplied
 * and publish as {@code Bad_NoData}.
 */
final class AddressSpacePublishedDataSetSource implements PublishedDataSetSource {

  private final OpcUaServer server;

  AddressSpacePublishedDataSetSource(OpcUaServer server) {
    this.server = server;
  }

  @Override
  public DataSetSnapshot read(PublishedDataSetReadContext context) {
    NamespaceTable namespaceTable = server.getNamespaceTable();

    DataSetSnapshot.Builder snapshot = DataSetSnapshot.builder(context);

    var fieldNames = new ArrayList<String>();
    var readValueIds = new ArrayList<ReadValueId>();

    for (FieldDefinition field : context.fields()) {
      if (field.getSource() instanceof NodeFieldAddress address) {
        Optional<NodeId> nodeId = address.nodeId().toNodeId(namespaceTable);

        if (nodeId.isPresent()) {
          fieldNames.add(field.getName());
          readValueIds.add(
              new ReadValueId(
                  nodeId.get(), address.attributeId().uid(), null, QualifiedName.NULL_VALUE));
        } else {
          snapshot.field(field.getName(), new DataValue(StatusCodes.Bad_NodeIdUnknown));
        }
      }
    }

    if (!readValueIds.isEmpty()) {
      var readContext = new ReadContext(server, null);

      List<DataValue> values =
          server
              .getAddressSpaceManager()
              .read(readContext, 0.0, TimestampsToReturn.Both, readValueIds);

      for (int i = 0; i < fieldNames.size() && i < values.size(); i++) {
        snapshot.field(fieldNames.get(i), values.get(i));
      }
    }

    return snapshot.build();
  }
}

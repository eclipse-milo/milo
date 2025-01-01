/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.util.Lists;

public class HistoryReadExampleProsys implements ClientExample {

  public static void main(String[] args) throws Exception {
    HistoryReadExampleProsys example = new HistoryReadExampleProsys();

    new ClientExampleRunner(example, false).run();
  }

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    HistoryReadDetails historyReadDetails =
        new ReadRawModifiedDetails(false, DateTime.MIN_VALUE, DateTime.now(), uint(0), true);

    HistoryReadValueId historyReadValueId =
        new HistoryReadValueId(
            new NodeId(3, "Counter"), null, QualifiedName.NULL_VALUE, ByteString.NULL_VALUE);

    List<HistoryReadValueId> nodesToRead = new ArrayList<>();
    nodesToRead.add(historyReadValueId);

    HistoryReadResponse historyReadResponse =
        client.historyRead(historyReadDetails, TimestampsToReturn.Both, false, nodesToRead);

    HistoryReadResult[] historyReadResults = historyReadResponse.getResults();

    if (historyReadResults != null) {
      HistoryReadResult historyReadResult = historyReadResults[0];
      StatusCode statusCode = historyReadResult.getStatusCode();

      if (statusCode.isGood()) {
        HistoryData historyData =
            (HistoryData)
                historyReadResult.getHistoryData().decode(client.getStaticEncodingContext());

        List<DataValue> dataValues = Lists.ofNullable(historyData.getDataValues());

        dataValues.forEach(v -> System.out.println("value=" + v));
      } else {
        System.out.println("History read failed: " + statusCode);
      }
    }

    future.complete(client);
  }

  @Override
  public String getEndpointUrl() {
    return "opc.tcp://localhost:53530/OPCUA/SimulationServer";
  }
}

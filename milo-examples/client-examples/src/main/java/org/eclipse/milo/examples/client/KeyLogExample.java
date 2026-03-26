/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.channel.WiresharkKeyLogWriter;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates OPC UA key logging for Wireshark decryption.
 *
 * <p>Starts an embedded server and client, both configured with {@link WiresharkKeyLogWriter}.
 * After the encrypted connection is established and some traffic is exchanged, the key log file
 * contents are printed. The resulting file can be loaded into Wireshark 4.4+ via Edit → Preferences
 * → Protocols → OPC UA → Key log file.
 */
public class KeyLogExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    var example = new KeyLogExample();

    logger.info("Server key log: {}", example.serverKeyLogFile);
    logger.info("Client key log: {}", example.clientKeyLogFile);

    new ClientExampleRunner(example).run();
  }

  private static final Logger logger = LoggerFactory.getLogger(KeyLogExample.class);

  private final Path serverKeyLogFile;
  private final Path clientKeyLogFile;
  private final WiresharkKeyLogWriter serverKeyLogWriter;
  private final WiresharkKeyLogWriter clientKeyLogWriter;

  public KeyLogExample() throws IOException {
    Path keyLogDir = Files.createTempDirectory("opcua-keylog");
    serverKeyLogFile = keyLogDir.resolve("server_keys.log");
    clientKeyLogFile = keyLogDir.resolve("client_keys.log");
    serverKeyLogWriter = new WiresharkKeyLogWriter(serverKeyLogFile);
    clientKeyLogWriter = new WiresharkKeyLogWriter(clientKeyLogFile);
  }

  @Override
  public String getEndpointUrl() {
    return "opc.tcp://localhost:4840/milo";
  }

  @Override
  public void configureServer(OpcUaServerConfigBuilder builder) {
    builder.setSecurityKeysListener(serverKeyLogWriter);
  }

  @Override
  public void configureClient(OpcUaClientConfigBuilder builder) {
    builder.setSecurityKeysListener(clientKeyLogWriter);
  }

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();
    logger.info("Connected with Basic256Sha256 / SignAndEncrypt");

    List<DataValue> values =
        client
            .readValuesAsync(
                0.0,
                TimestampsToReturn.Both,
                List.of(NodeIds.Server_ServerStatus_State, NodeIds.Server_ServerStatus_CurrentTime))
            .get();

    logger.info("ServerState = {}", values.get(0).value().value());
    logger.info("CurrentTime = {}", values.get(1).value().value());

    serverKeyLogWriter.close();
    clientKeyLogWriter.close();

    logger.info("--- Server key log ({}) ---", serverKeyLogFile);
    Files.readAllLines(serverKeyLogFile).forEach(line -> logger.info("  {}", line));

    logger.info("--- Client key log ({}) ---", clientKeyLogFile);
    Files.readAllLines(clientKeyLogFile).forEach(line -> logger.info("  {}", line));

    logger.info("Load either file into Wireshark 4.4+ via:");
    logger.info("  Edit > Preferences > Protocols > OPC UA > Key log file");

    future.complete(client);
  }
}

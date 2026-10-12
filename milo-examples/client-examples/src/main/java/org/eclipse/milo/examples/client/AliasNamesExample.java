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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameVerboseDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Looks up OPC UA Part 17 Alias Names on the example server: calls {@code FindAlias} and {@code
 * FindAliasVerbose} on the standard {@code Aliases} Object with the pattern {@code "Demo.%"},
 * decodes the results, then reads the Value of a resolved target to prove end-to-end resolution.
 *
 * <p>The demo aliases are created by the ExampleServer's AliasManager in a "MiloDemo" category
 * organized under the standard {@code TagVariables} Object; because the {@code FindAlias} search is
 * recursive, they are found from the {@code Aliases} root.
 */
public class AliasNamesExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    AliasNamesExample example = new AliasNamesExample();

    new ClientExampleRunner(example).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    // FindAlias is a standard Method instance defined by the base NodeSet; the pattern argument
    // uses the Part 4 "Like" grammar, so "Demo.%" matches every alias name starting "Demo.".
    AliasNameDataType[] aliases = findAlias(client, "Demo.%");

    for (AliasNameDataType alias : aliases) {
      logger.info(
          "FindAlias: {} -> {}",
          alias.getAliasName().name(),
          Arrays.toString(alias.getReferencedNodes()));
    }

    if (aliases.length == 0) {
      throw new UaException(StatusCodes.Bad_NotFound, "no aliases matched \"Demo.%\"");
    }

    // Prove end-to-end resolution: take the first alias's first target (targets are returned in
    // order of preference) and read its Value attribute.
    readFirstTarget(client, aliases[0]);

    // FindAliasVerbose is an Optional Method the standard NodeSet does not define an instance of;
    // the example server enables it on its AliasManager, which materializes the Method Node with
    // a NodeId allocated in the example namespace.
    NodeId findAliasVerboseId = NodeId.parse("ns=2;s=Aliases/FindAliasVerbose");

    AliasNameVerboseDataType[] verboseAliases =
        findAliasVerbose(client, findAliasVerboseId, "Demo.%");

    for (AliasNameVerboseDataType alias : verboseAliases) {
      logger.info(
          "FindAliasVerbose: {} -> {} (category={})",
          alias.getAliasName().name(),
          Arrays.toString(alias.getReferencedNodes()),
          alias.getAliasNameCategoryId());
    }

    future.complete(client);
  }

  private AliasNameDataType[] findAlias(OpcUaClient client, String pattern) throws UaException {
    ExtensionObject[] xos = callFindMethod(client, NodeIds.Aliases_FindAlias, pattern);

    var aliases = new AliasNameDataType[xos.length];
    for (int i = 0; i < xos.length; i++) {
      aliases[i] = (AliasNameDataType) xos[i].decode(client.getStaticEncodingContext());
    }
    return aliases;
  }

  private AliasNameVerboseDataType[] findAliasVerbose(
      OpcUaClient client, NodeId methodId, String pattern) throws UaException {

    ExtensionObject[] xos = callFindMethod(client, methodId, pattern);

    var aliases = new AliasNameVerboseDataType[xos.length];
    for (int i = 0; i < xos.length; i++) {
      aliases[i] = (AliasNameVerboseDataType) xos[i].decode(client.getStaticEncodingContext());
    }
    return aliases;
  }

  /**
   * Call a FindAlias-shaped Method on the standard {@code Aliases} Object and return the encoded
   * result entries; both FindAlias and FindAliasVerbose take an alias name search pattern and a
   * ReferenceType filter (a null NodeId means no restriction) and return a single output array.
   */
  private ExtensionObject[] callFindMethod(OpcUaClient client, NodeId methodId, String pattern)
      throws UaException {

    var request =
        new CallMethodRequest(
            NodeIds.Aliases,
            methodId,
            new Variant[] {new Variant(pattern), new Variant(NodeId.NULL_VALUE)});

    CallResponse response = client.call(List.of(request));

    CallMethodResult result = requireNonNull(response.getResults())[0];

    if (!result.getStatusCode().isGood()) {
      throw new UaException(result.getStatusCode());
    }

    Variant[] outputs = requireNonNull(result.getOutputArguments());

    return requireNonNullElse((ExtensionObject[]) outputs[0].value(), new ExtensionObject[0]);
  }

  private void readFirstTarget(OpcUaClient client, AliasNameDataType alias) throws UaException {
    ExpandedNodeId firstTarget = requireNonNull(alias.getReferencedNodes())[0];

    NodeId targetNodeId =
        firstTarget
            .toNodeId(client.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaException(
                        StatusCodes.Bad_NodeIdUnknown, "target is not local: " + firstTarget));

    DataValue value = client.readValue(0.0, TimestampsToReturn.Both, targetNodeId);

    logger.info(
        "read {} (target {}): {}",
        alias.getAliasName().name(),
        targetNodeId.toParseableString(),
        value.getValue().value());
  }
}

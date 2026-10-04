/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.examples.client.ClientExample;
import org.eclipse.milo.examples.client.LegacyDataTypeDictionaryExample;
import org.eclipse.milo.examples.client.prosys.ProsysHistoryReadExample;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.dtd.BsdStructWrapper;
import org.eclipse.milo.opcua.sdk.core.dtd.generic.Struct;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.ResourceLock;

/** Runs externally targeted source examples unchanged against a local protocol fixture. */
@Timeout(30)
@ResourceLock("java.lang.System.out")
class WikiSourceExamplesTest extends AbstractClientServerTest {
  private LegacyNamespace legacy;
  private ExampleHistoryService history;

  @Override
  protected TestServer createTestServer() throws Exception {
    TestServer fixture = TestServer.create();
    OpcUaServer localServer = fixture.getServer();
    // The original examples use namespace index 3, so reproduce that server-side address contract.
    localServer.getNamespaceTable().add("urn:eclipse:milo:wiki:reserved");
    legacy = new LegacyNamespace(localServer);
    assertEquals(3, legacy.getNamespaceIndex().intValue());
    legacy.startup();
    history = new ExampleHistoryService(localServer);
    localServer
        .getConfig()
        .getEndpoints()
        .forEach(endpoint -> localServer.addServiceSet(endpoint.getPath(), history));
    return fixture;
  }

  @AfterAll
  void stopLegacyNamespace() {
    if (legacy != null) legacy.shutdown();
  }

  @Test
  void originalHistoryBodyPrintsTheStoredValuesAndCompletes() throws Exception {
    int calls = history.calls;
    String output = executeOriginal(new ProsysHistoryReadExample());
    assertEquals(calls + 1, history.calls);
    assertEquals(2, output.lines().filter(line -> line.startsWith("value=")).count());
    assertTrue(output.contains("101"));
    assertTrue(output.contains("102"));
    assertTrue(history.lastRequest.getNodesToRead()[0].getContinuationPoint().isNullOrEmpty());
  }

  // This source example prints a bad operation result and completes; it does not throw it.
  @Test
  void originalHistoryBodyReportsUnsupportedHistoryAsOutput() throws Exception {
    history.reject = true;
    try {
      String output = executeOriginal(new ProsysHistoryReadExample());
      assertTrue(output.contains("History read failed:"));
      assertTrue(output.contains("Bad_HistoryOperationUnsupported"));
    } finally {
      history.reject = false;
    }
  }

  @Test
  void originalLegacyBodyDiscoversTheBsdDictionaryAndDecodesItsValue() throws Exception {
    String output = executeOriginal(new LegacyDataTypeDictionaryExample());
    assertTrue(output.contains("Decoded: "));
    assertTrue(output.contains("WorkOrder"));
    assertTrue(output.contains("12345"));
    var dictionary = client.getDynamicDataTypeManager().getTypeDictionary(LegacyNamespace.URI);
    assertTrue(dictionary != null);
    DataValue value = client.readValue(0, TimestampsToReturn.Neither, LegacyNamespace.VALUE);
    assertTrue(value.statusCode().isGood());
    ExtensionObject encoded = assertInstanceOf(ExtensionObject.class, value.value().value());
    BsdStructWrapper<?> wrapper =
        assertInstanceOf(
            BsdStructWrapper.class, encoded.decode(client.getDynamicEncodingContext()));
    assertEquals(new NodeId(3, "WorkOrder").expanded(), wrapper.getTypeId());
    assertEquals(new NodeId(3, "WorkOrder.Binary").expanded(), wrapper.getBinaryEncodingId());
    Struct decoded = assertInstanceOf(Struct.class, wrapper.object());
    assertEquals("WorkOrder", decoded.getName());
    assertEquals(12345, decoded.getMember("OrderId").getValue());
  }

  private String executeOriginal(ClientExample example) throws Exception {
    var bytes = new ByteArrayOutputStream();
    PrintStream original = System.out;
    try (var output = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
      System.setOut(output);
      var completion = new CompletableFuture<OpcUaClient>();
      example.run(client, completion);
      assertSame(client, completion.get(5, TimeUnit.SECONDS));
    } finally {
      System.setOut(original);
    }
    return bytes.toString(StandardCharsets.UTF_8);
  }

  /** A complete one-field BSD model, served through ordinary Browse and Read services. */
  private static final class LegacyNamespace extends ManagedNamespaceWithLifecycle {
    static final String URI = "urn:eclipse:milo:wiki:legacy";
    static final NodeId VALUE = new NodeId(3, "Demo.WorkOrder.WorkOrderVariable");

    LegacyNamespace(OpcUaServer server) {
      super(server, URI);
      getLifecycleManager().addStartupTask(this::createModel);
    }

    private void createModel() {
      NodeId typeId = newNodeId("WorkOrder");
      NodeId encodingId = newNodeId("WorkOrder.Binary");
      var type =
          new UaDataTypeNode(
              getNodeContext(),
              typeId,
              newQualifiedName("WorkOrder"),
              LocalizedText.english("WorkOrder"),
              LocalizedText.NULL_VALUE,
              uint(0),
              uint(0),
              false);
      getNodeManager().addNode(type);
      type.addReference(
          new Reference(typeId, NodeIds.HasSubtype, NodeIds.Structure.expanded(), false));
      var encoding =
          UaObjectNode.builder(getNodeContext())
              .setNodeId(encodingId)
              .setBrowseName(new QualifiedName(0, "Default Binary"))
              .setDisplayName(LocalizedText.english("Default Binary"))
              .setTypeDefinition(NodeIds.DataTypeEncodingType)
              .build();
      getNodeManager().addNode(encoding);
      link(type, NodeIds.HasEncoding, encoding);
      String schema =
          """
          <opc:TypeDictionary xmlns:opc="http://opcfoundation.org/BinarySchema/"
              xmlns:ua="http://opcfoundation.org/UA/"
              TargetNamespace="urn:eclipse:milo:wiki:legacy" DefaultByteOrder="LittleEndian">
            <opc:Import Namespace="http://opcfoundation.org/UA/" />
            <opc:StructuredType Name="WorkOrder" BaseType="ua:ExtensionObject">
              <opc:Field Name="OrderId" TypeName="opc:Int32" />
            </opc:StructuredType>
          </opc:TypeDictionary>
          """;
      UaVariableNode dictionary =
          variable(
              newNodeId("Dictionary"),
              "Dictionary",
              NodeIds.ByteString,
              NodeIds.DataTypeDictionaryType,
              ByteString.of(schema.getBytes(StandardCharsets.UTF_8)));
      dictionary.addReference(
          new Reference(
              dictionary.getNodeId(),
              NodeIds.HasComponent,
              NodeIds.OPCBinarySchema_TypeSystem.expanded(),
              false));
      UaVariableNode description =
          variable(
              newNodeId("WorkOrder.Description"),
              "WorkOrder",
              NodeIds.String,
              NodeIds.DataTypeDescriptionType,
              "WorkOrder");
      link(dictionary, NodeIds.HasComponent, description);
      link(encoding, NodeIds.HasDescription, description);
      ByteString body =
          ByteString.of(
              ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(12345).array());
      UaVariableNode value =
          variable(
              VALUE,
              "WorkOrderVariable",
              typeId,
              NodeIds.BaseDataVariableType,
              ExtensionObject.of(body, encodingId));
      value.addReference(
          new Reference(VALUE, NodeIds.Organizes, NodeIds.ObjectsFolder.expanded(), false));
    }

    private UaVariableNode variable(
        NodeId id, String name, NodeId dataType, NodeId type, Object value) {
      var node =
          UaVariableNode.builder(getNodeContext())
              .setNodeId(id)
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .setDataType(dataType)
              .setTypeDefinition(type)
              .setAccessLevel(AccessLevel.READ_ONLY)
              .setUserAccessLevel(AccessLevel.READ_ONLY)
              .setValue(new DataValue(new Variant(value)))
              .build();
      getNodeManager().addNode(node);
      return node;
    }

    private static void link(UaNode source, NodeId referenceType, UaNode target) {
      source.addReference(
          new Reference(source.getNodeId(), referenceType, target.getNodeId().expanded(), true));
    }
  }

  /** Supplies the exact single-page history request used by ProsysHistoryReadExample. */
  private static final class ExampleHistoryService extends DefaultAttributeServiceSet {
    private final OpcUaServer server;
    volatile boolean reject;
    volatile int calls;
    volatile HistoryReadRequest lastRequest;

    ExampleHistoryService(OpcUaServer server) {
      super(server);
      this.server = server;
    }

    @Override
    public HistoryReadResponse onHistoryRead(
        ServiceRequestContext context, HistoryReadRequest request) throws UaException {
      server.getSessionManager().getSession(context, request.getRequestHeader());
      calls++;
      lastRequest = request;
      var nodes = request.getNodesToRead();
      if (nodes == null || nodes.length != 1) throw new UaException(StatusCodes.Bad_NothingToDo);
      var raw =
          assertInstanceOf(
              ReadRawModifiedDetails.class,
              request.getHistoryReadDetails().decode(server.getStaticEncodingContext()));
      assertEquals(new NodeId(3, "Counter"), nodes[0].getNodeId());
      assertEquals(DateTime.MIN_VALUE, raw.getStartTime());
      assertTrue(raw.getReturnBounds());
      assertEquals(uint(0), raw.getNumValuesPerNode());
      assertEquals(TimestampsToReturn.Both, request.getTimestampsToReturn());
      var timestamp = new DateTime(Instant.parse("2026-01-01T00:00:00Z"));
      assertTrue(raw.getEndTime().getUtcTime() > timestamp.getUtcTime());
      HistoryReadResult result;
      if (reject) {
        result =
            new HistoryReadResult(
                new StatusCode(StatusCodes.Bad_HistoryOperationUnsupported),
                ByteString.NULL_VALUE,
                null);
      } else {
        var data =
            new HistoryData(
                new DataValue[] {
                  new DataValue(new Variant(101), StatusCode.GOOD, timestamp),
                  new DataValue(
                      new Variant(102), new StatusCode(StatusCodes.Bad_SensorFailure), timestamp)
                });
        result =
            new HistoryReadResult(
                StatusCode.GOOD,
                ByteString.NULL_VALUE,
                ExtensionObject.encode(server.getStaticEncodingContext(), data));
      }
      return new HistoryReadResponse(
          createResponseHeader(request), new HistoryReadResult[] {result}, null);
    }
  }
}
